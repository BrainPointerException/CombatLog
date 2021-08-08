package de.brainexception.combatlog.listener;

import de.brainexception.combatlog.CombatLogPlugin;
import de.brainexception.combatlog.combat.Combat;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerListener implements Listener {

    private final CombatLogPlugin plugin;

    public PlayerListener(CombatLogPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Combat combat = plugin.getCombatManager();
        if (combat.isInCombat(player)) {
            plugin.getLogger().info("PlayerQuitEvent: quit during combat! UUID: " +
                    player.getUniqueId() + " name: " + player.getName());
            combat.removeFromCombat(player);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Combat combat = plugin.getCombatManager();
        if (combat.isInCombat(player)) {
            plugin.getLogger().info("PlayerKickEvent: kick during combat! UUID: " +
                    player.getUniqueId() + " name: " + player.getName());
            combat.removeFromCombat(player);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Combat combat = plugin.getCombatManager();
        if (combat.isInCombat(player)) {
            combat.removeFromCombat(player);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getEntity() == null || event.getDamager() == null) {
            return;
        }
        if (event.getEntity().equals(event.getDamager())) {
            return;
        }

        Combat combat = plugin.getCombatManager();
        Player entity = ((Player) event.getEntity()).getPlayer();

        switch (event.getDamager().getType()) {
            case ARROW: {
                Arrow arrow = (Arrow) event.getDamager();
                ProjectileSource shooter = arrow.getShooter();
                if (shooter instanceof Player) {
                    if (shooter.equals(entity)) {
                        break;
                    }
                    if (!combat.setInCombat(entity, (Player) shooter)) {
                        break;
                    }
                    if (!combat.setInCombat((Player) shooter, entity)) {
                        break;
                    }
                }
                break;
            }
            case PLAYER: {
                Player enemy = (Player) event.getDamager();
                if (!combat.setInCombat(entity, enemy)) {
                    break;
                }
                if (!combat.setInCombat(enemy, entity)) {
                    break;
                }
                break;
            }
            default:
                break;
        }

    }

}
