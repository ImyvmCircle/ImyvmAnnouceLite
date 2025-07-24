package com.imyvm.broadcast

import com.imyvm.util.TextParser
import net.minecraft.server.MinecraftServer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object BroadcastScheduler {
    private var executor: ScheduledExecutorService? = null
    private var intervalSeconds = 30L
    private lateinit var server: MinecraftServer
    private var motdList: List<String> = emptyList()
    private var currentIndex = 0

    fun start(
        server: MinecraftServer,
        interval: Long = intervalSeconds,
        motdList: List<String> = BroadcastConfig.MOTD_LIST.value
    ) {
        this.server = server
        this.intervalSeconds = interval
        this.motdList = motdList
        this.currentIndex = 0

        executor = Executors.newSingleThreadScheduledExecutor()
        executor!!.scheduleAtFixedRate(
            { broadcastNextMessage() },
            intervalSeconds,
            intervalSeconds,
            TimeUnit.SECONDS
        )
    }

    fun stop() {
        executor?.shutdownNow()
        executor = null
    }

    private fun broadcastNextMessage() {
        if (motdList.isEmpty()) return

        val messageText = motdList[currentIndex]
        currentIndex = (currentIndex + 1) % motdList.size

        val message = TextParser.parseWithPrefix(messageText)
        server.playerManager.broadcast(message, false)
    }
}
