package com.ombremoon.epickombat.api.animation.types;

import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;

public class ComboAttackAnimation extends AttackAnimation {
    public ComboAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, preDelay, contact, recovery, collider, colliderJoint, path, armature);
    }

    public ComboAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, preDelay, contact, recovery, hand, collider, colliderJoint, path, armature);
    }

    public ComboAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature, boolean noRegister) {
        super(convertTime, antic, preDelay, contact, recovery, hand, collider, colliderJoint, path, armature, noRegister);
    }

    public ComboAttackAnimation(float convertTime, String path, Armature armature, Phase... phases) {
        super(convertTime, path, armature, phases);
    }

    public ComboAttackAnimation(float convertTime, String path, Armature armature, boolean noRegister, Phase... phases) {
        super(convertTime, path, armature, noRegister, phases);
    }

    @Override
    protected void bindPhaseState(Phase phase) {
        float preDelay = phase.preDelay;

        this.stateSpectrumBlueprint
                .newTimePair(phase.start, preDelay)
                .addState(EntityState.PHASE_LEVEL, 1)
                .newTimePair(phase.start, phase.contact)
                .addState(EntityState.CAN_SKILL_EXECUTION, false)
                .newTimePair(phase.start, phase.recovery)
                .addState(EntityState.MOVEMENT_LOCKED, true)
                .addState(EntityState.UPDATE_LIVING_MOTION, false)
                .newTimePair(phase.start, phase.end)
                .addState(EntityState.INACTION, true)
                .newTimePair(phase.antic, phase.end)
                .addState(EntityState.TURNING_LOCKED, true)
                .newTimePair(preDelay, phase.contact)
                .addState(EntityState.ATTACKING, true)
                .addState(EntityState.PHASE_LEVEL, 2)
                .newTimePair(phase.contact, phase.end)
                .addState(EntityState.PHASE_LEVEL, 3)
        ;
    }
}
