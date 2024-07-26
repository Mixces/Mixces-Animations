package me.mixces.animations.mixin;

import me.mixces.animations.util.GlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LayerHeldItem.class)
public abstract class LayerHeldItemMixin
{

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBiped;postRenderArm(F)V"
            )
    )
    private void mixcesAnimations$addSneakTranslation(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        if (entitylivingbaseIn.isSneaking())
        {
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
    private boolean mixcesAnimations$disableSneakTranslation(EntityLivingBase instance)
    {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return (false);
        }
        return instance.isSneaking();
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$swordBlockTransforms(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float s, CallbackInfo ci)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).isBlocking())
        {
            GlStateManager.translate(0.05F, 0.0F, -0.1F);
            GlHelper.INSTANCE.yaw(-50.0F).pitch(-10.0F).roll(-60.0F);
        }
    }

}