package com.imyvm.commands

import com.imyvm.util.ImyvmAnnouceLiteConfigHandler
import com.imyvm.util.TextParser
import com.imyvm.util.MsgCommandResponds
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

fun register(dispatcher: CommandDispatcher<ServerCommandSource>, registryAccess: CommandRegistryAccess) {
    dispatcher.register(
        CommandManager.literal("motd")
            .then(CommandManager.literal("list")
                .executes { ctx -> listMotds(ctx) })
            .then(CommandManager.literal("add")
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                    .executes { ctx -> addMotd(ctx) }))
            .then(CommandManager.literal("remove")
                .then(CommandManager.argument("index", StringArgumentType.word())
                    .executes { ctx -> removeMotd(ctx) }))
            .then(CommandManager.literal("edit")
                .then(CommandManager.argument("index", StringArgumentType.word())
                    .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes { ctx -> editMotd(ctx) })))
    )
}

private fun listMotds(ctx: CommandContext<ServerCommandSource>): Int {
    val motds = ImyvmAnnouceLiteConfigHandler.getMotdList()
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
    if (!TextParser.validate(newMsg)) {
        MsgCommandResponds.sendError(ctx.source, "motd.invalid_format")
        return 0
    }

    val list = ImyvmAnnouceLiteConfigHandler.getMotdList().toMutableList()
    list.add(newMsg)

    if (!ImyvmAnnouceLiteConfigHandler.updateMotdList(list)) {
        MsgCommandResponds.sendError(ctx.source, "motd.update_failed")
        return 0
    }

    MsgCommandResponds.sendSuccess(ctx.source, "motd.added", mapOf("index" to list.lastIndex.toString()))
    return 1
}

private fun removeMotd(ctx: CommandContext<ServerCommandSource>): Int {
    val indexStr = StringArgumentType.getString(ctx, "index")
    val index = indexStr.toIntOrNull()
    val list = ImyvmAnnouceLiteConfigHandler.getMotdList().toMutableList()

    if (index == null || index !in list.indices) {
        MsgCommandResponds.sendError(ctx.source, "motd.invalid_index")
        return 0
    }

    list.removeAt(index)
    if (!ImyvmAnnouceLiteConfigHandler.updateMotdList(list)) {
        MsgCommandResponds.sendError(ctx.source, "motd.update_failed")
        return 0
    }

    MsgCommandResponds.sendSuccess(ctx.source, "motd.removed", mapOf("index" to index.toString()))
    return 1
}

private fun editMotd(ctx: CommandContext<ServerCommandSource>): Int {
    val indexStr = StringArgumentType.getString(ctx, "index")
    val index = indexStr.toIntOrNull()
    val newMsg = StringArgumentType.getString(ctx, "message")

    if (!TextParser.validate(newMsg)) {
        MsgCommandResponds.sendError(ctx.source, "motd.invalid_format")
        return 0
    }

    val list = ImyvmAnnouceLiteConfigHandler.getMotdList().toMutableList()
    if (index == null || index !in list.indices) {
        MsgCommandResponds.sendError(ctx.source, "motd.invalid_index")
        return 0
    }

    list[index] = newMsg
    if (!ImyvmAnnouceLiteConfigHandler.updateMotdList(list)) {
        MsgCommandResponds.sendError(ctx.source, "motd.update_failed")
        return 0
    }

    MsgCommandResponds.sendSuccess(ctx.source, "motd.updated", mapOf("index" to index.toString()))
    return 1
}

