package me.basiqueevangelist.reelism.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;

public class ProcessLootTables implements DataProvider {
    private final FabricDataGenerator dataGenerator;
    private final LootManager lootManager;

    public ProcessLootTables(FabricDataGenerator dataGenerator, LootManager lootManager) {
        this.dataGenerator = dataGenerator;
        this.lootManager = lootManager;
    }

    private static final String[] NO_STRANGE_SILKTOUCH = {
        "blocks/grass_block",
        "blocks/glass",
        "blocks/grass_path",
        "blocks/ender_chest",
        "blocks/bookshelf",
        "blocks/campfire",
        "blocks/soul_campfire",
    };

    private static final String[] ALWAYS_SURVIVE_EXPLOSION = {
        "blocks/coarse_dirt",
        "blocks/dirt",
        "blocks/farmland",
//        "blocks/gold_ore",
//        "blocks/iron_ore",
        "blocks/sand"
    };

    private static final String[] ALL = {
        "blocks/grass_block",
        "blocks/glass",
        "blocks/grass_path",
        "blocks/ender_chest",
        "blocks/bookshelf",
        "blocks/campfire",
        "blocks/soul_campfire",

        "blocks/coarse_dirt",
        "blocks/dirt",
        "blocks/farmland",
//        "blocks/gold_ore",
//        "blocks/iron_ore",
        "blocks/sand"
    };

    @Override
    public void run(DataCache cache) {
        for (String path : ALL) {
            LootTable table = lootManager.getTable(new Identifier(path));

            boolean changed = false;

            if (ArrayUtils.contains(ALWAYS_SURVIVE_EXPLOSION, path))
                changed |= DatagenUtil.assumeConditionTrue(table, lootCondition -> lootCondition instanceof SurvivesExplosionLootCondition);

            if (ArrayUtils.contains(NO_STRANGE_SILKTOUCH, path))
                changed |= DatagenUtil.assumeConditionTrue(table, lootCondition -> {
                    if (!(lootCondition instanceof MatchToolLootCondition mtlc)) return false;

                    for (EnchantmentPredicate ench : mtlc.predicate.enchantments) {
                        if (ench.enchantment == Enchantments.SILK_TOUCH) return true;
                    }

                    return false;
                });

            if (!changed) continue;

            DatagenUtil.writeJson(dataGenerator, cache, "data/minecraft/loot_tables/" + path + ".json", LootManager.toJson(table));
        }
    }

    @Override
    public String getName() {
        return "Process loot tables";
    }
}
