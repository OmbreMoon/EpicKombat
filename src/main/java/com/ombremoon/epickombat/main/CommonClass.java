package com.ombremoon.epickombat.main;

import com.ombremoon.epickombat.init.EpicKombatItems;
import com.ombremoon.epickombat.skill.EpicKombatDataKeys;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLLoader;

public class CommonClass {

    public static void init(IEventBus modEventBus) {
        EpicKombatItems.register(modEventBus);
        EpicKombatDataKeys.register(modEventBus);
    }

    public static boolean inDevEnv() {
        return !FMLLoader.isProduction();
    }

    public static ResourceLocation customLocation(String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }
}
