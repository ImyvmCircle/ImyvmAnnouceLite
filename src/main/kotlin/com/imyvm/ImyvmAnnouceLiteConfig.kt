package com.imyvm

import com.imyvm.hoki.config.ConfigOption
import com.imyvm.hoki.config.HokiConfig
import com.imyvm.hoki.config.Option
import com.typesafe.config.Config

class ImyvmAnnouceLiteConfig : HokiConfig("imyvm-annouce.conf") {
    companion object {
        @JvmField
        @ConfigOption
        val INTERVAL_SECONDS = Option(
            "core.imyvm-annouce.interval_seconds",
            600L,
            "The interval in seconds between each broadcast message."
        ) { obj: Config, path: String? ->
            obj.getLong(path)
        }

        @JvmField
        @ConfigOption
        val MOTD_LIST = Option(
            "core.imyvm-annouce.motd_list",
            listOf(
                "&a服务器娘喵了一下～",
                "&c请遵守服务器规则，祝您游戏愉快！"
        ),
            "The list of messages to broadcast."
        ) { obj: Config, path: String? ->
            obj.getStringList(path)
        }
    }
}