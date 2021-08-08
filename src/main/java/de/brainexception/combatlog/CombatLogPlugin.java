package de.brainexception.combatlog;

import de.brainexception.combatlog.combat.CombatManager;
import de.brainexception.combatlog.combat.CombatRunnable;
import de.brainexception.combatlog.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatLogPlugin extends JavaPlugin {

    private CombatManager combatManager;
    private CombatRunnable combatRunnable;

    @Override
    public void onEnable() {
        getLogger().info("onEnabled called!");
        this.combatManager = new CombatManager(this);
        this.combatRunnable = new CombatRunnable(this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable called");
        this.combatRunnable.cancelTask();
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

}
