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
    )

    fun sendInfo(source: ServerCommandSource, key: String, placeholders: Map<String, String> = emptyMap()) {
        source.sendMessage(Text.literal("§7[MOTD] " + format(key, placeholders)))
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
