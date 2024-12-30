package com.ombremoon.epickombat.main;

import com.ombremoon.epickombat.config.ConfigHandler;
import com.ombremoon.epickombat.init.EpicKombatItems;
import com.ombremoon.epickombat.networking.NetworkManager;
import com.ombremoon.epickombat.skill.KombatCategories;
import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.world.capability.FighterInfoReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.main.EpicFightExtensions;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

@Mod(Constants.MOD_ID)
public class EpicKombat {

    public EpicKombat() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        SkillCategory.ENUM_MANAGER.registerEnumCls(Constants.MOD_ID, KombatCategories.class);
        SkillSlot.ENUM_MANAGER.registerEnumCls(Constants.MOD_ID, KombatSlots.class);

        CommonClass.init(modEventBus);
        ModLoadingContext.get().registerExtensionPoint(EpicFightExtensions.class, () -> new EpicFightExtensions(EpicKombatItems.TAB.get()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG, "epickombat-client.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(FighterInfoReloadListener::registerDefaultFighters);
        event.enqueueWork(NetworkManager::registerPackets);
    }
}
