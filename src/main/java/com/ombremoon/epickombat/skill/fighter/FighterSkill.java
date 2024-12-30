package com.ombremoon.epickombat.skill.fighter;

import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.skill.KombatCategories;
import com.ombremoon.epickombat.util.KombatUtil;
import com.ombremoon.epickombat.world.capability.FighterInfo;
import com.ombremoon.epickombat.world.capability.FighterInfoReloadListener;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;

public abstract class FighterSkill extends Skill {
    private final ResourceLocation fighterName;

    public static Builder createFighterBuilder() {
        return (new FighterSkill.Builder()).setCategory(KombatCategories.FIGHTER).setResource(Resource.NONE);
    }

    public FighterSkill(Builder builder) {
        super(builder);
        this.fighterName = builder.fighterName;
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        var kombat = KombatUtil.getKombat(container.getExecuter().getOriginal());
        kombat.setFighterInfo(this.getFighterInfo());
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        var kombat = KombatUtil.getKombat(container.getExecuter().getOriginal());
        kombat.setFighterInfo(FighterInfo.EMPTY);
    }

    @Override
    public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y) {
        super.drawOnGui(gui, container, guiGraphics, x, y);
    }

    public FighterInfo getFighterInfo() {
        return FighterInfoReloadListener.getOrThrow(this.fighterName).build();
    }

    public static class Builder extends Skill.Builder<FighterSkill> {
        protected ResourceLocation fighterName;

        public Builder() {
            this.fighterName = CommonClass.customLocation("");
        }

        public Builder setFighterInfo(ResourceLocation fighterName) {
            this.fighterName = fighterName;
            return this;
        }

        public Builder setRegistryName(ResourceLocation registryName) {
            this.registryName = registryName;
            return this;
        }

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
    }
}
