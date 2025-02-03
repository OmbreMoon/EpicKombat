package com.ombremoon.epickombat.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ombremoon.epickombat.main.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;

public class LightningLayer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    public LightningLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
//        double d0 = (Mth.lerp(pPartialTick, player.yBodyRotO, player.yBodyRot) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
//        Vec3 vec31 = player.getLeashOffset(pPartialTick);
//        Constants.LOG.debug("{}", Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x);
//        List<Cow> cows = player.level().getEntitiesOfClass(Cow.class, player.getBoundingBox().inflate(4));
//        for (var cow : cows) {
//            if (cow != null) {
//                renderLeash(cow, pPartialTick, pPoseStack, pBuffer, player);
//            }
//        }
    }

    private void renderLeash(LivingEntity pEntityLiving, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, Player player) {
        pMatrixStack.pushPose();
//        Vec3 vec3 = player.getRopeHoldPosition(pPartialTicks);
        double d9 = 0.22D * (player.getMainArm() == HumanoidArm.RIGHT ? -1.0D : 1.0D);
        float f7 = Mth.lerp(pPartialTicks * 0.5F, player.getXRot(), player.xRotO) * ((float)Math.PI / 180F);
        float f8 = Mth.lerp(pPartialTicks, player.yBodyRotO, player.yBodyRot) * ((float)Math.PI / 180F);
        double d8 = player.getBoundingBox().getYsize() - 1.0D;
        double d6 = player.isCrouching() ? -0.2D : 0.07D;
        Vec3 vec3 = player.getPosition(pPartialTicks).add((new Vec3(d9, 2.2D, -0.15D)).xRot(-f7).yRot(-f8));
        double d0 = (double)(Mth.lerp(pPartialTicks, pEntityLiving.yBodyRotO, pEntityLiving.yBodyRot) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = pEntityLiving.getLeashOffset(pPartialTicks);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(pPartialTicks, pEntityLiving.xo, pEntityLiving.getX()) + d1;
        double d4 = Mth.lerp(pPartialTicks, pEntityLiving.yo, pEntityLiving.getY()) + vec31.y;
        double d5 = Mth.lerp(pPartialTicks, pEntityLiving.zo, pEntityLiving.getZ()) + d2;
        pMatrixStack.translate(d1, vec31.y, d2);
        float f = (float)(vec3.x - d3);
        float f1 = (float)(vec3.y - d4);
        float f2 = (float)(vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = BlockPos.containing(pEntityLiving.getEyePosition(pPartialTicks));
        BlockPos blockpos1 = BlockPos.containing(player.getEyePosition(pPartialTicks));
        int i = pEntityLiving.level().getBrightness(LightLayer.BLOCK, blockpos);
        int j = pEntityLiving.level().getBrightness(LightLayer.BLOCK, blockpos1);
        int k = pEntityLiving.level().getBrightness(LightLayer.SKY, blockpos);
        int l = pEntityLiving.level().getBrightness(LightLayer.SKY, blockpos1);

        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }

        pMatrixStack.popPose();
    }

    private static void addVertexPair(VertexConsumer pConsumer, Matrix4f pMatrix, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float)p_174321_ / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        float f1 = p_174321_ % 2 == (p_174322_ ? 1 : 0) ? 0.7F : 1.0F;
        float f2 = 0.5F * f1;
        float f3 = 0.4F * f1;
        float f4 = 0.3F * f1;
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        pConsumer.vertex(pMatrix, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        pConsumer.vertex(pMatrix, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }
}
