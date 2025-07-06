package cc.vastsea.fastbacktolobby.managers

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CooldownManager {
    
    private val cooldowns = ConcurrentHashMap<UUID, Long>()
    
    /**
     * 设置玩家冷却时间
     * @param playerUUID 玩家UUID
     * @param cooldownSeconds 冷却时间（秒）
     */
    fun setCooldown(playerUUID: UUID, cooldownSeconds: Int) {
        if (cooldownSeconds <= 0) return
        
        val expireTime = System.currentTimeMillis() + (cooldownSeconds * 1000L)
        cooldowns[playerUUID] = expireTime
    }
    
    /**
     * 检查玩家是否在冷却中
     * @param playerUUID 玩家UUID
     * @return 是否在冷却中
     */
    fun isOnCooldown(playerUUID: UUID): Boolean {
        val expireTime = cooldowns[playerUUID] ?: return false
        
        if (System.currentTimeMillis() >= expireTime) {
            cooldowns.remove(playerUUID)
            return false
        }
        
        return true
    }
    
    /**
     * 获取剩余冷却时间
     * @param playerUUID 玩家UUID
     * @return 剩余冷却时间（秒），如果没有冷却则返回0
     */
    fun getRemainingCooldown(playerUUID: UUID): Int {
        val expireTime = cooldowns[playerUUID] ?: return 0
        
        val remaining = (expireTime - System.currentTimeMillis()) / 1000L
        
        if (remaining <= 0) {
            cooldowns.remove(playerUUID)
            return 0
        }
        
        return remaining.toInt()
    }
    
    /**
     * 移除玩家的冷却时间
     * @param playerUUID 玩家UUID
     */
    fun removeCooldown(playerUUID: UUID) {
        cooldowns.remove(playerUUID)
    }
    
    /**
     * 清除所有冷却时间
     */
    fun clearAllCooldowns() {
        cooldowns.clear()
    }
    
    /**
     * 清理过期的冷却时间
     */
    fun cleanupExpiredCooldowns() {
        val currentTime = System.currentTimeMillis()
        cooldowns.entries.removeIf { (_, expireTime) ->
            currentTime >= expireTime
        }
    }
}