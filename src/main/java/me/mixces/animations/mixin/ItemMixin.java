package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class)
public class ItemMixin
{

    @Inject(
            method = "shouldCauseReequipAnimation",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true,
            remap = false
    )
    public void mixcesAnimations$removeCheck(ItemStack oldStack, ItemStack newStack, boolean slotChanged, CallbackInfoReturnable<Boolean> cir)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldReequip() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            cir.setReturnValue(slotChanged);
        }
    }

}
