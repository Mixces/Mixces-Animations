package me.mixces.animations.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerHeldItem.class)
public class LayerHeldItemMixin {

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBiped;postRenderArm(F)V"
            )
    )
    private void mixcesAnimations$doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
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
    private boolean mixcesAnimations$doRenderLayer2(EntityLivingBase instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isSneaking();
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$doRenderLayer3(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float s, CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).isBlocking()) {
            GlStateManager.translate(0.05f, 0.0f, -0.1f);
            GlStateManager.rotate(-50.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-10.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-60.0f, 0.0f, 0.0f, 1.0f);
        }
    }

}