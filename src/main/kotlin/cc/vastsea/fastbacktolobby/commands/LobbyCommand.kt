package cc.vastsea.fastbacktolobby.commands

import cc.vastsea.fastbacktolobby.FastBackToLobby
import cc.vastsea.fastbacktolobby.utils.TeleportUtils
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class LobbyCommand(private val plugin: FastBackToLobby) : CommandExecutor, TabCompleter {
    
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        
        // 处理重载命令
        if (command.name.equals("fastbacktolobby", ignoreCase = true)) {
            return handleReloadCommand(sender, args)
        }
        
        // 检查是否为玩家
        if (sender !is Player) {
            sender.sendMessage(plugin.languageManager.getMessage("error.console-cannot-use"))
            return true
        }
        
        // 检查权限
        if (!sender.hasPermission("fastbacktolobby.use")) {
            sender.sendMessage(plugin.languageManager.getMessage("error.no-permission"))
            return true
        }
        
        // 检查冷却时间（除非有绕过权限）
        val cooldownTime = plugin.configManager.getCooldown()
        if (cooldownTime > 0 && 
            !sender.hasPermission("fastbacktolobby.bypass.cooldown") && 
            plugin.cooldownManager.isOnCooldown(sender.uniqueId)) {
            val remaining = plugin.cooldownManager.getRemainingCooldown(sender.uniqueId)
            sender.sendMessage(plugin.languageManager.getMessage(
                "error.cooldown",
                "time" to remaining.toString()
            ))
            return true
        }
        
        // 获取大厅服务器名称
        val lobbyServer = plugin.configManager.getLobbyServer()
        if (lobbyServer.isEmpty()) {
            sender.sendMessage(plugin.languageManager.getMessage("error.lobby-not-configured"))
            return true
        }
        
        // 发送传送消息
        sender.sendMessage(plugin.languageManager.getMessage("success.teleporting"))
        
        // 播放音效
        if (plugin.configManager.isSoundEnabled()) {
            try {
                val sound = Sound.valueOf(plugin.configManager.getSound())
                sender.playSound(sender.location, sound, 1.0f, 1.0f)
            } catch (e: IllegalArgumentException) {
                plugin.configManager.debug("Invalid sound: ${plugin.configManager.getSound()}")
            }
        }
        
        // 设置冷却时间（除非有绕过权限）
        if (cooldownTime > 0 && !sender.hasPermission("fastbacktolobby.bypass.cooldown")) {
            plugin.cooldownManager.setCooldown(sender.uniqueId, cooldownTime)
        }
        
        // 传送到大厅
        val success = TeleportUtils.sendToLobby(sender, lobbyServer)
        
        if (!success) {
            sender.sendMessage(plugin.languageManager.getMessage(
                "error.server-not-found",
                "server" to lobbyServer
            ))
            // 移除冷却时间，因为传送失败
            plugin.cooldownManager.removeCooldown(sender.uniqueId)
        }
        
        return true
    }
    
    private fun handleReloadCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.isNotEmpty()) {
            when (args[0].lowercase()) {
                "reload" -> {
                    if (!sender.hasPermission("fastbacktolobby.admin.reload")) {
                        sender.sendMessage(plugin.languageManager.getMessage("error.no-permission"))
                        return true
                    }
                    
                    val success = plugin.reloadPlugin()
                    if (success) {
                        sender.sendMessage(plugin.languageManager.getMessage("admin.config-reloaded"))
                    } else {
                        sender.sendMessage(plugin.languageManager.getMessage("admin.reload-failed"))
                    }
                    return true
                }
                "permissions", "perms" -> {
                    return handlePermissionsCommand(sender)
                }
            }
        }
        
        // 显示帮助信息
        sender.sendMessage(plugin.languageManager.getMessage("help.header"))
        sender.sendMessage(plugin.languageManager.getMessage("help.commands"))
        if (sender.hasPermission("fastbacktolobby.admin")) {
            sender.sendMessage(plugin.languageManager.getMessage("help.admin-commands"))
        }
        sender.sendMessage(plugin.languageManager.getMessage("help.description"))
        sender.sendMessage(plugin.languageManager.getMessage("help.permissions"))
        sender.sendMessage(plugin.languageManager.getMessage("help.footer"))
        
        return true
    }
    
    private fun handlePermissionsCommand(sender: CommandSender): Boolean {
        sender.sendMessage(plugin.languageManager.getMessage("admin.permissions-info"))
        sender.sendMessage(plugin.languageManager.getMessage("admin.permission-use"))
        sender.sendMessage(plugin.languageManager.getMessage("admin.permission-admin"))
        sender.sendMessage(plugin.languageManager.getMessage("admin.permission-reload"))
        sender.sendMessage(plugin.languageManager.getMessage("admin.permission-bypass"))
        sender.sendMessage(plugin.languageManager.getMessage("admin.permission-all"))
        
        // 显示当前用户的权限状态
        if (sender is Player) {
            sender.sendMessage("")
            sender.sendMessage(plugin.languageManager.getMessage("prefix") + "&eYour permissions:")
            
            if (sender.hasPermission("fastbacktolobby.*")) {
                sender.sendMessage(plugin.languageManager.getMessage("prefix") + "&a✓ All permissions")
            } else {
                if (sender.hasPermission("fastbacktolobby.use")) {
                    sender.sendMessage(plugin.languageManager.getMessage("prefix") + "&a✓ Use lobby commands")
                }
                if (sender.hasPermission("fastbacktolobby.admin")) {
                    sender.sendMessage(plugin.languageManager.getMessage("prefix") + "&a✓ Admin permissions")
                }
                if (sender.hasPermission("fastbacktolobby.admin.reload")) {
                    sender.sendMessage(plugin.languageManager.getMessage("prefix") + "&a✓ Reload configuration")
                }
                if (sender.hasPermission("fastbacktolobby.bypass.cooldown")) {
                    sender.sendMessage(plugin.languageManager.getMessage("prefix") + "&a✓ Bypass cooldown")
                }
            }
        }
        
        return true
    }
    
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        val completions = mutableListOf<String>()
        
        // 只为fastbacktolobby命令提供tab补全
        if (command.name.equals("fastbacktolobby", ignoreCase = true)) {
            when (args.size) {
                1 -> {
                    // 第一个参数的补全选项
                    val subCommands = mutableListOf<String>()
                    
                    if (sender.hasPermission("fastbacktolobby.admin.reload")) {
                        subCommands.add("reload")
                    }
                    
                    subCommands.add("permissions")
                    subCommands.add("help")
                    
                    // 过滤匹配的命令
                    val input = args[0].lowercase()
                    completions.addAll(subCommands.filter { it.startsWith(input) })
                }
            }
        }
        
        return completions
    }
}