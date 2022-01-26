package me.basiqueevangelist.reelism.item;

import me.basiqueevangelist.reelism.entity.SpearEntity;
import me.basiqueevangelist.reelism.util.ReelismTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SturdyStickItem extends MiningToolItem {
    protected SturdyStickItem(float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, ToolMaterials.WOOD, ReelismTags.STURDY_STICK_MINEABLE, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        int useTicks = this.getMaxUseTime(stack) - remainingUseTicks;

        if (useTicks < 10) return;

        if (!world.isClient) {
            stack.damage(1, player, p -> p.sendToolBreakStatus(user.getActiveHand()));

            SpearEntity spear = new SpearEntity(world, player, stack);
            spear.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.5F + (float)useTicks * 0.125F, 1.0F);
            if (player.getAbilities().creativeMode) {
                spear.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }

            world.spawnEntity(spear);
            world.playSoundFromEntity(null, spear, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F); // TODO: Make spear sounds.
            if (!player.getAbilities().creativeMode) {
                player.getInventory().removeOne(stack);
            }
        }

        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }
}
