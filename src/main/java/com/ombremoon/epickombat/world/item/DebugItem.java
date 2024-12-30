package com.ombremoon.epickombat.world.item;

import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.util.KombatUtil;
import com.ombremoon.epickombat.world.capability.KombatKapability;
import com.ombremoon.epickombat.world.capability.input.Input;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class DebugItem extends Item {
    public DebugItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(pPlayer, PlayerPatch.class);
        KombatKapability kombat = KombatUtil.getKombat(pPlayer);
//        playerPatch.getSkillCapability().skillContainers[KombatSlots.FIGHTER.universalOrdinal()].setSkill(EpicKombatSkills.LIU_KANG);
//        Constants.LOG.debug("{}", Input.JUMP.append(Input.S, Input.FK));
//        Constants.LOG.debug("{}", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN));
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
