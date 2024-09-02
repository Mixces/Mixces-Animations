package me.mixces.animations.mixin;

import me.mixces.animations.util.GlHelper;
import net.minecraft.client.renderer.entity.RenderSnowball;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSnowball.class)
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
            angle += 180.0F;
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
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$applyProjectilePosition(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled) {
           GlHelper.INSTANCE.translate(0.0F, 0.25F, 0.0625F);
        }
    }
}
