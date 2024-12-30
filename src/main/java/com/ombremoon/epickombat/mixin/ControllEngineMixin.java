package com.ombremoon.epickombat.mixin;

import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.util.KombatUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(value = ControllEngine.class, remap = false)
public abstract class ControllEngineMixin {

    @Shadow private LocalPlayerPatch playerpatch;

    @Shadow private LocalPlayer player;

    @Shadow public Options options;

    @Shadow private KeyMapping currentChargingKey;

    @Inject(method = "Lyesman/epicfight/client/events/engine/ControllEngine;keyPressed(Lnet/minecraft/client/KeyMapping;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z", shift = At.Shift.AFTER), cancellable = true)
    private static void keyPressed(KeyMapping key, boolean eventCheck, CallbackInfoReturnable<Boolean> cir) {
        LocalPlayerPatch playerPatch = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, LocalPlayerPatch.class);

        if (playerPatch.getSkill(KombatSlots.FIGHTER) != null && key == EpicFightKeyMappings.ATTACK && KombatUtil.hasFighterWeapon(playerPatch.getOriginal()))
            cir.setReturnValue(false);
    }

    @Inject(method = "Lyesman/epicfight/client/events/engine/ControllEngine;handleEpicFightKeyMappings()V", at = @At("HEAD"))
    private void handleEpicFightKeyMappings(CallbackInfo ci) {
        if (playerpatch.getSkill(KombatSlots.FIGHTER) != null && playerpatch.isBattleMode() && currentChargingKey != EpicFightKeyMappings.ATTACK) {
            if (options.keyAttack.getKey() == EpicFightKeyMappings.ATTACK.getKey()) {
                ControllEngine.disableKey(options.keyAttack);
            }
        }
    }
}
