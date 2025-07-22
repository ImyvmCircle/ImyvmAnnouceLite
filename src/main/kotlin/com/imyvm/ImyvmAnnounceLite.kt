package com.imyvm

import com.imyvm.broadcast.BroadcastConfig.Companion.INTERVAL_SECONDS
import com.imyvm.broadcast.BroadcastScheduler
import com.imyvm.commands.register
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.LoggerFactory

object ImyvmAnnounceLite : ModInitializer {
    private val logger = LoggerFactory.getLogger("imyvm-announce-lite")

	override fun onInitialize() {
		CommandRegistrationCallback.EVENT.register { dispatcher, commandRegistryAccess, _ ->
			register(dispatcher, commandRegistryAccess)
		}
		ServerLifecycleEvents.SERVER_STARTED.register { server ->
			BroadcastScheduler.start(server, INTERVAL_SECONDS.value)
			logger.info("Imyvm Announce Lite has been initialized.")
		}
		ServerLifecycleEvents.SERVER_STOPPED.register {
			BroadcastScheduler.stop()
			logger.info("Imyvm Announce Lite is shutting down.")
		}
		logger.info("Hello Fabric world!")
	}
}