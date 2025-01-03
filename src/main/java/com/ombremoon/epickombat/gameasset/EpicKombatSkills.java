package com.ombremoon.epickombat.gameasset;

import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.skill.ComboBasicAttack;
import com.ombremoon.epickombat.skill.fighter.FighterSkill;
import com.ombremoon.epickombat.skill.fighter.LiuKangSkill;
import com.ombremoon.epickombat.skill.fighter.ScorpionSkill;
import com.ombremoon.epickombat.skill.fighter.SubZeroSkill;
import com.ombremoon.epickombat.skill.guard.HandGuardSkill;
import com.ombremoon.epickombat.skill.special.SimpleSpecialSkill;
import com.ombremoon.epickombat.skill.special.SpecialSkill;
import com.ombremoon.epickombat.skill.special.TestSpecial;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EpicKombatSkills {
    public static Skill COMBO_BASIC_ATTACK;
    public static Skill HAND_GUARD;
    public static Skill LIU_KANG;
    public static Skill SUB_ZERO;
    public static Skill SCORPION;
    public static Skill TEST_SPECIAL;
    public static Skill TEST_SPECIAL1;

    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent event) {
        SkillBuildEvent.ModRegistryWorker registry = event.createRegistryWorker(Constants.MOD_ID);

        COMBO_BASIC_ATTACK = registry.build("combo_basic_attack", ComboBasicAttack::new, ComboBasicAttack.createComboBasicAttackBuilder());
        HAND_GUARD = registry.build("hand_guard", HandGuardSkill::new, HandGuardSkill.createGuardBuilder());

        LIU_KANG = registry.build("liu_kang", LiuKangSkill::new, FighterSkill.createFighterBuilder().setFighter(CommonClass.customLocation("liu_kang")));
        SUB_ZERO = registry.build("sub_zero", SubZeroSkill::new, FighterSkill.createFighterBuilder().setFighter(CommonClass.customLocation("sub_zero")));
        SCORPION = registry.build("scorpion", ScorpionSkill::new, FighterSkill.createFighterBuilder().setFighter(CommonClass.customLocation("scorpion")));

        SpecialSkill test = registry.build("test", TestSpecial::new, TestSpecial.createSimpleSpecialBuilder().setAnimations(() -> (AttackAnimation) Animations.THE_GUILLOTINE));
        test.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(1))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE));
        TEST_SPECIAL = test;

        SpecialSkill test1 = registry.build("test1", SimpleSpecialSkill::new, SimpleSpecialSkill.createSimpleSpecialBuilder().setAnimations(() -> (AttackAnimation)Animations.SHARP_STAB));
        test1.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.4F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE, EpicFightDamageType.GUARD_PUNCTURE));
        TEST_SPECIAL1 = test1;
    }

    public EpicKombatSkills() {}
}
