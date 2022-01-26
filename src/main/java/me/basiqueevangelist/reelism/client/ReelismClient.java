package me.basiqueevangelist.reelism.client;

import io.github.foundationgames.jsonem.JsonEM;
import me.basiqueevangelist.reelism.Reelism;
import me.basiqueevangelist.reelism.client.entity.SpearEntityRenderer;
import me.basiqueevangelist.reelism.entity.ReelismEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ReelismClient implements ClientModInitializer {
    public static final EntityModelLayer SPEAR_STICK_LAYER = new EntityModelLayer(Reelism.id("spear/sturdy_stick"), "main");

    @Override
    public void onInitializeClient() {
        JsonEM.registerModelLayer(SPEAR_STICK_LAYER);
        EntityRendererRegistry.register(ReelismEntities.SPEAR, SpearEntityRenderer::new);
    }
}
