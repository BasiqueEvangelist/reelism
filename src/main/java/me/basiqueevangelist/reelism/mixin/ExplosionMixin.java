package me.basiqueevangelist.reelism.mixin;

import me.basiqueevangelist.reelism.access.ExplosionAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin implements ExplosionAccess {
    @Unique private final List<Entity> ree$affectedEntities = new ArrayList<>();

    @Redirect(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean storeEntity(Entity entity, DamageSource source, float amount) {
        ree$affectedEntities.add(entity);
        return entity.damage(source, amount);
    }

    @Override
    public List<Entity> ree$getAffectedEntities() {
        return ree$affectedEntities;
    }
}
