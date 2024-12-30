package com.ombremoon.epickombat.util;

import com.ombremoon.epickombat.world.capability.FighterInfo;
import com.ombremoon.epickombat.world.capability.KombatKapability;
import com.ombremoon.epickombat.world.capability.KombatKapabilityProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class KombatUtil {

    public static KombatKapability getKombat(Player player) {
        return KombatKapabilityProvider.get(player);
    }

    public static boolean hasFighterWeapon(Player player) {
        FighterInfo fighter = getKombat(player).getFighterInfo();
        return (fighter.requiresWeaponToFight() && player.getMainHandItem().is(fighter.getRequiredWeapon())) || (!fighter.requiresWeaponToFight() && player.getMainHandItem().isEmpty());
    }

    public static FighterInfo getFighter(Player player) {
        return getKombat(player).getFighterInfo();
    }
}
