package me.mixces.animations.mixin;

import net.minecraft.client.renderer.entity.RenderSnowball;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = RenderSnowball.class)
public abstract class RenderSnowballMixin {

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 0
            ),
            index = 0
    )
    private float mixcesAnimations$rotateProjectile(float angle) {
        if (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return 180.0F + angle;
        }
        return angle;
    }

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 1
            ),
            index = 0
    )
    private float mixcesAnimations$useProperCameraView(float angle) {
        return (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled ? -1F : 1F) * angle;
    }

}
