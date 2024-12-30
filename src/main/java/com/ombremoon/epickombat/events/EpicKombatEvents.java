package com.ombremoon.epickombat.events;

import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.world.capability.KombatKapabilityProvider;
import com.ombremoon.epickombat.util.KombatUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class EpicKombatEvents {

    @SubscribeEvent
    public static void onAttachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player player) {
            var provider = new KombatKapabilityProvider();
            if (provider.hasCap()) {
                var cap = provider.getCapability(KombatKapabilityProvider.KOMBAT_KAPABILITY).orElse(null);
                cap.init(player);
                event.addCapability(KombatKapabilityProvider.CAPABILITY_LOCATION, provider);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            var cap = KombatUtil.getKombat(player);
            if (!cap.isInitialized()) {
                cap.joinWorld(event);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickStart(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            var cap = KombatUtil.getKombat(player);
            cap.tick();
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            var cap = KombatUtil.getKombat(player);
            cap.die(event);
        }
    }
}
