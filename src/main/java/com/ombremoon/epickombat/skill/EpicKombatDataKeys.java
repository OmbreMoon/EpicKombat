package com.ombremoon.epickombat.skill;

import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.world.capability.input.Input;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.skill.SkillDataKey;

public class EpicKombatDataKeys {
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(new ResourceLocation("epicfight", "skill_data_keys"), Constants.MOD_ID);

    public static final RegistryObject<SkillDataKey<Integer>> COMBO_COUNTER = DATA_KEYS.register("combo_counter", () -> SkillDataKey.createIntKey(0, false, ComboBasicAttack.class));
    public static final RegistryObject<SkillDataKey<Boolean>> BASIC_ATTACK_ACTIVATE = DATA_KEYS.register("basic_attack_activate", () -> SkillDataKey.createBooleanKey(false, false, ComboBasicAttack.class));
    public static final RegistryObject<SkillDataKey<Input>> FIRST_INPUT = DATA_KEYS.register("first_input", () -> createInputKey(Input.EMPTY, false, ComboBasicAttack.class));

    public static SkillDataKey<Input> createInputKey(Input defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        return SkillDataKey.createSkillDataKey((byteBuf, input) -> {
            input.write(byteBuf);
        }, Input::read, defaultValue, syncronizeTrackingPlayers, skillClass);
    }

    public static void register(IEventBus modEventBus) {
        DATA_KEYS.register(modEventBus);
    }
}
