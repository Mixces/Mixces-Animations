package me.mixces.animations.mixin;

import me.mixces.animations.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
        return (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isSneaking();
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void mixcesAnimations$thirdPersonItemPositions(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float s, CallbackInfo ci, ItemStack stack, Item item)
    {
        if (!MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        if (MixcesAnimationsConfig.INSTANCE.getBlockHitting() && entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).isBlocking())
        {
            GlStateManager.translate(0.05F, 0.0F, -0.1F);
            GlHelper.INSTANCE.yaw(-50.0F).pitch(-10.0F).roll(-60.0F);
        }

        if (MixcesAnimationsConfig.INSTANCE.getItemPositions() && !Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(stack))
        {
            float scale = 1.5F * 0.625F;

            if (item instanceof ItemBow)
            {
                GlHelper.INSTANCE.yaw(-12.0F).pitch(-7.0F).roll(10.0F).yaw(1.0F).pitch(-4.5F).roll(-1.5F);
                GlStateManager.translate(0.022F, -0.01F, -0.108F);
                GlHelper.INSTANCE.scale(scale);
            }
            else if (item.isFull3D())
            {
                scale /= 0.85F;

                if (item.shouldRotateAroundWhenRendering())
                {
                    GlHelper.INSTANCE.roll(180.0F);
                }

                GlHelper.INSTANCE.scale(scale);
                GlHelper.INSTANCE.yaw(-2.4F).pitch(-20.0F).roll(4.5F);
                GlStateManager.translate(-0.013F, 0.01F, 0.125F);
            }
            else
            {
                scale = 1.5F * 0.375F;

                GlHelper.INSTANCE.scale(scale / 0.55F);
                GlHelper.INSTANCE.yaw(-195.0F).pitch(-168.0F).roll(15.0F);
                GlStateManager.translate(-0.047F, -0.28F, 0.038F);
            }
        }
    }

}