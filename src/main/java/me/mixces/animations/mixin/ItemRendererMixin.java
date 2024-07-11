package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class)
public abstract class ItemRendererMixin
{

    @Shadow private float prevEquippedProgress;
    @Shadow @Final private Minecraft mc;
    @Shadow private float equippedProgress;
    @Shadow private ItemStack itemToRender;
    @Shadow private int equippedItemSlot;
    @Unique private static final ThreadLocal<Float> mixcesAnimations$f1 = ThreadLocal.withInitial(() -> 0.0F);

    @ModifyVariable(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "STORE"
            ),
            index = 4
    )
    private float mixcesAnimations$captureF1(float f1)
    {
        mixcesAnimations$f1.set(f1);
        return f1;
    }

    @ModifyArg(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;performDrinking(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;doBowTransformations(FLnet/minecraft/client/entity/AbstractClientPlayer;)V"
                    )
            ),
            index = 1
    )
    private float mixcesAnimations$useF1(float swingProgress)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return mixcesAnimations$f1.get();
        }
        return swingProgress;
    }

//    @Inject(
//             method = "updateEquippedItem",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    private void adw(CallbackInfo ci)
//    {
//        ci.cancel();
//        this.prevEquippedProgress = this.equippedProgress;
//        EntityPlayer entityplayer = this.mc.thePlayer;
//        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
//        boolean flag = false;
//        if (this.itemToRender != null && itemstack != null)
//        {
//            if (!this.itemToRender.getIsItemStackEqual(itemstack) || this.equippedItemSlot != entityplayer.inventory.currentItem)
//            {
//                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("REAL"));
//                if (this.equippedItemSlot != entityplayer.inventory.currentItem || !this.itemToRender.getItem().shouldCauseReequipAnimation(this.itemToRender, itemstack, this.equippedItemSlot != entityplayer.inventory.currentItem))
//                {
//                    this.itemToRender = itemstack;
//                    this.equippedItemSlot = entityplayer.inventory.currentItem;
//                    return;
//                }
//
//                flag = true;
//            }
//        }
//        else if (this.itemToRender == null && itemstack == null)
//        {
//            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("INVENTORY?"));
//            flag = false;
//        }
//        else
//        {
//            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("NOOB"));
//            flag = true;
//        }
//
//        float f = 0.4F;
//        float f1 = flag ? 0.0F : 1.0F;
//        float f2 = MathHelper.clamp_float(f1 - this.equippedProgress, -f, f);
//        this.equippedProgress += f2;
//        if (this.equippedProgress < 0.1F)
//        {
//            this.itemToRender = itemstack;
//            this.equippedItemSlot = entityplayer.inventory.currentItem;
//        }
//    }

}
