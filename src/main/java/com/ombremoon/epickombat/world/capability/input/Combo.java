package com.ombremoon.epickombat.world.capability.input;

import yesman.epicfight.api.animation.AnimationProvider;

import java.util.List;

public record Combo(String name, AnimationProvider<?> animation, List<Input> inputs) {
}
