package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(
            method = "hurtCameraEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mixcesAnimations$cancelHurtCam(float partialTicks, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getNoHurtCam() && MixcesAnimationsConfig.INSTANCE.enabled) {
            ci.cancel();
        }
    }
}
