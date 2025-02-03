package com.ombremoon.epickombat.world.capability;

import com.ombremoon.epickombat.gameasset.EpicKombatAnimations;
import com.ombremoon.epickombat.gameasset.EpicKombatSkills;
import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.world.capability.input.Combo;
import com.ombremoon.epickombat.world.capability.input.Input;
import com.ombremoon.epickombat.world.capability.input.SkillCombo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;

import java.util.List;

public class FighterPresets {
    public static final Fighter.Builder LIU_KANG =
            Fighter.builder(CommonClass.customLocation("liu_kang"));

    public static final Fighter.Builder SUB_ZERO =
            Fighter.builder(CommonClass.customLocation("sub_zero"));

    public static final Fighter.Builder SCORPION =
            Fighter.builder(CommonClass.customLocation("scorpion"));

    public static final Fighter.Builder RAIDEN =
            Fighter.builder(CommonClass.customLocation("raiden"))
                    /*Necessary Info
                     * Block Motions
                     */
                    .addLivingMotion(LivingMotions.IDLE, () -> EpicKombatAnimations.RAIDEN_IDLE)
                    .addLivingMotion(LivingMotions.WALK, () -> Animations.BIPED_HOLD_DUAL_WEAPON)
                    .addBasicCombo(Input.FP,
                            new Combo("electric_trips", () -> EpicKombatAnimations.ELECTRIC_TIPS),
                            new Combo("warrior_stance", () -> EpicKombatAnimations.WARRIOR_STANCE, List.of(Input.BP)),
                            new Combo("arctic_hammer", () -> EpicKombatAnimations.RUSHING_TEMPO3, List.of(Input.BP, Input.BK)))
                    .addSpecialCombo(Input.A,
                            new SkillCombo("test", EpicKombatSkills.TEST_SPECIAL, List.of(Input.D, Input.A)),
                            new SkillCombo("test1", EpicKombatSkills.TEST_SPECIAL1, List.of(Input.S, Input.FP)))
                    .addBasicCombo(Input.BP,
                            new Combo("quick_chill", () -> EpicKombatAnimations.UCHIGATANA_AUTO1),
                            new Combo("blistering_blizzard", () -> EpicKombatAnimations.UCHIGATANA_AUTO2, List.of(Input.FP)),
                            new Combo("whiteout", () -> EpicKombatAnimations.UCHIGATANA_AUTO3, List.of(Input.FP, Input.BP)))
                    .tauntMotions(() -> Animations.BATTOJUTSU, () -> Animations.METEOR_SLAM);

    public static final Fighter.Builder JOHNNY_CAGE =
            Fighter.builder(CommonClass.customLocation("johnny_cage"));

    public static final Fighter.Builder KITANA =
            Fighter.builder(CommonClass.customLocation("kitana"));
}
