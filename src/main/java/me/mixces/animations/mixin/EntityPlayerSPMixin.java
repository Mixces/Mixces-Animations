package me.mixces.animations.mixin;

import me.mixces.animations.MixcesAnimations;
import me.mixces.animations.hook.SprintReset;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends EntityPlayerMixin {

    @Shadow
    public MovementInput movementInput;

    @Shadow
    public abstract void setSprinting(boolean sprinting);

    @Inject(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;pushOutOfBlocks(DDD)Z",
                    ordinal = 0
            )
    )
    private void mixcesAnimations$onSneakYSize(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (movementInput.sneak && mixcesAnimations$ySize < 0.2F) mixcesAnimations$ySize = 0.2F;
        }
    }

    @Inject(
            method = "swingItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mixcesAnimations$removeSwingPackets(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (isUsingItem()) {
                ci.cancel();
                mixcesAnimations$swingItem();
            }
        }
    }

    @Unique
    private void mixcesAnimations$swingItem() { /* packet-less swing method */
        if (!isSwingInProgress || swingProgressInt >= mixcesAnimations$getArmSwingAnimationEnd() / 2 || swingProgressInt < 0) {
            swingProgressInt = -1;
            isSwingInProgress = true;
        }
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isSprinting()Z",
                    ordinal = 2
            )
    )
    private void mixcesAnimations$stopSprinting(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.enabled && SprintReset.INSTANCE.getShouldStop()) {
            SprintReset.INSTANCE.setShouldStop(false);
            setSprinting(false);
        }
    }
}
