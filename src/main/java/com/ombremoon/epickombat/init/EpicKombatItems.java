package com.ombremoon.epickombat.init;

import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.world.item.DebugItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EpicKombatItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static RegistryObject<DebugItem> DEBUG;

    public static RegistryObject<CreativeModeTab> TAB = TABS.register(Constants.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(DEBUG.get()))
            .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".tab"))
            .displayItems(
                    (itemDisplayParameters, output) -> {
                        ITEMS.getEntries().forEach((registryObject) -> {
                            output.accept(new ItemStack(registryObject.get()));
                        });
                    })
            .build());;

    public static Item.Properties itemProperties() {
        return new Item.Properties();
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        if (CommonClass.inDevEnv())
            DEBUG = ITEMS.register("debug", () -> new DebugItem(itemProperties()));
        TABS.register(modEventBus);
    }
}
