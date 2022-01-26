package me.basiqueevangelist.reelism.client.entity;

import me.basiqueevangelist.reelism.Reelism;
import me.basiqueevangelist.reelism.client.ReelismClient;
import me.basiqueevangelist.reelism.entity.SpearEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class SpearEntityRenderer extends EntityRenderer<SpearEntity> {
    public static final Identifier TEXTURE = Reelism.id("textures/entity/spear/sturdy_stick.png");
    private final SpearEntityModel model;

    public SpearEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SpearEntityModel(context.getPart(ReelismClient.SPEAR_STICK_LAYER));
    }

    @Override
    public void render(SpearEntity spear, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, spear.prevYaw, spear.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, spear.prevPitch, spear.getPitch()) + 90.0F));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(spear)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
        super.render(spear, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(SpearEntity spear) {
        return TEXTURE;
    }
}
