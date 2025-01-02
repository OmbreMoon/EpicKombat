package com.ombremoon.epickombat.skill.special;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.skill.KombatCategories;
import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.util.KombatUtil;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpecialSkill extends Skill {
    protected List<Map<AnimationProperty.AttackPhaseProperty<?>, Object>> properties = Lists.newArrayList();

    @SuppressWarnings("unchecked")
    public static Skill.Builder<SpecialSkill> createSpecialSkillBuilder() {
        return (new Skill.Builder()).setCategory(KombatCategories.SPECIAL).setResource(Resource.STAMINA);
    }

    public SpecialSkill(Builder<? extends Skill> builder) {
        super(builder);
    }

    public boolean canExecute(PlayerPatch<?> executer) {
        if (executer.isLogicalClient()) {
            return super.canExecute(executer);
        } else {
            return super.canExecute(executer) && KombatUtil.hasFighterWeapon(executer.getOriginal()) && executer.getSkill(KombatSlots.SPECIAL).getSkill() == this && executer.getOriginal().getVehicle() == null && (!executer.getSkill(this).isActivated() || this.activateType == ActivateType.TOGGLE);
        }
    }

    @SuppressWarnings("unchecked")
    protected <V> Optional<V> getProperty(AnimationProperty.AttackPhaseProperty<V> propertyKey, Map<AnimationProperty.AttackPhaseProperty<?>, Object> map) {
        return (Optional<V>) Optional.ofNullable(map.get(propertyKey));
    }

    public SpecialSkill newProperty() {
        this.properties.add(Maps.newHashMap());
        return this;
    }

    public <T> SpecialSkill addProperty(AnimationProperty.AttackPhaseProperty<T> propertyKey, T object) {
        this.properties.get(this.properties.size() - 1).put(propertyKey, object);
        return this;
    }
}
