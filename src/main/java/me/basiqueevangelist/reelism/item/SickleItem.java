package me.basiqueevangelist.reelism.item;

import me.basiqueevangelist.reelism.util.ReelismTags;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;

public class SickleItem extends MiningToolItem {
    protected SickleItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, material, ReelismTags.SICKLE_MINEABLE, settings);
    }
}
