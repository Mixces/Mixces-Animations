package me.mixces.animations.mixin;

import me.mixces.animations.api.IDamageTint;
import me.mixces.animations.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin implements IDamageTint {

    @Shadow
    protected abstract int getColorMultiplier(EntityLivingBase entitylivingbaseIn, float lightBrightness, float partialTickTime);

    @Shadow protected FloatBuffer brightnessBuffer;
    @Unique
    private static final ThreadLocal<Float> mixcesAnimations$f = ThreadLocal.withInitial(() -> 1.0F);

    @ModifyVariable(
            method = "setBrightness",
            at = @At("STORE"),
            index = 4
    )
    private float mixcesAnimations$captureF(float value) {
        mixcesAnimations$f.set(value);
        return value;
    }

    @Inject(
            method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
            )
    )
    private void mixcesAnimations$removeSneakingTranslation(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (entity.isSneaking()) GlHelper.INSTANCE.translate(0.0F, -0.2F, 0.0F);
        }
    }

    @ModifyArg(
            method = "setBrightness",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;",
                    ordinal = 0
            )
    )
    public float mixcesAnimations$replaceHitAlpha(float original) {
        return MixcesAnimationsConfig.INSTANCE.getDamageTint() && MixcesAnimationsConfig.INSTANCE.enabled ? mixcesAnimations$f.get() : original;
    }

    @ModifyArg(
            method = "setBrightness",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;",
                    ordinal = 3
            )
    )
    public float mixcesAnimations$modifyRedTint(float original) {
        return MixcesAnimationsConfig.INSTANCE.getDamageTint() && MixcesAnimationsConfig.INSTANCE.enabled ? 0.4f : original;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean setupDamageTint(EntityLivingBase entitylivingbaseIn, float partialTicks) {
        float f = entitylivingbaseIn.getBrightness(partialTicks);
        int i = getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        boolean flag = (i >> 24 & 0xFF) > 0;
        boolean flag1 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;

        float red = brightnessBuffer.get(0);
        float green = brightnessBuffer.get(1);
        float blue = brightnessBuffer.get(2);
        float alpha = brightnessBuffer.get(3);

        if (!flag && !flag1) {
            return false;
        } else {
            Minecraft.getMinecraft().entityRenderer.disableLightmap();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.depthFunc(514);
            GlStateManager.color(red, green, blue, alpha);
            return true;
        }
    }
}
