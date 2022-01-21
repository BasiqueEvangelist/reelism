package me.basiqueevangelist.reelism.mixin;

import me.basiqueevangelist.reelism.util.ReelismTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Redirect(method = "takeShieldHit", at = @At(value = "JUMP", shift = At.Shift.BEFORE))
    public boolean makeBattleAxesBreakShields(Object o, Class<?> axe) {
        return ReelismTags.CAN_BREAK_SHIELD.contains((Item)o);
    }
}
