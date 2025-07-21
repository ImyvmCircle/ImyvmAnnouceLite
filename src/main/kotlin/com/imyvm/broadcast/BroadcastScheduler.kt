package com.imyvm.broadcast

import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object BroadcastScheduler {
    private var executor: ScheduledExecutorService? = null
    private var intervalSeconds = 30L
    private lateinit var server: MinecraftServer
    fun start(server: MinecraftServer,
              inteval: Long = intervalSeconds) {
        this.server = server
        this.intervalSeconds = inteval

        executor = Executors.newSingleThreadScheduledExecutor()
        executor!!.scheduleAtFixedRate(
            {broadcastMessage("喵")},
            intervalSeconds,
            intervalSeconds,
            TimeUnit.SECONDS
        )
    }

    fun stop() {
        executor?.shutdownNow()
        executor = null
    }

    private fun broadcastMessage(s: String) {
        val message = Text.literal(s).formatted(Formatting.DARK_GREEN)
        server.playerManager.broadcast(message, false)
    }
}