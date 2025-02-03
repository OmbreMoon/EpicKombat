package com.ombremoon.epickombat.client;

import com.ombremoon.epickombat.client.renderer.layer.LightningLayer;
import com.ombremoon.epickombat.client.renderer.patched.layer.PatchedLightningLayer;
import com.ombremoon.epickombat.main.Constants;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.FRONT_PUNCH_BINDING);
            event.register(KeyBinds.BACK_PUNCH_BINDING);
            event.register(KeyBinds.FRONT_KICK_BINDING);
            event.register(KeyBinds.BACK_KICK_BINDING);
            event.register(KeyBinds.GUARD_BINDING);
        }

        @SubscribeEvent
        public static void registerEntityLayers(EntityRenderersEvent.AddLayers event) {
            for (final var skin : event.getSkins()) {
                final LivingEntityRenderer<Player, PlayerModel<Player>> playerRenderer = event.getSkin(skin);
                if (playerRenderer == null)
                    continue;

                playerRenderer.addLayer(new LightningLayer<>(playerRenderer));
            }
        }

        @SubscribeEvent
        public static void onModifyPatchEntityRender(PatchedRenderersEvent.Modify event) {
            ((PatchedLivingEntityRenderer)event.get(EntityType.PLAYER)).addPatchedLayer(LightningLayer.class, new PatchedLightningLayer());
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

    }
}
