package de.brainexception.combatlog.combat;

import de.brainexception.combatlog.CombatLogPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class CombatRunnable implements Runnable {

    private final CombatLogPlugin plugin;
    private final BukkitTask task;

    public CombatRunnable(CombatLogPlugin plugin) {
        this.plugin = plugin;
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 20L);
    }

    @Override
    public void run() {
        Combat combat = this.plugin.getCombatManager();
        combat.getCombatPlayers().stream().filter(uuid -> combat.isInCombat(Bukkit.getPlayer(uuid))).forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (combat.getTime(player) <= 0) {
                if (combat.removeFromCombat(player)) {
                    player.sendMessage("Du bist nicht mehr im Kampf!");
                }
            } else {
                if (combat.getTime(player) > 0) {
                    player.sendMessage("Du bist noch für " + ((combat.getTime(player) / 1000) + 1) + "s im Kampf!");
                }
            }
        });
    }

    public void cancelTask() {
        this.task.cancel();
    }

}
