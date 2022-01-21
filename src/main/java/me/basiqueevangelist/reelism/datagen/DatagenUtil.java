package me.basiqueevangelist.reelism.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatagenUtil {
    public static void writeEmpty(DataGenerator generator, String path) {
        Path filePath = generator.getOutput().resolve(path);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, new byte[0]);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeEmptyRecipe(DataGenerator generator, Identifier id) {
        writeEmpty(generator, "data/" + id.getNamespace() + "/recipes/" + id.getPath() + ".json");
    }
}
