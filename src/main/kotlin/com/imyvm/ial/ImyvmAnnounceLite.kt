package com.imyvm.ial

import com.imyvm.ial.ImyvmAnnouceLiteConfig.Companion.INTERVAL_SECONDS
import com.imyvm.ial.ImyvmAnnouceLiteConfig.Companion.MOTD_LIST
import com.imyvm.ial.commands.register
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ImyvmAnnounceLite : ModInitializer {
	override fun onInitialize() {
		CONFIG.loadAndSave()

		CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, _ ->
			register(dispatcher, registryAccess)
		}

		ServerLifecycleEvents.SERVER_STARTED.register { server ->
			broadcastScheduler.start(
				server,
				INTERVAL_SECONDS.value,
				MOTD_LIST.value,
			)
			logger.info("Imyvm Announce Lite has been initialized.")
		}

		ServerLifecycleEvents.SERVER_STOPPED.register {
			broadcastScheduler.stop()
			logger.info("Imyvm Announce Lite is shutting down.")
		}
	}

	companion object {
		const val MOD_ID = "imyvm_announce_lite"
		@JvmField
		val logger: Logger = LoggerFactory.getLogger(MOD_ID)
		val CONFIG: ImyvmAnnouceLiteConfig = ImyvmAnnouceLiteConfig()

		val broadcastScheduler = BroadcastScheduler()
	}
}
