package me.mixces.animations.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderSnowball;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderSnowball.class)
public abstract class RenderSnowballMixin<T extends Entity>
{

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 0
            ),
            index = 0
    )
    private float mixcesAnimations$rotateProjectile(float angle)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
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
    private float mixcesAnimations$useProperCameraView(float angle)
    {
        return (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled ? -1F : 1F) * angle;
    }

    @Inject(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    public void mixcesAnimations$modifyProjectileHeight(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci)
    {
        if (MixcesAnimationsConfig.INSTANCE.getItemPositions() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            GlStateManager.translate(0.0F, 0.25F, 0.0F);
        }
    }

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            ),
            index = 1
    )
    private ItemCameraTransforms.TransformType mixcesAnimations$replaceTransform(ItemCameraTransforms.TransformType cameraTransformType)
    {
        if (MixcesAnimationsConfig.INSTANCE.getFastItems() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return ItemCameraTransforms.TransformType.GUI;
        }
        return cameraTransformType;
    }

}
