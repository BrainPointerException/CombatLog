package de.brainexception.combatlog.combat;

import de.brainexception.combatlog.CombatLogPlugin;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatManager implements Combat {

    private final CombatLogPlugin plugin;
    private final Map<UUID, Long> timerMap;
    private final Map<UUID, UUID> enemyMap;

    public CombatManager(CombatLogPlugin plugin) {
        this.plugin = plugin;
        this.timerMap = new ConcurrentHashMap<>();
        this.enemyMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isInCombat(Player player) {
        return getCombatPlayers().contains(player.getUniqueId());
    }

    @Override
    public boolean setInCombat(Player player, Player enemy) {
        if (player.getPlayer() == null) {
            plugin.getLogger().warning("setInCombat: Player = null");
            return false;
        }
        if (enemy == null) {
            plugin.getLogger().warning("setInCombat: Enemy = null");
            return false;
        }
        this.enemyMap.put(player.getUniqueId(), enemy.getUniqueId());
        this.timerMap.put(player.getUniqueId(), System.currentTimeMillis() + 5 * 1000L);
        this.timerMap.put(enemy.getUniqueId(), System.currentTimeMillis() + 5 * 1000L);
        return true;
    }

    @Override
    public boolean removeFromCombat(Player player) {
        if (player == null) {
            plugin.getLogger().warning("removeFromCombat: Player = null");
            return false;
        }
        if (!isInCombat(player)) {
            plugin.getLogger().info("removeFromCombat: Not in combat! UUID: " +
                    player.getUniqueId() + ", name: " + player.getName());
            return false;
        }
        this.timerMap.remove(player.getUniqueId());
        this.enemyMap.remove(player.getUniqueId());
        return true;
    }

    @Override
    public long getTime(Player player) {
        if (player == null) {
            plugin.getLogger().warning("getTime: Player = null");
            return -1L;
        }
        return timerMap.get(player.getUniqueId()) - System.currentTimeMillis();
    }

    @Override
    public boolean setTime(Player player, long time) {
        if (player == null) {
            plugin.getLogger().warning("setTime: Player = null");
            return false;
        }
        if (!isInCombat(player)) {
            plugin.getLogger().warning("setTime: player is not in combat! UUID: " +
                    player.getUniqueId() + ", name: " + player.getName());
            return false;
        }
        timerMap.put(player.getUniqueId(), time);
        return true;
    }

    @Override
    public Set<UUID> getCombatPlayers() {
        return timerMap.keySet();
    }
}
