package com.ombremoon.epickombat.world.capability;

import com.ombremoon.epickombat.gameasset.EpicKombatSkills;
import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.world.capability.input.Combo;
import com.ombremoon.epickombat.world.capability.input.Input;
import com.ombremoon.epickombat.world.capability.input.SkillCombo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;

import java.util.List;

@SuppressWarnings("unchecked")
public class FighterPresets {
    public static final Fighter.Builder LIU_KANG =
            Fighter.builder(CommonClass.customLocation("liu_kang"));

    public static final Fighter.Builder SCORPION =
            Fighter.builder(CommonClass.customLocation("scorpion"));

    public static final Fighter.Builder SUB_ZERO =
            Fighter.builder(CommonClass.customLocation("sub_zero"))
                    /*Necessary Info
                    * Block Motions
                    */
                    .addLivingMotion(LivingMotions.IDLE, () -> Animations.BIPED_HOLD_GREATSWORD)
                    .addLivingMotion(LivingMotions.WALK, () -> Animations.BIPED_HOLD_DUAL_WEAPON)
                    .addSpecialCombo(Input.A,
                            new SkillCombo("test", EpicKombatSkills.TEST_SPECIAL, List.of(Input.D, Input.A)),
                            new SkillCombo("test1", EpicKombatSkills.TEST_SPECIAL1, List.of(Input.S, Input.FP)))
                    .addBasicCombo(Input.FP,
                            new Combo("clammy_palm", () -> Animations.RUSHING_TEMPO1),
                            new Combo("lin_kuei_storm", () -> Animations.RUSHING_TEMPO2, List.of(Input.BP)),
                            new Combo("arctic_hammer", () -> Animations.DAGGER_AUTO2, List.of(Input.BP, Input.BP)))
                    .tauntMotions(() -> Animations.BATTOJUTSU, () -> Animations.METEOR_SLAM);
}
