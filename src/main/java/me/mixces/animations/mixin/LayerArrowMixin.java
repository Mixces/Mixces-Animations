package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArrow.class)
public class LayerArrowMixin {

    @Inject(
            method = "doRenderLayer",
            at = @At("HEAD")
    )
    private void mixcesAnimations$disableStandardItemLighting(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getArrowLighting() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (entitylivingbaseIn.getArrowCountInEntity() > 0) RenderHelper.disableStandardItemLighting();
        }
    }

    @Inject(
            method = "doRenderLayer",
            at = @At("TAIL")
    )
    private void mixcesAnimations$enableStandardItemLighting(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getArrowLighting() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (entitylivingbaseIn.getArrowCountInEntity() > 0) RenderHelper.enableStandardItemLighting();
        }
    }
}
