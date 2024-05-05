package me.mixces.animations.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    
    @Inject(
            method = "hurtCameraEffect",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void mixcesAnimations$hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getBetterShake() && MixcesAnimationsConfig.INSTANCE.enabled) {
            ci.cancel();
        }
    }

}
