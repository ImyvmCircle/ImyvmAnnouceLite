package com.imyvm.util

import com.imyvm.ImyvmAnnouceLiteConfig
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigValueFactory
import java.io.File
import java.io.PrintWriter

object ImyvmAnnouceLiteConfigHandler {
    private val imyvmAnnouceLiteConfig = ImyvmAnnouceLiteConfig()

    fun getMotdList(): List<String> {
        return ImyvmAnnouceLiteConfig.MOTD_LIST.value
    }

    fun updateMotdList(newList: List<String>): Boolean {
        val configFile = imyvmAnnouceLiteConfig.javaClass
            .getDeclaredField("configFile")
            .apply { isAccessible = true }
            .get(imyvmAnnouceLiteConfig) as File

        val raw = ConfigFactory.parseFile(configFile)

        val updated = raw.withValue(
            "core.imyvm-annouce.motd_list",
            ConfigValueFactory.fromIterable(newList)
        )

        val options = ConfigRenderOptions.defaults()
            .setJson(false)
            .setOriginComments(false)

        PrintWriter(configFile).use { writer ->
            writer.write(updated.root().render(options))
        }

        return reload()
    }

    private fun reload(): Boolean {
        return imyvmAnnouceLiteConfig.load()
    }
}
