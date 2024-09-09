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

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public double posY;

    @Shadow
    public int hurtResistantTime;

    @Unique
    public float mixcesAnimations$ySize;

    @Shadow
    public abstract boolean isSneaking();

    @Inject(
            method = "moveEntity",
            at = @At(
                    value = "INVOKE_STRING",
                    target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V",
                    args = "ldc=move",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$onEntityMoveYSize(double x, double y, double z, CallbackInfo ci) {
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
    private void mixcesAnimations$moveEntityBB(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            posY -= mixcesAnimations$ySize;
        }
    }
}
