package com.ombremoon.epickombat.client.renderer.patched.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ombremoon.epickombat.client.renderer.layer.LightningLayer;
import com.ombremoon.epickombat.main.Constants;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedLightningLayer<E extends Player, T extends LivingEntityPatch<E>, M extends PlayerModel<E>> extends PatchedLayer<E, T, M, LightningLayer<E, M>> {

    @Override
    protected void renderLayer(T t, E e, @Nullable LightningLayer<E, M> emLightningLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, OpenMatrix4f[] openMatrix4fs, float bob, float yRot, float xRot, float partialTicks) {
        Constants.LOG.debug("{}", (Mth.lerp(partialTicks, e.yBodyRotO, e.yBodyRot) * ((float)Math.PI / 180F)) + (Math.PI / 2D));
    }
}
