package com.ombremoon.epickombat.config;

import com.ombremoon.epickombat.world.capability.input.Input;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
//    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG;

    public static final ForgeConfigSpec.EnumValue<Input.Timing> INPUT_TIMING;
    public static final ForgeConfigSpec.BooleanValue DUAL_CONTROLS;

    public ConfigHandler() {
    }

    static {
        INPUT_TIMING = CLIENT_BUILDER.defineEnum("epickombat.config.input_timing", Input.Timing.LONG);
        DUAL_CONTROLS = CLIENT_BUILDER.define("epickombat.config.dual_controls", false);
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }
}
