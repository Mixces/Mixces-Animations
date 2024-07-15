package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.mixin.interfaces.ItemRendererMixinInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class)
public abstract class ItemStackMixin
{

    @Inject(
            method = "getIsItemStackEqual",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    public void mixcesAnimations$addCheck(ItemStack p_179549_1_, CallbackInfoReturnable<Boolean> cir)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldReequip() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            Minecraft mc = Minecraft.getMinecraft();
            int currentItem = mc.thePlayer.inventory.currentItem;
            int equippedProgress = ((ItemRendererMixinInterface) mc.getItemRenderer()).getEquippedItemSlot();
            cir.setReturnValue(cir.getReturnValue() && equippedProgress == currentItem);
        }
    }

}
