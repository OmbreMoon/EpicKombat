package com.ombremoon.epickombat.world.capability;

import com.ombremoon.epickombat.gameasset.EpicKombatSkills;
import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.world.capability.input.Combo;
import com.ombremoon.epickombat.world.capability.input.Input;
import com.ombremoon.epickombat.world.capability.input.SkillCombo;
import yesman.epicfight.gameasset.Animations;

import java.util.List;

@SuppressWarnings("unchecked")
public class FighterPresets {
    public static final FighterInfo.Builder LIU_KANG =
            FighterInfo.builder(CommonClass.customLocation("liu_kang"));

    public static final FighterInfo.Builder SCORPION =
            FighterInfo.builder(CommonClass.customLocation("scorpion"));

    public static final FighterInfo.Builder SUB_ZERO =
            FighterInfo.builder(CommonClass.customLocation("sub_zero"))
                    /*Necessary Info
                    * Living Motions
                    * Default Combat Motions
                    * Block Motions
                    */
                    .addSpecialCombo(Input.A,
                            new SkillCombo("test", EpicKombatSkills.TEST_SPECIAL, List.of(Input.D, Input.A)),
                            new SkillCombo("test1", EpicKombatSkills.TEST_SPECIAL1, List.of(Input.S, Input.FP)))
                    .addBasicCombo(Input.FP,
                            new Combo("lin_kuei_storm", Animations.DAGGER_AUTO1, List.of(Input.BP)),
                            new Combo("arctic_hammer", Animations.DAGGER_AUTO2, List.of(Input.BP, Input.BP)))
                    .tauntMotions(() -> Animations.AXE_AUTO1, () -> Animations.AXE_AUTO2);
}
