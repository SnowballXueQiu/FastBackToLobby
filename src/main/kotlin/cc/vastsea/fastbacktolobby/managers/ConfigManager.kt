package com.example.fastbacktolobby.managers

import com.example.fastbacktolobby.FastBackToLobby
import org.bukkit.configuration.file.FileConfiguration

class ConfigManager(private val plugin: FastBackToLobby) {
    
    private lateinit var config: FileConfiguration
    
    fun loadConfig() {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
        config = plugin.config
    }
    
    fun getLobbyServer(): String {
        return config.getString("lobby-server") ?: "lobby"
    }
    
    fun getLanguage(): String {
        return config.getString("language") ?: "en_US"
    }
    
    fun isDebugEnabled(): Boolean {
        return config.getBoolean("debug", false)
    }
    
    fun getCooldown(): Int {
        return config.getInt("cooldown", 3)
    }
    
    fun isSoundEnabled(): Boolean {
        return config.getBoolean("enable-sound", true)
    }
    
    fun getSound(): String {
        return config.getString("sound") ?: "ENTITY_ENDERMAN_TELEPORT"
    }
    
    fun getCommands(): List<String> {
        return config.getStringList("commands").ifEmpty {
            listOf("quit", "l", "lobby", "leave", "hub")
        }
    }
    
    fun debug(message: String) {
        if (isDebugEnabled()) {
            plugin.logger.info("[DEBUG] $message")
        }
    }
}