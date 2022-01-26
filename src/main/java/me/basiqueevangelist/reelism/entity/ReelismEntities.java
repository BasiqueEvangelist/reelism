package me.basiqueevangelist.reelism.entity;

import me.basiqueevangelist.reelism.Reelism;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ReelismEntities {
    private ReelismEntities() {

    }

    public static final EntityType<SpearEntity> SPEAR = FabricEntityTypeBuilder.<SpearEntity>create(SpawnGroup.MISC, SpearEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(20).build();

    public static void register() {
        Registry.register(Registry.ENTITY_TYPE, Reelism.id("spear"), SPEAR);
    }
}
