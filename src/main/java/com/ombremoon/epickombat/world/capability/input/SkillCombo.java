package com.ombremoon.epickombat.world.capability.input;

import com.ombremoon.epickombat.skill.special.SpecialSkill;
import yesman.epicfight.skill.Skill;

import java.util.List;

public record SkillCombo(String name, Skill skill, List<Input> inputs) {
}
