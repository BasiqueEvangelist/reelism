package me.basiqueevangelist.reelism.util;

import me.basiqueevangelist.reelism.Reelism;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class ReelismTags {
    private ReelismTags() {

    }

    public static final Tag.Identified<Block> CREEPER_REPLACEABLE_BLOCKS = TagFactory.BLOCK.create(Reelism.id("creeper_replaceable_blocks"));
    public static final Tag.Identified<Item> CAN_BREAK_SHIELD = TagFactory.ITEM.create(Reelism.id("can_break_shield"));
}
