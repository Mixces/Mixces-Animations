package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.util.GlHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class)
public abstract class ItemRendererMixin
{

    @Shadow @Final private RenderItem itemRenderer;
    @Shadow private ItemStack itemToRender;
    @Unique private static final ThreadLocal<Float> mixcesAnimations$f1 = ThreadLocal.withInitial(() -> 0.0F);

    @ModifyVariable(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "STORE"
            ),
            index = 4
    )
    private float mixcesAnimations$captureF1(float f1)
    {
        mixcesAnimations$f1.set(f1);
        return f1;
    }

    @ModifyArg(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;performDrinking(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;doBowTransformations(FLnet/minecraft/client/entity/AbstractClientPlayer;)V"
                    )
            ),
            index = 1
    )
    private float mixcesAnimations$useF1(float swingProgress)
    {
        if (MixcesAnimationsConfig.INSTANCE.getBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return mixcesAnimations$f1.get();
        }
        return swingProgress;
    }

    @Inject(
            method = "doBowTransformations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"
            )
    )
    private void mixcesAnimations$preBowTransform(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci)
    {
        if (MixcesAnimationsConfig.INSTANCE.getItemPositions() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            GlHelper.INSTANCE.roll(-335.0F).yaw(-50.0F);
            GlStateManager.translate(0.0F, 0.5F, 0.0F);
        }
    }

    @Inject(
            method = "doBowTransformations",
            at = @At(
                    value = "TAIL"
            )
    )
    private void mixcesAnimations$postBowTransform(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci)
    {
        if (MixcesAnimationsConfig.INSTANCE.getItemPositions() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            GlStateManager.translate(0.0F, -0.5F, 0.0F);
            GlHelper.INSTANCE.yaw(50.0F).roll(335.0F);
        }
    }

    @Inject(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$firstPersonItemPositions(float partialTicks, CallbackInfo ci)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getItemPositions() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        if (!itemRenderer.shouldRenderItemIn3D(itemToRender))
        {
            if (itemToRender.getItem().shouldRotateAroundWhenRendering())
            {
                GlHelper.INSTANCE.yaw(180.0F);
            }

            GlHelper.INSTANCE.scale(1.5F / 1.7F);
            GlHelper.INSTANCE.yaw(5.0F);
            GlStateManager.translate(-0.29F, 0.149F, -0.0328F);
        }
    }

}
