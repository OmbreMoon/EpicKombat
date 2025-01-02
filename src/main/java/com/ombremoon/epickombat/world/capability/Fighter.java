package com.ombremoon.epickombat.world.capability;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.util.KombatUtil;
import com.ombremoon.epickombat.world.capability.input.Combo;
import com.ombremoon.epickombat.world.capability.input.Input;
import com.ombremoon.epickombat.world.capability.input.SkillCombo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Fighter {
    public static Fighter EMPTY = Fighter.builder(CommonClass.customLocation("empty")).build();
    protected final ResourceLocation name;
    protected final Map<Attribute, AttributeModifier> attributeMap;
    protected final Map<String, List<Combo>> basicAttackCombos;
    protected final Map<String, List<SkillCombo>> specialCombos;
    protected final Map<LivingMotion, Supplier<StaticAnimation>> livingMotions;
    protected final List<Supplier<StaticAnimation>> taunts;
    @Nullable
    private final Item requiredWeapon;

    protected Fighter(Builder builder) {
        this.name = builder.name;
        ImmutableMap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMap.builder();

        for (Map.Entry<Attribute, AttributeModifier> entry : builder.attributeMap.entrySet()) {
            mapBuilder.put(entry.getKey(), entry.getValue());
        }
        this.attributeMap = mapBuilder.build();
        this.basicAttackCombos = builder.basicComboMap;
        this.specialCombos = builder.specialComboMap;
        this.livingMotions = builder.livingMotions;
        this.requiredWeapon = builder.requiredWeapon;
        this.taunts = builder.tauntMotions;
    }

    public final List<Combo> getBasicCombo(Input input) {
        return this.basicAttackCombos.getOrDefault(input.getInput(), Lists.newArrayList());
    }

    public final List<SkillCombo> getSpecialCombo(Input input) {
        return this.specialCombos.getOrDefault(input.getInput(), Lists.newArrayList());
    }

    public Map<LivingMotion, AnimationProvider<?>> getLivingMotions(Player player) {
        if (!KombatUtil.hasFighterWeapon(player))
            return Maps.newHashMap();

        Map<LivingMotion, AnimationProvider<?>> motions = Maps.newHashMap();
        this.livingMotions.forEach((livingMotion, animation) -> motions.put(livingMotion, animation.get()));

        return motions;
    }

    public boolean requiresWeaponToFight() {
        return this.requiredWeapon != null;
    }

    @Nullable
    public Item getRequiredWeapon() {
        return this.requiredWeapon;
    }

    public StaticAnimation getTaunt(Player player) {
        int i = player.getRandom().nextInt(this.taunts.size());
        return this.taunts.get(i).get();
    }

    @Override
    public String toString() {
        return this.name.toString();
    }

    public static Builder builder(ResourceLocation name) {
        return new Builder(name);
    }

    public static class Builder {
        ResourceLocation name;
        Function<Builder, Fighter> constructor;
        Map<Attribute, AttributeModifier> attributeMap;
        List<AnimationProvider<?>> blockMotions;
        Map<String, List<Combo>> basicComboMap;
        Map<String, List<SkillCombo>> specialComboMap;
        Map<LivingMotion, Supplier<StaticAnimation>> livingMotions;
        List<Supplier<StaticAnimation>> tauntMotions;
        Item requiredWeapon;

        protected Builder(ResourceLocation name) {
            this.name = name;
            this.constructor = Fighter::new;
            this.attributeMap = Maps.newHashMap();
            this.blockMotions = Lists.newArrayList();
            this.basicComboMap = Maps.newHashMap();
            this.specialComboMap = Maps.newHashMap();
            this.livingMotions = Maps.newHashMap();
            this.tauntMotions = Lists.newArrayList();
            this.requiredWeapon = null;
        }

        public Builder constructor(Function<Builder, Fighter> constructor) {
            this.constructor = constructor;
            return this;
        }

        public Builder addFighterAttributes(Attribute attribute, AttributeModifier modifier) {
            this.attributeMap.put(attribute, modifier);

            return this;
        }

        public Builder blockMotions(StaticAnimation... animation) {
            this.blockMotions = Lists.newArrayList(animation);
            return this;
        }

        public Builder addBasicCombo(Input start, Combo... combos) {
            this.basicComboMap.put(start.getInput(), Lists.newArrayList(combos));
            return this;
        }

        public Builder addSpecialCombo(Input start, SkillCombo... combos) {
            this.specialComboMap.put(start.getInput(), Lists.newArrayList(combos));
            return this;
        }

        public Builder addLivingMotion(LivingMotion livingMotion, Supplier<StaticAnimation> staticAnimation) {
            this.livingMotions.put(livingMotion, staticAnimation);
            return this;
        }

        @SafeVarargs
        public final Builder tauntMotions(Supplier<StaticAnimation>... animation) {
            this.tauntMotions = Lists.newArrayList(animation);
            return this;
        }

        public Builder requiredWeapon(Item weapon) {
            this.requiredWeapon = weapon;
            return this;
        }

        public final Fighter build() {
            return this.constructor.apply(this);
        }
    }
}
