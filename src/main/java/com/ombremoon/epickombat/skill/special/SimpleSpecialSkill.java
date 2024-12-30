package com.ombremoon.epickombat.skill.special;

import com.ombremoon.epickombat.skill.KombatCategories;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.api.animation.AttackAnimationProvider;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class SimpleSpecialSkill extends SpecialSkill {
    protected AttackAnimationProvider attackAnimation;

    public static SimpleSpecialSkill.Builder createSimpleSpecialBuilder() {
        return (new SimpleSpecialSkill.Builder()).setCategory(KombatCategories.SPECIAL).setResource(Resource.STAMINA);
    }

    public SimpleSpecialSkill(SimpleSpecialSkill.Builder builder) {
        super(builder);
        this.attackAnimation = builder.attackAnimation;
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        executer.playAnimationSynchronized(this.attackAnimation.get(), 0.0F);
        super.executeOnServer(executer, args);
    }

    @Override
    public SpecialSkill registerPropertiesToAnimation() {
        AttackAnimation anim = this.attackAnimation.get();

        for (AttackAnimation.Phase phase : anim.phases) {
            phase.addProperties(this.properties.get(0).entrySet());
        }

        return this;
    }

    public static class Builder extends Skill.Builder<SimpleSpecialSkill> {
        protected AttackAnimationProvider attackAnimation;

        public Builder() {
        }

        public Builder setCategory(SkillCategory category) {
            this.category = category;
            return this;
        }

        public Builder setActivateType(Skill.ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public Builder setResource(Skill.Resource resource) {
            this.resource = resource;
            return this;
        }

        public Builder setAnimations(AttackAnimationProvider attackAnimation) {
            this.attackAnimation = attackAnimation;
            return this;
        }
    }
}
