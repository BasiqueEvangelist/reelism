package me.basiqueevangelist.reelism.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "applyClimbingSpeed", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"))
    public double doubleIfLookingDown(double motY, double y) {
        double orig = Math.max(motY, y);
        return orig * (getPitch() >= 45 && orig < 0 ? 3 : 1);
    }

    @Inject(method = "blockedByShield", at = @At(value = "HEAD"), cancellable = true)
    public void disableExplosionBlocking(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source.isExplosive())
            cir.setReturnValue(false);
    }
}
