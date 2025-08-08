package com.imyvm.ial.commands

import com.imyvm.ial.ImyvmAnnouceLiteConfig.Companion.INTERVAL_SECONDS
import com.imyvm.ial.ImyvmAnnouceLiteConfig.Companion.MOTD_LIST
import com.imyvm.ial.ImyvmAnnounceLite
import com.imyvm.ial.ImyvmAnnounceLite.Companion.CONFIG
import com.imyvm.ial.ImyvmAnnounceLite.Companion.logger
import com.imyvm.ial.util.TextParser
import com.imyvm.ial.util.MsgCommandResponds
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource

fun register(dispatcher: CommandDispatcher<ServerCommandSource>, registryAccess: CommandRegistryAccess) {
    dispatcher.register(
        literal("imyvm-motd")
            .then(
                literal("list")
                    .executes { listMotds(it) }
            )
            .then(
                literal("add")
                    .requires { it.hasPermissionLevel(2) }
                    .then(
                        argument("message", StringArgumentType.greedyString())
                            .executes { addMotd(it) }
                    )
            )
            .then(
                literal("remove")
                    .requires { it.hasPermissionLevel(2) }
                    .then(
                        argument("index", StringArgumentType.word())
                            .executes { removeMotd(it) }
                    )
            )
            .then(
                literal("reload")
                    .requires { it.hasPermissionLevel(2) }
                    .executes { reloadMotd(it) }
            )
            .then(
                literal("timeset")
                    .requires{ it.hasPermissionLevel(2) }
                    .then(
                        argument("interval", StringArgumentType.word())
                            .executes { setTimeInterval(it) }
                )
            )
            .then(
                literal("timequery")
                    .executes { ctx ->
                        MsgCommandResponds.sendInfo(ctx.source, "motd.interval_query", mapOf("interval" to INTERVAL_SECONDS.value.toString()))
                        1
                    }
            )
            .then(
                literal("help")
                    .executes { ctx ->
                        MsgCommandResponds.sendInfo(ctx.source, "motd.help")
                        1
                    }
            )
            .then(
                literal("reset")
                    .requires { it.hasPermissionLevel(2) }
                    .executes { ctx ->
                        resetConfig(ctx)
                    }
            )
    )
}


private fun listMotds(ctx: CommandContext<ServerCommandSource>): Int {
    val motds = MOTD_LIST.value
    if (motds.isEmpty()) {
        MsgCommandResponds.sendInfo(ctx.source, "motd.empty")
    } else {
        motds.forEachIndexed { index, line ->
            val preview = TextParser.preview(line)
            MsgCommandResponds.sendMotdEntry(ctx.source, index, preview)
        }
    }
    return 1
}

private fun addMotd(ctx: CommandContext<ServerCommandSource>): Int {
    val newMsg = StringArgumentType.getString(ctx, "message")

    return updateMotd(ctx) { list ->
        list.add(newMsg)
        MsgCommandResponds.sendSuccess(ctx.source, "motd.added", mapOf("index" to list.lastIndex.toString()))
        true
    }
}

private fun removeMotd(ctx: CommandContext<ServerCommandSource>): Int {
    val indexStr = StringArgumentType.getString(ctx, "index")
    val index = indexStr.toIntOrNull()

    return updateMotd(ctx) { list ->
        if (index == null || index !in list.indices) {
            MsgCommandResponds.sendError(ctx.source, "motd.invalid_index")
            return@updateMotd false
        }
        list.removeAt(index)
        MsgCommandResponds.sendSuccess(ctx.source, "motd.removed", mapOf("index" to index.toString()))
        true
    }
}

private fun reloadMotd(ctx: CommandContext<ServerCommandSource>): Int {
    CONFIG.loadAndSave()
    ImyvmAnnounceLite.broadcastScheduler.restart(
        ctx.source.server,
        INTERVAL_SECONDS.value,
        MOTD_LIST.value
    )
    MsgCommandResponds.sendInfo(ctx.source, "motd.reloaded")
    return 1
}

private inline fun updateMotd(ctx: CommandContext<ServerCommandSource>, modifier: (MutableList<String>) -> Boolean): Int {
    val list = MOTD_LIST.value.toMutableList()
    val changed = modifier(list)
    if (changed) {
        MOTD_LIST.setValue(list)
        CONFIG.save()
        logger.info("MOTD updated: $list")

        ImyvmAnnounceLite.broadcastScheduler.restart(
            ctx.source.server,
            INTERVAL_SECONDS.value,
            list
        )
        logger.info("BroadcastScheduler restarted after MOTD update.")

        return 1
    }
    return 0
}

private fun setTimeInterval(ctx: CommandContext<ServerCommandSource>): Int {
    val newInterval = StringArgumentType.getString(ctx, "interval").toLongOrNull()
    if (newInterval == null || newInterval <= 0) {
        MsgCommandResponds.sendError(ctx.source, "motd.invalid_interval")
        return 0
    }

    INTERVAL_SECONDS.setValue(newInterval)
    CONFIG.save()
    logger.info("Motd time interval set to $newInterval seconds.")

    ImyvmAnnounceLite.broadcastScheduler.restart(
        ctx.source.server,
        INTERVAL_SECONDS.value,
        MOTD_LIST.value
    )
    MsgCommandResponds.sendSuccess(ctx.source, "motd.interval_set", mapOf("interval" to newInterval.toString()))
    return 1
}

private fun resetConfig(ctx: CommandContext<ServerCommandSource>): Int {
    MOTD_LIST.setValue(listOf(
        "&a服务器娘喵了一下～",
        "&c请遵守服务器规则，祝您游戏愉快！"
    ))
    INTERVAL_SECONDS.setValue(600)
    CONFIG.save()
    ImyvmAnnounceLite.broadcastScheduler.restart(
        ctx.source.server,
        INTERVAL_SECONDS.value,
        MOTD_LIST.value
    )
    MsgCommandResponds.sendSuccess(ctx.source, "motd.reset")
    return 1
}