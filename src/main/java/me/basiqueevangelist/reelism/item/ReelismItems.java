package me.basiqueevangelist.reelism.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.registry.Registry;

import static me.basiqueevangelist.reelism.Reelism.id;

public class ReelismItems {
    private ReelismItems() {

    }

    public static final BattleAxeItem IRON_BATTLE_AXE = new BattleAxeItem(ToolMaterials.IRON, 6.0F, -3.1F, new FabricItemSettings().group(ItemGroup.COMBAT));
    public static final BattleAxeItem DIAMOND_BATTLE_AXE = new BattleAxeItem(ToolMaterials.IRON, 7.0F, -3.0F, new FabricItemSettings().group(ItemGroup.COMBAT));
    public static final SturdyStickItem STURDY_STICK = new SturdyStickItem(1, -2.8F, new FabricItemSettings().group(ItemGroup.TOOLS));

    public static void register() {
        Registry.register(Registry.ITEM, id("iron_battle_axe"), IRON_BATTLE_AXE);
        Registry.register(Registry.ITEM, id("diamond_battle_axe"), DIAMOND_BATTLE_AXE);
        Registry.register(Registry.ITEM, id("sturdy_stick"), STURDY_STICK);
    }
}
