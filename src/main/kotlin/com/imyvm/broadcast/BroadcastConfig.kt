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
            "core.imyvm-annouce.interval_seconds",
            30L,
            "The interval in seconds between each broadcast message."
        ) { obj: Config, path: String? ->
            obj.getLong(path)
        }

        @JvmField
        @ConfigOption
        val MOTD_LIST = Option(
            "core.imyvm-annouce.motd_list",
            listOf(
                "<green>绿色文本 <red>红色文本 <blue>蓝色文本</blue> 红色结束</red> 绿色结束</green>",
                "普通文本行喵喵",
                "<yellow><italic>黄色斜体文本</italic> 之后是正常黄色</yellow>",
                "<blue>第一行蓝色</blue>\n<gold><bold>第二行金色加粗</bold></gold>\n第三行默认",
        ),
            "The list of messages to broadcast."
        ) { obj: Config, path: String? ->
            obj.getStringList(path)
        }
    }
}