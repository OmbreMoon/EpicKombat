package com.ombremoon.epickombat.world.capability;

import com.ombremoon.epickombat.main.CommonClass;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KombatKapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>, NonNullSupplier<KombatKapability> {
    public static final Capability<KombatKapability> KOMBAT_KAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation CAPABILITY_LOCATION = CommonClass.customLocation("kombat_kapability");

    private KombatKapability kombatKapability;
    private final LazyOptional<KombatKapability> optional = LazyOptional.of(() -> kombatKapability);

    public KombatKapabilityProvider() {
        this.kombatKapability = new KombatKapability();
    }

    public static KombatKapability get(Player player) {
        return player.getCapability(KOMBAT_KAPABILITY).orElseThrow(NullPointerException::new);
    }

    public static boolean isPresent(Player player) {
        return player.getCapability(KOMBAT_KAPABILITY).isPresent();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return KOMBAT_KAPABILITY.orEmpty(cap, this.optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    @Override
    public @NotNull KombatKapability get() {
        return this.kombatKapability;
    }

    public boolean hasCap() {
        return this.kombatKapability != null;
    }
}
