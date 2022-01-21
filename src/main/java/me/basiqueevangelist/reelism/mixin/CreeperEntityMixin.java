package me.basiqueevangelist.reelism.mixin;

import me.basiqueevangelist.reelism.access.ExplosionAccess;
import me.basiqueevangelist.reelism.util.ReelismTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {
    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "onStruckByLightning", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;set(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;)V"))
    private void noFunnyLightning(DataTracker instance, TrackedData<Object> key, Object value) {
        // no.
    }

    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;discard()V"))
    private void spawnPoisonCloud(CallbackInfo ci) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, getX(), getY() + 0.25, getZ());
        float localDifficulty = world.getLocalDifficulty(getBlockPos()).getLocalDifficulty();
        cloud.setRadius(1.5F * localDifficulty);
        cloud.setRadiusOnUse(0);
        cloud.setWaitTime((int) (10 * localDifficulty));
        cloud.setPotion(Potions.STRONG_POISON);
        world.spawnEntity(cloud);
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"))
    private Explosion plantExplosion(World world, Entity entity, double x, double y, double z, float power, Explosion.DestructionType destructionType) {
        Explosion explosion = new Explosion(world, entity, null, null, x, y, z, power, false, Explosion.DestructionType.NONE);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(false);

        float localDifficulty = world.getLocalDifficulty(getBlockPos()).getLocalDifficulty();
        for (Entity affectedEntity : ((ExplosionAccess)explosion).ree$getAffectedEntities()) {
            if (affectedEntity instanceof LivingEntity living) {
                if (!living.isBlocking())
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, (int) (10 * 20 * localDifficulty)));

                living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, (int) (20 * localDifficulty)));
            }
        }

        for (BlockPos block : explosion.getAffectedBlocks()) {
            if (ReelismTags.CREEPER_REPLACEABLE_BLOCKS.contains(world.getBlockState(block).getBlock())) {
                world.setBlockState(block, Blocks.MYCELIUM.getDefaultState(), Block.NOTIFY_ALL);
            }
        }

        for (ServerPlayerEntity serverPlayerEntity : ((ServerWorld)world).getPlayers()) {
            if (serverPlayerEntity.squaredDistanceTo(x, y, z) < 4096.0) {
                serverPlayerEntity.networkHandler
                    .sendPacket(
                        new ExplosionS2CPacket(x, y, z, power, Collections.emptyList(), explosion.getAffectedPlayers().get(serverPlayerEntity))
                    );
            }
        }
        return explosion;
    }
}
