package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.SprintReset;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends EntityMixin {

    @Shadow
    public boolean isSwingInProgress;

    @Shadow
    public int swingProgressInt;

    @Shadow
    protected abstract int getArmSwingAnimationEnd();

    @Shadow
    public abstract void setJumping(boolean jumping);

    @Unique
    public int mixcesAnimations$getArmSwingAnimationEnd() {
        return getArmSwingAnimationEnd(); /* we need to expose this method */
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE_STRING",
                    target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V",
                    args = "ldc=jump",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$doJump(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getJumpReset() && MixcesAnimationsConfig.INSTANCE.enabled && SprintReset.getShouldJump()) {
            setJumping(true);
            SprintReset.setShouldJump(false);
        }
    }
}
