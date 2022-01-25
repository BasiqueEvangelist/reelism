package me.basiqueevangelist.reelism.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.CombinedEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class DatagenUtil {
    private static final String EMPTY_SHA1 = DataProvider.SHA1.hashUnencodedChars("").toString();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public static void writeEmpty(DataGenerator generator, DataCache cache, String path) {
        Path filePath = generator.getOutput().resolve(path);
        try {
            Files.createDirectories(filePath.getParent());
            if (!Files.exists(filePath) || Objects.equals(cache.getOldSha1(filePath), EMPTY_SHA1)) {
                Files.write(filePath, new byte[0]);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        cache.updateSha1(filePath, EMPTY_SHA1);
    }

    public static void writeEmptyRecipe(DataGenerator generator, DataCache cache, Identifier id) {
        writeEmpty(generator, cache, "data/" + id.getNamespace() + "/recipes/" + id.getPath() + ".json");
    }

    public static void writeJson(DataGenerator generator, DataCache cache, String path, JsonElement element) {
        Path filePath = generator.getOutput().resolve(path);
        String jsonText = GSON.toJson(element);
        String hash = DataProvider.SHA1.hashUnencodedChars(jsonText).toString();

        try {
            Files.createDirectories(filePath.getParent());
            if (!Files.exists(filePath) || Objects.equals(cache.getOldSha1(filePath), hash)) {
                Files.writeString(filePath, jsonText);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        cache.updateSha1(filePath, hash);
    }

    public static boolean assumeConditionTrue(LootTable table, Predicate<LootCondition> predicate) {
        boolean changed = false;
        for (LootPool pool : ((FabricLootSupplier)table).getPools()) {
            List<LootCondition> conditions = new ArrayList<>(List.of(pool.conditions));
            changed |= conditions.removeIf(predicate);
            pool.conditions = conditions.toArray(LootCondition[]::new);

            for (LootPoolEntry entry : pool.entries)
                changed |= assumeConditionTrueEntry(entry, predicate);
        }
        return changed;
    }

    private static boolean assumeConditionTrueEntry(LootPoolEntry entry, Predicate<LootCondition> predicate) {
        boolean changed = false;
        if (entry instanceof CombinedEntry combined) {
            for (LootPoolEntry entry2 : combined.children) {
                changed |= assumeConditionTrueEntry(entry2, predicate);
            }
        }

        List<LootCondition> conditions = new ArrayList<>(List.of(entry.conditions));
        changed |= conditions.removeIf(predicate);
        entry.conditions = conditions.toArray(LootCondition[]::new);
        return changed;
    }
}
