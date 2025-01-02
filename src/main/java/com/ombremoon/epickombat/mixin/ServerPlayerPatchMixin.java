package com.ombremoon.epickombat.mixin;

import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.util.KombatUtil;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Map;

@Mixin(value = ServerPlayerPatch.class, remap = false)
public class ServerPlayerPatchMixin extends PlayerPatch<ServerPlayer> {

    @Inject(method = "modifyLivingMotionByCurrentItem(Z)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;putAll(Ljava/util/Map;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void modifyLivingMotionByCurrentItem(boolean checkOldAnimations, CallbackInfo ci, Map<LivingMotion, StaticAnimation> oldLivingAnimations, Map<LivingMotion, StaticAnimation> newLivingAnimations, CapabilityItem mainhandCap, CapabilityItem offhandCap, Map<LivingMotion, AnimationProvider<?>> livingMotionModifiers) {
        if (getSkill(KombatSlots.FIGHTER) != null && KombatUtil.hasFighterWeapon(getOriginal())) {
            livingMotionModifiers.clear();
            livingMotionModifiers.putAll(KombatUtil.getKombat(getOriginal()).getFighter().getLivingMotions(getOriginal()));
        }
    }

    @Override
    public void updateMotion(boolean b) {

    }
}
