package com.ombremoon.epickombat.client;

import com.ombremoon.epickombat.main.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.FRONT_PUNCH_BINDING);
//            event.register(KeyBinds.BACK_PUNCH_BINDING);
//            event.register(KeyBinds.FRONT_KICK_BINDING);
//            event.register(KeyBinds.BACK_KICK_BINDING);
//            event.register(KeyBinds.GUARD_BINDING);
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {


    }
}
