package com.ombremoon.epickombat.api.forgeevent;

import com.ombremoon.epickombat.world.capability.FighterInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.Map;

public class FighterPresetRegistryEvent extends Event implements IModBusEvent {
    private final Map<ResourceLocation, FighterInfo.Builder> fighterMap;

    public FighterPresetRegistryEvent(Map<ResourceLocation, FighterInfo.Builder> fighterMap) {
        this.fighterMap = fighterMap;
    }

    public Map<ResourceLocation, FighterInfo.Builder> getFighterMap() {
        return this.fighterMap;
    }
}
