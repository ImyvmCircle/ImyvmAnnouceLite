package com.imyvm.broadcast

import com.imyvm.hoki.config.ConfigOption
import com.imyvm.hoki.config.HokiConfig
import com.imyvm.hoki.config.Option
import com.typesafe.config.Config

class BroadcastConfig : HokiConfig("imyvm-annouce.conf") {
    companion object {
        @JvmField
        @ConfigOption
        val INTERVAL_SECONDS = Option(
            "core.broadcast.interval_seconds",
            30L,
            "The interval in seconds between each broadcast message."
        ) { obj: Config, path: String? ->
            obj.getLong(path)
        }

    }
}