package com.ombremoon.epickombat.gameasset;

import com.ombremoon.epickombat.api.animation.types.ComboAttackAnimation;
import com.ombremoon.epickombat.api.animation.types.ComboBasicAttackAnimation;
import com.ombremoon.epickombat.main.Constants;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EpicKombatAnimations {
    public static StaticAnimation RAIDEN_IDLE;
    public static StaticAnimation ELECTRIC_TIPS;
    public static StaticAnimation WARRIOR_STANCE;
    public static StaticAnimation RUSHING_TEMPO1;
    public static StaticAnimation RUSHING_TEMPO2;
    public static StaticAnimation RUSHING_TEMPO3;
    public static StaticAnimation UCHIGATANA_AUTO1;
    public static StaticAnimation UCHIGATANA_AUTO2;
    public static StaticAnimation UCHIGATANA_AUTO3;

    @SubscribeEvent
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(Constants.MOD_ID, EpicKombatAnimations::build);
    }

    private static void build() {
        HumanoidArmature biped = Armatures.BIPED;

        RAIDEN_IDLE = new StaticAnimation(true, "biped/living/raiden_idle", biped);
        ELECTRIC_TIPS = new BasicAttackAnimation(0.05F, 0.15F, 0.25F, 0.3F, null, biped.toolR, "biped/combat/electric_tips", biped)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 3.6F);
        WARRIOR_STANCE = new BasicAttackAnimation(0.05F, 0.15F, 0.25F, 0.3F, null, biped.toolR, "biped/combat/warrior_stance", biped)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 3.6F);
        RUSHING_TEMPO1 = new BasicAttackAnimation(0.05F, 0.0F, 0.15F, 0.25F, 0.6F, null, biped.toolR, "biped/skill/rushing_tempo1", biped)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F)
                .addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
                .addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, false)
                .newTimePair(0.0F, 0.25F);
        RUSHING_TEMPO2 = new BasicAttackAnimation(0.05F, 0.0F, 0.15F, 0.25F, 0.6F, null, biped.toolR, "biped/skill/rushing_tempo2", biped)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F)
                .addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
                .addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, false)
                .newTimePair(0.0F, 0.25F);
        RUSHING_TEMPO3 = new BasicAttackAnimation(0.05F, 0.0F, 0.2F, 0.25F, 0.6F, null, biped.toolR, "biped/skill/rushing_tempo3", biped)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F)
                .addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
                .addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, false)
                .newTimePair(0.0F, 0.25F);
        UCHIGATANA_AUTO1 = (new BasicAttackAnimation(0.05F, 0.15F, 0.25F, 0.3F, (Collider)null, biped.toolR, "biped/combat/uchigatana_auto1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F);
        UCHIGATANA_AUTO2 = (new BasicAttackAnimation(0.05F, 0.2F, 0.3F, 0.3F, (Collider)null, biped.toolR, "biped/combat/uchigatana_auto2", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F);
        UCHIGATANA_AUTO3 = (new BasicAttackAnimation(0.1F, 0.15F, 0.25F, 0.5F, (Collider)null, biped.toolR, "biped/combat/uchigatana_auto3", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F);
    }
}
