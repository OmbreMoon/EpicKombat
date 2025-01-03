package com.ombremoon.epickombat.skill.guard;

import com.google.common.collect.Maps;
import com.ombremoon.epickombat.client.KeyBinds;
import com.ombremoon.epickombat.skill.EpicKombatDataKeys;
import com.ombremoon.epickombat.util.KombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class HandGuardSkill extends Skill {
    protected static final UUID EVENT_UUID = UUID.fromString("77117fec-a57f-4f86-9538-7c1596819b47");

    public static class Builder extends Skill.Builder<HandGuardSkill> {
        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardMotions = Maps.newHashMap();
        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> advancedGuardMotions = Maps.newHashMap();
        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardBreakMotions = Maps.newHashMap();

        public Builder setCategory(SkillCategory category) {
            this.category = category;
            return this;
        }

        public Builder setActivateType(ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public Builder setResource(Resource resource) {
            this.resource = resource;
            return this;
        }

        public Builder setCreativeTab(CreativeModeTab tab) {
            this.tab = tab;
            return this;
        }

        public Builder addGuardMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation> function) {
            this.guardMotions.put(weaponCategory, function);
            return this;
        }

        public Builder addAdvancedGuardMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?> function) {
            this.advancedGuardMotions.put(weaponCategory, function);
            return this;
        }

        public Builder addGuardBreakMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation> function) {
            this.guardBreakMotions.put(weaponCategory, function);
            return this;
        }
    }

    public static HandGuardSkill.Builder createGuardBuilder() {
        return (new HandGuardSkill.Builder())
                .setCategory(SkillCategories.GUARD)
                .setActivateType(ActivateType.ONE_SHOT)
                .setResource(Resource.STAMINA);
    }

    public HandGuardSkill(Builder builder) {
        super(builder);
    }

    protected float penalizer;

    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        this.penalizer = parameters.getFloat("penalizer");
    }

    @Override
    public void onInitiate(SkillContainer container) {
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_DAMAGE, EVENT_UUID, event -> {
            container.getDataManager().setDataSync(EpicKombatDataKeys.PENALTY.get(), 0.0F, event.getPlayerPatch().getOriginal());
        });

        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (KombatUtil.hasFighterWeapon(event.getPlayerPatch().getOriginal())/* && KeyBinds.GUARD_BINDING.isDown()*/) {
                LocalPlayer player = event.getPlayerPatch().getOriginal();
                player.setSprinting(false);
                player.sprintTriggerTime = -1;
                Minecraft minecraft = Minecraft.getInstance();
                ControllEngine.setKeyBind(minecraft.options.keySprint, false);
            }
        });
    }
}
