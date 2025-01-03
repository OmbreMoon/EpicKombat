package com.ombremoon.epickombat.skill;

import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

public enum KombatSlots implements SkillSlot {
    FIGHTER(KombatCategories.FIGHTER),
    BASIC(KombatCategories.BASIC),
    SPECIAL(KombatCategories.SPECIAL),
    FINISHER(KombatCategories.FINISHER),
    HAND_GUARD(KombatCategories.HAND_GUARD);

    final SkillCategory category;
    final int id;

    KombatSlots(SkillCategory category) {
        this.category = category;
        this.id = SkillSlot.ENUM_MANAGER.assign(this);
    }

    @Override
    public SkillCategory category() {
        return this.category;
    }

    @Override
    public int universalOrdinal() {
        return this.id;
    }
}
