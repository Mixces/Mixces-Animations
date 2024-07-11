package me.mixces.animations.mixin;

import me.mixces.animations.MixcesAnimations;
import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.mixin.interfaces.ItemRendererInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

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
            int equippedProgress = ((ItemRendererInterface) mc.getItemRenderer()).getEquippedItemSlot();
            cir.setReturnValue(cir.getReturnValue() && equippedProgress == currentItem);
        }
    }

    @ModifyVariable(
            method = "getTooltip",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            index = 3
    )
    private List<String> mixcesAnimations$captureF1(List<String> list)
    {
        if (MixcesAnimations.INSTANCE.getTooltipCache() != null && (System.currentTimeMillis() - MixcesAnimations.INSTANCE.getCacheTime()) >= 200)
        {
            return MixcesAnimations.INSTANCE.getTooltipCache();
        }
        return list;
    }

    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "RETURN"
            )
    )
    public void mixcesAnimations$cacheTooltip(EntityPlayer playerIn, boolean advanced, CallbackInfoReturnable<List<String>> cir)
    {
        MixcesAnimations.INSTANCE.setTooltipCache(cir.getReturnValue());
    }

}
