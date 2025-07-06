package cc.vastsea.fastbacktolobby

import cc.vastsea.fastbacktolobby.commands.LobbyCommand
import cc.vastsea.fastbacktolobby.managers.ConfigManager
import cc.vastsea.fastbacktolobby.managers.LanguageManager
import cc.vastsea.fastbacktolobby.managers.CooldownManager
import org.bukkit.plugin.java.JavaPlugin

class FastBackToLobby : JavaPlugin() {
    
    companion object {
        lateinit var instance: FastBackToLobby
            private set
    }
    
    lateinit var configManager: ConfigManager
        private set
    
    lateinit var languageManager: LanguageManager
        private set
        
    lateinit var cooldownManager: CooldownManager
        private set
    
    override fun onEnable() {
        instance = this
        
        // 初始化管理器
        configManager = ConfigManager(this)
        languageManager = LanguageManager(this)
        cooldownManager = CooldownManager()
        
        // 加载配置
        configManager.loadConfig()
        languageManager.loadLanguage()
        
        // 注册BungeeCord消息通道
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        
        // 注册命令
        registerCommands()
        
        // 检查代理服务器环境配置
        val isBungeeCordEnabled = server.spigot().config.getBoolean("settings.bungeecord", false)
        val isForceProxyMode = configManager.isForceProxyMode()
        
        if (!isBungeeCordEnabled && !isForceProxyMode) {
            logger.warning(languageManager.getMessage("plugin.proxy-warning"))
            logger.warning(languageManager.getMessage("plugin.proxy-instruction"))
        } else if (isForceProxyMode) {
            logger.info(languageManager.getMessage("plugin.force-proxy-enabled"))
        }
        
        logger.info(languageManager.getMessage("plugin.enabled"))
    }
    
    override fun onDisable() {
        logger.info(languageManager.getMessage("plugin.disabled"))
    }
    
    private fun registerCommands() {
        val lobbyCommand = LobbyCommand(this)
        
        // 注册所有配置的命令
        val commands = configManager.getCommands()
        commands.forEach { command ->
            getCommand(command)?.setExecutor(lobbyCommand)
        }
        
        // 注册重载命令和tab补全
        getCommand("fastbacktolobby")?.let { cmd ->
            cmd.setExecutor(lobbyCommand)
            cmd.tabCompleter = lobbyCommand
        }
    }
    
    fun reloadPlugin(): Boolean {
        return try {
            configManager.loadConfig()
            languageManager.loadLanguage()
            true
        } catch (e: Exception) {
            logger.severe("Failed to reload plugin: ${e.message}")
            false
        }
    }
}