package com.ombremoon.epickombat.mixin;

import com.ombremoon.epickombat.gameasset.EpicKombatSkills;
import com.ombremoon.epickombat.skill.KombatSlots;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.server.SPDatapackSyncSkill;
import yesman.epicfight.skill.SkillSlots;

@Mixin(value = SkillManager.class, remap = false)
public class SkillManagerMixin {

    @Inject(method = "processServerPacket(Lyesman/epicfight/network/server/SPDatapackSyncSkill;)V", at = @At("TAIL"))
    private static void processServerPacket(SPDatapackSyncSkill packet, CallbackInfo info) {
        LocalPlayerPatch localplayerpatch = ClientEngine.getInstance().getPlayerPatch();
        if (localplayerpatch != null) {
            if (localplayerpatch.getSkill(KombatSlots.FIGHTER) != null)
                localplayerpatch.getSkill(KombatSlots.BASIC).setSkill(EpicKombatSkills.COMBO_BASIC_ATTACK);
        }
    }
}
