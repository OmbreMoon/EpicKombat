package com.ombremoon.epickombat.util;

import com.ombremoon.epickombat.world.capability.Fighter;
import com.ombremoon.epickombat.world.capability.KombatKapability;
import com.ombremoon.epickombat.world.capability.KombatKapabilityProvider;
import net.minecraft.world.entity.player.Player;

public class KombatUtil {

    public static KombatKapability getKombat(Player player) {
        return KombatKapabilityProvider.get(player);
    }

    public static boolean hasFighterWeapon(Player player) {
        Fighter fighter = getKombat(player).getFighter();
        return (fighter.requiresWeaponToFight() && player.getMainHandItem().is(fighter.getRequiredWeapon())) || (!fighter.requiresWeaponToFight() && player.getMainHandItem().isEmpty());
    }

    public static Fighter getFighter(Player player) {
        return getKombat(player).getFighter();
    }
}
