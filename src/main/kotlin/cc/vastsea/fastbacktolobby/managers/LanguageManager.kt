package com.example.fastbacktolobby.managers

import com.example.fastbacktolobby.FastBackToLobby
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader

class LanguageManager(private val plugin: FastBackToLobby) {
    
    private lateinit var languageConfig: FileConfiguration
    private var currentLanguage: String = "en_US"
    
    fun loadLanguage() {
        currentLanguage = plugin.configManager.getLanguage()
        
        val langFile = File(plugin.dataFolder, "lang/$currentLanguage.yml")
        
        // 如果语言文件不存在，创建默认文件
        if (!langFile.exists()) {
            plugin.saveResource("lang/$currentLanguage.yml", false)
        }
        
        // 如果仍然不存在，使用英文作为后备
        if (!langFile.exists()) {
            currentLanguage = "en_US"
            val fallbackFile = File(plugin.dataFolder, "lang/en_US.yml")
            if (!fallbackFile.exists()) {
                plugin.saveResource("lang/en_US.yml", false)
            }
            languageConfig = YamlConfiguration.loadConfiguration(fallbackFile)
        } else {
            languageConfig = YamlConfiguration.loadConfiguration(langFile)
        }
        
        // 加载默认配置作为后备
        val defaultConfigStream = plugin.getResource("lang/$currentLanguage.yml")
        if (defaultConfigStream != null) {
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigStream))
            languageConfig.setDefaults(defaultConfig)
        }
        
        plugin.configManager.debug("Loaded language: $currentLanguage")
    }
    
    fun getMessage(path: String, vararg placeholders: Pair<String, String>): String {
        var message = languageConfig.getString(path) ?: "Missing message: $path"
        
        // 替换占位符
        placeholders.forEach { (key, value) ->
            message = message.replace("{$key}", value)
        }
        
        // 替换前缀
        val prefix = languageConfig.getString("prefix") ?: "&8[&6FastBackToLobby&8] &r"
        message = message.replace("{prefix}", prefix)
        
        // 转换颜色代码
        return ChatColor.translateAlternateColorCodes('&', message)
    }
    
    fun getMessageList(path: String, vararg placeholders: Pair<String, String>): List<String> {
        val messages = languageConfig.getStringList(path)
        return messages.map { message ->
            var processedMessage = message
            placeholders.forEach { (key, value) ->
                processedMessage = processedMessage.replace("{$key}", value)
            }
            val prefix = languageConfig.getString("prefix") ?: "&8[&6FastBackToLobby&8] &r"
            processedMessage = processedMessage.replace("{prefix}", prefix)
            ChatColor.translateAlternateColorCodes('&', processedMessage)
        }
    }
    
    fun getCurrentLanguage(): String {
        return currentLanguage
    }
}