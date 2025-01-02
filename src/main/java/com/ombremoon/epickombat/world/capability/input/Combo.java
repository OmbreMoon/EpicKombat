package com.ombremoon.epickombat.world.capability.input;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.types.StaticAnimation;

import java.util.List;
import java.util.function.Supplier;

public record Combo(String name, Supplier<StaticAnimation> animation, @Nullable List<Input> inputs) {

    public Combo(String name, Supplier<StaticAnimation> animation) {
        this(name, animation, null);
    }
}
