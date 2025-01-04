package com.ombremoon.epickombat.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final String KEY_CATEGORY_EK = "key.category.epickombat";
    public static final String KEY_FRONT_PUNCH = "key.epickombat.front_punch";
    public static final String KEY_BACK_PUNCH = "key.epickombat.back_punch";
    public static final String KEY_FRONT_KICK = "key.epickombat.front_kick";
    public static final String KEY_BACK_KICK = "key.epickombat.back_kick";
    public static final String KEY_GUARD = "key.epickombat.guard";

    public static final KeyMapping FRONT_PUNCH_BINDING = new KeyMapping(KEY_FRONT_PUNCH, KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE, 0, KEY_CATEGORY_EK);

    public static final KeyMapping BACK_PUNCH_BINDING = new KeyMapping(KEY_BACK_PUNCH, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, KEY_CATEGORY_EK);

    public static final KeyMapping FRONT_KICK_BINDING = new KeyMapping(KEY_FRONT_KICK, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_EK);

    public static final KeyMapping BACK_KICK_BINDING = new KeyMapping(KEY_BACK_KICK, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_EK);

    public static final KeyMapping GUARD_BINDING = new KeyMapping(KEY_GUARD, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_EK);

}
