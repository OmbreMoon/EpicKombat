package com.ombremoon.epickombat.world.capability;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.ombremoon.epickombat.api.forgeevent.FighterPresetRegistryEvent;
import com.ombremoon.epickombat.main.CommonClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModLoader;

import java.util.Map;

public class FighterReloadListener extends SimpleJsonResourceReloadListener {
    public static void registerDefaultFighters() {
        Map<ResourceLocation, Fighter.Builder> map = Maps.newHashMap();
        map.put(CommonClass.customLocation("liu_kang"), FighterPresets.LIU_KANG);
        map.put(CommonClass.customLocation("scorpion"), FighterPresets.SCORPION);
        map.put(CommonClass.customLocation("sub_zero"), FighterPresets.SUB_ZERO);

        FighterPresetRegistryEvent event = new FighterPresetRegistryEvent(map);
        ModLoader.get().postEvent(event);
        PRESETS.putAll(event.getFighterMap());
    }

    public static final String DIRECTORY = "capabilities/fighters";

    private static final Gson GSON = (new GsonBuilder()).create();
    private static final Map<ResourceLocation, Fighter.Builder> PRESETS = Maps.newHashMap();
    private static final Map<ResourceLocation, CompoundTag> TAGMAP = Maps.newHashMap();

    public FighterReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {

    }

    public static Fighter.Builder getOrThrow(ResourceLocation fighterName) {
        if (!PRESETS.containsKey(fighterName)) {
            throw new IllegalArgumentException("Can't find fighter: " + fighterName);
        }

        return PRESETS.get(fighterName);
    }

    public static Fighter.Builder getOrThrow(String fighterName) {
        return getOrThrow(ResourceLocation.tryParse(fighterName));
    }

    public static Fighter.Builder get(ResourceLocation fighterName) {
        return PRESETS.get(fighterName);
    }

    public static Fighter.Builder get(String fighterName) {
        return get(ResourceLocation.tryParse(fighterName));
    }
}
