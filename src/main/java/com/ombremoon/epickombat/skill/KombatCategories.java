package com.ombremoon.epickombat.skill;

import yesman.epicfight.skill.SkillCategory;

public enum KombatCategories implements SkillCategory {
    FIGHTER(true, true, true),
    BASIC(false, false, false),
    SPECIAL(false, false, false),
    FINISHER(false, false, false),
    TAUNT(true, false, false);

    final boolean shouldSave;
    final boolean shouldSynchronize;
    final boolean modifiable;
    final int id;

    KombatCategories(boolean shouldSave, boolean shouldSynchronize, boolean modifiable) {
        this.shouldSave = shouldSave;
        this.shouldSynchronize = shouldSynchronize;
        this.modifiable = modifiable;
        this.id = SkillCategory.ENUM_MANAGER.assign(this);
    }

    @Override
    public boolean shouldSave() {
        return this.shouldSave;
    }

    @Override
    public boolean shouldSynchronize() {
        return this.shouldSynchronize;
    }

    @Override
    public boolean learnable() {
        return this.modifiable;
    }

    @Override
    public int universalOrdinal() {
        return this.id;
    }
}
