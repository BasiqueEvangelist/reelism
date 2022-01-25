package me.basiqueevangelist.reelism.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class StonecutterOnlyRecipes implements DataProvider {
    private final FabricDataGenerator dataGenerator;
    private final RecipeManager recipeManager;

    public StonecutterOnlyRecipes(FabricDataGenerator dataGenerator, RecipeManager recipeManager) {
        this.dataGenerator = dataGenerator;
        this.recipeManager = recipeManager;
    }

    @Override
    public void run(DataCache cache) {
        for (var recipe : recipeManager.listAllOfType(RecipeType.STONECUTTING)) {
            var matchingCraftingRecipeOpt = recipeManager.listAllOfType(RecipeType.CRAFTING)
                .stream()
                .filter(
                    x -> x.getIngredients()
                        .stream()
                        .anyMatch(
                            y -> y.getMatchingStacks().length > 0 && y.getMatchingStacks()[0].getItem() == recipe.getIngredients().get(0).getMatchingStacks()[0].getItem())
                    && x.getOutput().getItem() == recipe.getOutput().getItem())
                .findFirst();

            if (matchingCraftingRecipeOpt.isEmpty()) continue;

            var outputItemName = Registry.ITEM.getId(recipe.getOutput().getItem()).getPath();
            var inputItemName = Registry.ITEM.getId(recipe.getIngredients().get(0).getMatchingStacks()[0].getItem()).getPath();
            if (!outputItemName.contains("polished") && (outputItemName.contains("cobble") || inputItemName.contains("brick") || outputItemName.contains("sandstone")))
                continue;

            DatagenUtil.writeEmptyRecipe(dataGenerator, cache, matchingCraftingRecipeOpt.get().getId());
        }
    }

    @Override
    public String getName() {
        return "Stonecutter-only recipes";
    }
}
