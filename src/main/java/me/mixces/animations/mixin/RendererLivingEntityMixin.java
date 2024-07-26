package me.mixces.animations.mixin;

import me.mixces.animations.util.GlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin
{

    @Inject(
            method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
            )
    )
    private void mixcesAnimations$addModelSneakingTranslation(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        if (entity instanceof EntityPlayer && entity.isSneaking())
        {
            GlHelper.INSTANCE.translate(0.0F, -0.2F, 0.0F);
        }
    }

}
