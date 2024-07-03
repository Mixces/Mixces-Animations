package me.mixces.animations.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LayerHeldItem.class)
public class LayerHeldItemMixin {

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBiped;postRenderArm(F)V"
            )
    )
    private void mixcesAnimations$addSneakTranslation(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        if (entitylivingbaseIn.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
    }

    @Redirect(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;isSneaking()Z"
            )
    )
    private boolean mixcesAnimations$disableSneakTranslation(EntityLivingBase instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isSneaking();
    }

//    @Inject(
//            method = "doRenderLayer",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
//            )
//    )
//    private void mixcesAnimations$swordBlockPosition(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float s, CallbackInfo ci) {
//        if (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
//        if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).isBlocking()) {
//            mixcesAnimations$doBlockTransformations();
//        }
//    }
//
    @Unique
    private void mixcesAnimations$doBlockTransformations() {
        GlStateManager.translate(0.05f, 0.0f, -0.1f);
        GlStateManager.rotate(-50.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-60.0f, 0.0f, 0.0f, 1.0f);
    }

    @Unique private static final ThreadLocal<ItemStack> mixcesAnimations$stack = ThreadLocal.withInitial(() -> null);

    @ModifyVariable(
            method = "doRenderLayer",
            at = @At(
                    value = "STORE"
            ),
            index = 9
    )
    private ItemStack mixcesAnimations$captureStack(ItemStack stack) {
        mixcesAnimations$stack.set(stack);
        return stack;
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$oldItemPositions(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float s, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.enabled) {
            ItemStack stack = mixcesAnimations$stack.get();
            Item item = stack.getItem();
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).isBlocking()) {
                mixcesAnimations$doBlockTransformations();
            }
            if (!Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(stack) && !(stack.getItem() instanceof ItemSkull || stack.getItem() instanceof ItemBanner)) {
                float scale = 1.5F * 0.625F;
                if (item instanceof ItemBow) {
                    GlStateManager.rotate(-12.0F, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-7.0F, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(10.0F, 0.0f, 0.0f, 1.0f);
                    GlStateManager.rotate(1.0F, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-4.5F, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(-1.5F, 0.0f, 0.0f, 1.0f);
                    GlStateManager.translate(0.022F, -0.01F, -0.108F);
                    GlStateManager.scale(scale, scale, scale);
                } else if (item.isFull3D()) {
                    if (item.shouldRotateAroundWhenRendering()) {
                        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    }
                    GlStateManager.scale(scale / 0.85F, scale / 0.85F, scale / 0.85F);
                    GlStateManager.rotate(-2.4F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-20.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(4.5F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(-0.013F, 0.01F, 0.125F);
                } else {
                    scale = 1.5F * 0.375F;
                    GlStateManager.scale(scale / 0.55, scale / 0.55, scale / 0.55);
                    GlStateManager.rotate(-195.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-168.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(15.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(-0.047F, -0.28F, 0.038F);
                }
            }
        }
    }

}