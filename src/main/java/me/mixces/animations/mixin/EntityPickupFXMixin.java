package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.util.GlHelper;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPickupFX.class)
public abstract class EntityPickupFXMixin {

    @Inject(
            method = "renderParticle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntityWithPosYaw(Lnet/minecraft/entity/Entity;DDDFF)Z"
            )
    )
    private void mixcesAnimations$pushPosition(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPickup() && MixcesAnimationsConfig.INSTANCE.enabled) {
            GlStateManager.pushMatrix();
            GlHelper.INSTANCE.translate(0.0F, 0.5F, 0.0F);
        }
    }

    @Inject(
            method = "renderParticle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntityWithPosYaw(Lnet/minecraft/entity/Entity;DDDFF)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$popPosition(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPickup() && MixcesAnimationsConfig.INSTANCE.enabled) {
            GlStateManager.popMatrix();
        }
    }
}