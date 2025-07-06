package cc.vastsea.fastbacktolobby.utils

import cc.vastsea.fastbacktolobby.FastBackToLobby
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

object TeleportUtils {
    
    /**
     * 将玩家传送到大厅服务器
     * @param player 要传送的玩家
     * @param serverName 目标服务器名称
     * @return 是否成功发送传送请求
     */
    fun sendToLobby(player: Player, serverName: String): Boolean {
        return try {
            val plugin = FastBackToLobby.instance
            
            // 检查是否在代理服务器环境中
            if (!isProxyEnvironment()) {
                plugin.configManager.debug("Not in proxy environment, cannot teleport to lobby")
                return false
            }
            
            // 创建BungeeCord消息
            val out = ByteArrayOutputStream()
            val dataOut = DataOutputStream(out)
            
            // 写入命令和服务器名称
            dataOut.writeUTF("Connect")
            dataOut.writeUTF(serverName)
            
            // 发送插件消息
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray())
            
            plugin.configManager.debug("Sent teleport request for ${player.name} to server: $serverName")
            
            // 延迟发送成功消息，给传送一些时间
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                if (player.isOnline) {
                    player.sendMessage(plugin.languageManager.getMessage("success.teleported"))
                }
            }, 20L) // 1秒后
            
            true
        } catch (e: Exception) {
            FastBackToLobby.instance.logger.severe("Failed to send player to lobby: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 检查是否在代理服务器环境中
     * @return 是否在代理环境中
     */
    private fun isProxyEnvironment(): Boolean {
        val plugin = FastBackToLobby.instance
        val server = plugin.server
        
        // 检查配置文件中是否强制启用代理模式
        if (plugin.configManager.isForceProxyMode()) {
            plugin.configManager.debug("Force proxy mode enabled in config")
            return true
        }
        
        // 检查是否启用了BungeeCord
        if (server.spigot().config.getBoolean("settings.bungeecord", false)) {
            plugin.configManager.debug("BungeeCord mode detected in spigot.yml")
            return true
        }
        
        // 检查Velocity相关的环境变量或系统属性
        if (System.getProperty("velocity.native.enabled") != null ||
            System.getProperty("velocity.forwarding.secret") != null) {
            plugin.configManager.debug("Velocity environment detected")
            return true
        }
        
        // 检查是否有相关的代理插件
        val pluginManager = server.pluginManager
        if (pluginManager.isPluginEnabled("ViaVersion") || 
            pluginManager.isPluginEnabled("ProtocolLib") ||
            pluginManager.isPluginEnabled("VelocityForward") ||
            pluginManager.isPluginEnabled("ModernForwarding")) {
            plugin.configManager.debug("Proxy-related plugins detected")
            return true
        }
        
        // 检查服务器属性
        try {
            val onlineMode = server.onlineMode
            if (!onlineMode) {
                // 通常代理服务器会将子服务器设置为offline-mode
                plugin.configManager.debug("Offline mode detected, assuming proxy environment")
                return true
            }
        } catch (e: Exception) {
            // 忽略异常
        }
        
        plugin.configManager.debug("No proxy environment detected")
        return false
    }
    
    /**
     * 验证服务器名称是否有效
     * @param serverName 服务器名称
     * @return 是否有效
     */
    fun isValidServerName(serverName: String): Boolean {
        if (serverName.isBlank()) return false
        
        // 检查服务器名称格式（只允许字母、数字、下划线和连字符）
        val validPattern = Regex("^[a-zA-Z0-9_-]+$")
        return validPattern.matches(serverName)
    }
    
    /**
     * 获取推荐的大厅服务器名称列表
     * @return 常用的大厅服务器名称
     */
    fun getCommonLobbyNames(): List<String> {
        return listOf(
            "lobby",
            "hub",
            "main",
            "spawn",
            "lobby1",
            "hub1",
            "main-lobby",
            "central"
        )
    }
}