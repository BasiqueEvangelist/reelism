package me.basiqueevangelist.reelism.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RegroupRecipes implements DataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private final FabricDataGenerator generator;
    private final RecipeManager recipeManager;
    private final ResourcePack source;

    public RegroupRecipes(FabricDataGenerator generator, RecipeManager recipeManager, ResourcePack source) {
        this.generator = generator;
        this.recipeManager = recipeManager;
        this.source = source;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void run(DataCache cache) throws IOException {
        for (var recipeType : Registry.RECIPE_TYPE)
            for (Recipe<?> recipe : recipeManager.listAllOfType((RecipeType<Recipe<Inventory>>) recipeType)) {
                String group = null;
                String itemPath = Registry.ITEM.getId(recipe.getOutput().getItem()).getPath();
                if (itemPath.contains("glazed_terracotta")) {
                    group = "glazed_terracotta";
                } else if (itemPath.contains("_dye")) {
                    group = "dye";
                } else if (itemPath.contains("sign")) {
                    group = "signs";
                }

                if (group != null) {
                    JsonObject jo;
                    try (InputStream is = source.open(ResourceType.SERVER_DATA, new Identifier(recipe.getId().getNamespace(), "recipes/" + recipe.getId().getPath() + ".json"))) {
                        jo = JsonParser.parseReader(new BufferedReader(new InputStreamReader(is))).getAsJsonObject();
                    }

                    jo.addProperty("group", group);

                    RecipesProvider.saveRecipe(cache, jo, generator.getOutput().resolve("data/" + recipe.getId().getNamespace() + "/recipes/" + recipe.getId().getPath() + ".json"));
                }
            }
    }

    @Override
    public String getName() {
        return null;
    }
}
