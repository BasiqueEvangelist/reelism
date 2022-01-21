package me.basiqueevangelist.reelism.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.util.Unit;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class ReelismDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        ReloadableResourceManagerImpl resourceManager = new ReloadableResourceManagerImpl(ResourceType.SERVER_DATA);
        RecipeManager recipeManager = new RecipeManager();
        resourceManager.registerReloader(recipeManager);
        try {
            resourceManager.reload(ForkJoinPool.commonPool(), ForkJoinPool.commonPool(), CompletableFuture.completedFuture(Unit.INSTANCE), Collections.singletonList(new DefaultResourcePack(VanillaDataPackProvider.DEFAULT_PACK_METADATA, "minecraft"))).whenComplete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        fabricDataGenerator.addProvider((generator) -> new DropAbsurdSmeltingRecipes(generator, recipeManager));
    }
}
