package me.basiqueevangelist.reelism.mixin;

import me.basiqueevangelist.reelism.item.BattleAxeItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/enchantment/EnchantmentTarget$11")
public class WeaponEnchantmentTargetMixin {
    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void addBattleAxes(Item item, CallbackInfoReturnable<Boolean> cir) {
        if (item instanceof BattleAxeItem)
            cir.setReturnValue(true);
    }
}
