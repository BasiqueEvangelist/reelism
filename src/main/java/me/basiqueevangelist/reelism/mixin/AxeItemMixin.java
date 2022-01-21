package me.basiqueevangelist.reelism.mixin;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float replaceAttackDamage(float attackDamage, ToolMaterial tm) {
        return (tm.getAttackDamage() + attackDamage + 1) / 2 - tm.getAttackDamage() - 1;
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private static float replaceAttackSpeed(float attackSpeed) {
        return (attackSpeed + 4.0F) / 2 - 4.0F;
    }
}
