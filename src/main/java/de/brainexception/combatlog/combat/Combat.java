package de.brainexception.combatlog.combat;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public interface Combat {

    boolean isInCombat(Player player);

    boolean setInCombat(Player player, Player enemy);

    boolean removeFromCombat(Player player);

    long getTime(Player player);

    boolean setTime(Player player, long time);

    Set<UUID> getCombatPlayers();

}
