package com.ombremoon.epickombat.api.forgeevent;

import com.ombremoon.epickombat.world.capability.Fighter;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.Map;

public class FighterPresetRegistryEvent extends Event implements IModBusEvent {
    private final Map<ResourceLocation, Fighter.Builder> fighterMap;

    public FighterPresetRegistryEvent(Map<ResourceLocation, Fighter.Builder> fighterMap) {
        this.fighterMap = fighterMap;
    }

    public Map<ResourceLocation, Fighter.Builder> getFighterMap() {
        return this.fighterMap;
    }
}
