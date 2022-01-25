package me.basiqueevangelist.reelism.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.tag.TagManagerLoader;
import net.minecraft.util.Unit;
import net.minecraft.util.registry.DynamicRegistryManager;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class ReelismDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        ReloadableResourceManagerImpl resourceManager = new ReloadableResourceManagerImpl(ResourceType.SERVER_DATA);
        DynamicRegistryManager registryManager = DynamicRegistryManager.create();
        RecipeManager recipeManager = new RecipeManager();
        TagManagerLoader tagManagerLoader = new TagManagerLoader(registryManager);
        LootConditionManager conditionManager = new LootConditionManager();
        ServerAdvancementLoader advancementLoader = new ServerAdvancementLoader(conditionManager);
        LootManager lootManager = new LootManager(conditionManager);
        DefaultResourcePack defaultResourcePack = new DefaultResourcePack(VanillaDataPackProvider.DEFAULT_PACK_METADATA, "minecraft");
        resourceManager.registerReloader(tagManagerLoader);
        resourceManager.registerReloader(recipeManager);
        resourceManager.registerReloader(conditionManager);
        resourceManager.registerReloader(advancementLoader);
        resourceManager.registerReloader(lootManager);
        try {
            resourceManager.reload(ForkJoinPool.commonPool(), ForkJoinPool.commonPool(), CompletableFuture.completedFuture(Unit.INSTANCE), Collections.singletonList(defaultResourcePack)).whenComplete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        fabricDataGenerator.addProvider((generator) -> new DropAbsurdSmeltingRecipes(generator, recipeManager));
        fabricDataGenerator.addProvider((generator) -> new StonecutterOnlyRecipes(generator, recipeManager));
        fabricDataGenerator.addProvider((generator) -> new RegroupRecipes(generator, recipeManager, defaultResourcePack));
        fabricDataGenerator.addProvider((generator) -> new DisableAllVanillaAdvancements(generator, advancementLoader));
        fabricDataGenerator.addProvider((generator) -> new ProcessLootTables(generator, lootManager));
    }
}
