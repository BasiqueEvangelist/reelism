package me.basiqueevangelist.reelism.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;
import net.minecraft.server.ServerAdvancementLoader;

public class DisableAllVanillaAdvancements implements DataProvider {
    private final FabricDataGenerator dataGenerator;
    private final ServerAdvancementLoader advancementLoader;

    public DisableAllVanillaAdvancements(FabricDataGenerator dataGenerator, ServerAdvancementLoader advancementLoader) {
        this.dataGenerator = dataGenerator;
        this.advancementLoader = advancementLoader;
    }

    @Override
    public void run(DataCache cache) {
        for (Advancement adv : advancementLoader.getAdvancements()) {
            DatagenUtil.writeEmpty(dataGenerator, cache, "data/" + adv.getId().getNamespace() + "/advancements/" + adv.getId().getPath() + ".json");
        }
    }

    @Override
    public String getName() {
        return "Disable all vanilla advancements";
    }
}
