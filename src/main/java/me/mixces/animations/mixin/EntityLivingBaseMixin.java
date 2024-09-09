package me.mixces.animations.mixin;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends EntityMixin {

    @Shadow
    public boolean isSwingInProgress;

    @Shadow
    public int swingProgressInt;

    @Shadow
    protected abstract int getArmSwingAnimationEnd();

    @Unique
    public int mixcesAnimations$getArmSwingAnimationEnd() {
        return getArmSwingAnimationEnd(); /* we need to expose this method */
    }

    @Inject(
            method = "attackEntityFrom",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mixcesAnimations$disableInvulnerability(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (MixcesAnimationsConfig.INSTANCE.getDisableInvulnerable() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (!Minecraft.getMinecraft().isSingleplayer()) {
                hurtResistantTime = 0;
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "jump",
            at = @At(value = "HEAD")
    )
    private void mixcesAnimations$debugJump(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getJumpReset() && MixcesAnimationsConfig.INSTANCE.enabled) {
            UChat.chat("Jumped");
        }
    }
}
