package me.mixces.animations.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderSnowball.class)
public abstract class RenderSnowballMixin <T extends Entity> extends Render<T> {

    protected RenderSnowballMixin(RenderManager renderManager) {
        super(renderManager);
    }

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

    @Inject(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderSnowball;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"
            )
    )
    private void mixcesAnimations$translateProjectile(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled) {
            GlStateManager.translate(0.0F, 0.25F, 0.0F);
        }
    }

}
