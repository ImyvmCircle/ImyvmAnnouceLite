package com.imyvm

import com.imyvm.util.TextParser
import net.minecraft.server.MinecraftServer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BroadcastScheduler {
    private var executor: ScheduledExecutorService? = null
    private var intervalSeconds = 30L
    private var server: MinecraftServer? = null
    private var motdList: List<String> = emptyList()
    private var currentIndex = 0

    fun start(server: MinecraftServer, interval: Long, motdList: List<String>) {
        stop()
        this.server = server
        this.intervalSeconds = interval
        this.motdList = motdList
        currentIndex = 0

        if (motdList.isEmpty()) return

        executor = Executors.newSingleThreadScheduledExecutor()
        executor!!.scheduleAtFixedRate({
            broadcastNextMessage()
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS)
    }

    fun stop() {
        executor?.shutdownNow()
        executor = null
    }

    fun restart(server: MinecraftServer, interval: Long, motdList: List<String>) {
        start(server, interval, motdList)
    }

    private fun broadcastNextMessage() {
        val s = server ?: return
        if (motdList.isEmpty()) return

        val messageText = motdList[currentIndex]
        currentIndex = (currentIndex + 1) % motdList.size

        val message = TextParser.parseWithPrefix(messageText)
        s.playerManager.broadcast(message, false)
    }
}

