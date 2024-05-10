package me.mixces.animations.mixin;

import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class)
public abstract class EntityMixin {

    @Shadow public double posY;
    @Unique public float mixcesAnimations$yOffset;
    @Unique public float mixcesAnimations$ySize;

    @Inject(
            method = "setPositionAndRotation",
            at = @At(
                    value = "HEAD"
            )
    )
    private void mixcesAnimations$moveEntity(double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            mixcesAnimations$ySize = 0.0F;
        }
    }

    @Inject(
            method = "moveEntity",
            at = @At(
                    value = "INVOKE_STRING",
                    target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V",
                    args = "ldc=move",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$moveEntity2(double x, double y, double z, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            mixcesAnimations$ySize *= 0.4F;
        }
    }

    @Inject(
            method = "resetPositionToBB",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/entity/Entity;posY:D",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$resetPositionToBB(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            posY += mixcesAnimations$yOffset - mixcesAnimations$ySize;
        }
    }

}
