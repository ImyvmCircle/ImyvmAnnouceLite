package com.imyvm.util

import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object MsgCommandResponds {
    private val messages: Map<String, String> = mapOf(
        "motd.empty" to "§7当前没有公告。",
        "motd.invalid_index" to "§c无效编号，请输入有效索引。",
        "motd.update_failed" to "§c配置更新失败。",
        "motd.added" to "§a已添加公告，编号为 {index}。",
        "motd.removed" to "§a已移除第 {index} 条公告。",
        "motd.reloaded" to "§a公告配置已重新加载。",
        "motd.interval_query" to "§7当前公告间隔为 {interval} 秒。",
        "motd.interval_invalid" to "§c无效的公告间隔，请输入一个合法的正整数。",
        "motd.interval_set" to "§a公告间隔已设置为 {interval} 秒。",
        "motd.help" to "§7/imyvm-motd list - 查看当前公告列表\n" +
                "§7/imyvm-motd add <message> - 添加新的公告\n" +
                "§7/imyvm-motd remove <index> - 移除指定编号的公告\n" +
                "§7/imyvm-motd reload - 重新加载公告配置\n" +
                "§7/imyvm-motd timeset <interval> - 设置公告间隔时间（秒）\n" +
                "§7/imyvm-motd timequery - 查询当前公告间隔时间\n" +
                "§7/imyvm-motd help - 显示帮助信息\n" +
                "§7/imyvm-motd reset - 重置mod配置",
        "motd.reset" to "§a公告已重置为默认配置。"
    )

    fun sendInfo(source: ServerCommandSource, key: String, placeholders: Map<String, String> = emptyMap()) {
        source.sendMessage(Text.literal("§7[IMYVM-MOTD] " + format(key, placeholders)))
    }

    fun sendError(source: ServerCommandSource, key: String, placeholders: Map<String, String> = emptyMap()) {
        source.sendMessage(Text.literal("§c[错误] " + format(key, placeholders)))
    }

    fun sendSuccess(source: ServerCommandSource, key: String, placeholders: Map<String, String> = emptyMap()) {
        source.sendMessage(Text.literal("§a[成功] " + format(key, placeholders)))
    }

    fun sendMotdEntry(source: ServerCommandSource, index: Int, content: String) {
        source.sendMessage(Text.literal("§7[$index] §r$content"))
    }

    private fun format(key: String, placeholders: Map<String, String>): String {
        var message = messages[key] ?: key
        for ((k, v) in placeholders) {
            message = message.replace("{$k}", v)
        }
        return message
    }
}
