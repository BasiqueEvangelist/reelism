package me.basiqueevangelist.reelism.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import org.apache.commons.lang3.ArrayUtils;

public class DropAbsurdSmeltingRecipes implements DataProvider {
    private final FabricDataGenerator dataGenerator;
    private final RecipeManager recipeManager;

    public DropAbsurdSmeltingRecipes(FabricDataGenerator dataGenerator, RecipeManager recipeManager) {
        this.dataGenerator = dataGenerator;
        this.recipeManager = recipeManager;
    }

    public static final Item[] BANNED_ITEMS = {
        Items.COAL,
        Items.DIAMOND,
        Items.EMERALD,
        Items.LAPIS_LAZULI,
        Items.QUARTZ,
        Items.REDSTONE,
    };

    @Override
    public void run(DataCache cache) {
        for (SmeltingRecipe recipe : recipeManager.listAllOfType(RecipeType.SMELTING)) {
            if (ArrayUtils.contains(BANNED_ITEMS, recipe.getOutput().getItem()))
                DatagenUtil.writeEmptyRecipe(dataGenerator, cache, recipe.getId());
        }

        for (BlastingRecipe recipe : recipeManager.listAllOfType(RecipeType.BLASTING)) {
            if (ArrayUtils.contains(BANNED_ITEMS, recipe.getOutput().getItem()))
                DatagenUtil.writeEmptyRecipe(dataGenerator, cache, recipe.getId());
        }
    }

    @Override
    public String getName() {
        return "Drop absurd smelting recipes";
    }
}
