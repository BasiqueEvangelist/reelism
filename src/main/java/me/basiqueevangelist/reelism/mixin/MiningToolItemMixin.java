package me.basiqueevangelist.reelism.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.tag.Tag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends Item {
    @Shadow @Final private Tag<Block> effectiveBlocks;

    public MiningToolItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "postHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    public void noDamageOnHit(ItemStack st, int amount, LivingEntity entity, Consumer<LivingEntity> onBreak) {
    }

    @Redirect(method = "postMine", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    public void noDamageOnNonEffective(ItemStack st, int amount, LivingEntity entity, Consumer<LivingEntity> onBreak,
                                       ItemStack st2, World world, BlockState state) {
        if (isSuitableFor(state)
            || effectiveBlocks.contains(state.getBlock()) || getMiningSpeedMultiplier(st, state) != 1.0F)
            st.damage(amount, entity, onBreak);
    }
}
