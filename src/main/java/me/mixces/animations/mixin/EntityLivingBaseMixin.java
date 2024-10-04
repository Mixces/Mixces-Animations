package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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

    @Shadow
    public float moveForward;

    @Shadow
    public float moveStrafing;

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
            method = "moveEntityWithHeading",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;moveEntity(DDD)V"
            )
    )
    private void mixcesAnimations$instantStop(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getInstantMovement() && MixcesAnimationsConfig.INSTANCE.enabled) {
            EntityLivingBase entity = ((EntityLivingBase) (Object) this);
            if (entity.getEntityId() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
                EntityPlayerSP player = ((EntityPlayerSP) entity);
                if (player.hurtTime > 0 || !player.onGround || player.movementInput.jump) return;
                if (moveForward == 0 && moveStrafing == 0 && (motionX != 0 || motionZ != 0)) {
                    motionX = 0.0;
                    motionZ = 0.0;
                }
            }
        }
    }
}
