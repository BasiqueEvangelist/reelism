package me.basiqueevangelist.reelism.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpearEntity extends PersistentProjectileEntity {
    private static final TrackedData<ItemStack> STACK = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    private boolean dealtDamage;

    public SpearEntity(EntityType<SpearEntity> entityType, World world) {
        super(entityType, world);
    }

    public SpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ReelismEntities.SPEAR, owner, world);
        this.getDataTracker().set(STACK, stack);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STACK, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return this.getDataTracker().get(STACK).copy();
    }

    @Override
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity attacked = entityHitResult.getEntity();
        float damage = 4.0F;
        if (attacked instanceof LivingEntity living) {
            damage += EnchantmentHelper.getAttackDamage(this.getDataTracker().get(STACK), living.getGroup());
        }

        DamageSource damageSource = DamageSource.trident(this, this.getOwner() == null ? this : this.getOwner());
        dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT; // TODO: Make spear sounds.
        if (attacked.damage(damageSource, damage)) {
            if (attacked.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (attacked instanceof LivingEntity living) {
                if (getOwner() instanceof LivingEntity owner) {
                    EnchantmentHelper.onUserDamaged(living, owner);
                    EnchantmentHelper.onTargetDamaged(owner, living);
                }
            }
        }

        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.velocityDirty = true;
        float g = 1.0F;

        this.playSound(soundEvent, g, 1.0F);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    @Override
    protected SoundEvent getHitSound() {
        // TODO: Make spear sounds.
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("Spear", NbtElement.COMPOUND_TYPE)) {
            this.dataTracker.set(STACK, ItemStack.fromNbt(nbt.getCompound("Spear")));
        }

        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Spear", this.dataTracker.get(STACK).writeNbt(new NbtCompound()));
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    protected float getDragInWater() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}
