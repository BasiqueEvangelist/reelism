package me.basiqueevangelist.reelism.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import me.basiqueevangelist.reelism.access.ExplosionAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin implements ExplosionAccess {
    @Unique private final List<Entity> ree$affectedEntities = new ArrayList<>();

    @ModifyReceiver(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private Entity storeEntity(Entity entity, DamageSource source, float amount) {
        ree$affectedEntities.add(entity);
        return entity;
    }

    @Override
    public List<Entity> ree$getAffectedEntities() {
        return ree$affectedEntities;
    }
}
