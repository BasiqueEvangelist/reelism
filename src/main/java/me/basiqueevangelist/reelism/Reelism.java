package me.basiqueevangelist.reelism;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reelism implements ModInitializer {
    public static final String MOD_ID = "reelism";
    public static final Logger LOGGER = LogManager.getLogger("Reelism");

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("It's time for your fantasy flattening!");
    }
}
