package me.basiqueevangelist.reelism.datagen;

import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class DatagenUtil {
    private static final String EMPTY_SHA1 = DataProvider.SHA1.hashUnencodedChars("").toString();

    public static void writeEmpty(DataGenerator generator, DataCache cache, String path) {
        Path filePath = generator.getOutput().resolve(path);
        try {
            Files.createDirectories(filePath.getParent());
            if (!Files.exists(filePath) || Objects.equals(cache.getOldSha1(filePath), EMPTY_SHA1)) {
                Files.write(filePath, new byte[0]);
                cache.updateSha1(filePath, EMPTY_SHA1);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeEmptyRecipe(DataGenerator generator, DataCache cache, Identifier id) {
        writeEmpty(generator, cache, "data/" + id.getNamespace() + "/recipes/" + id.getPath() + ".json");
    }
}
