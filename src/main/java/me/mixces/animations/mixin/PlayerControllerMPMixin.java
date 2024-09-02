package me.mixces.animations.mixin;

import me.mixces.animations.hook.SprintReset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin {

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    private float curBlockDamageMP;

    @Shadow
    protected abstract boolean isHittingPosition(BlockPos pos);

    @Shadow
    public abstract void resetBlockRemoving();

    @Redirect(
            method = "onPlayerDamageBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;isHittingPosition(Lnet/minecraft/util/BlockPos;)Z"
            )
    )
    private boolean mixcesAnimations$includeIsHittingCheck(PlayerControllerMP instance, BlockPos pos) {
        return ((!MixcesAnimationsConfig.INSTANCE.getBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) || instance.getIsHittingBlock()) && isHittingPosition(pos);
    }

    @Inject(
            method = "onPlayerDamageBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void mixcesAnimations$resetDestroyProgress(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        if (MixcesAnimationsConfig.INSTANCE.getBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (mc.thePlayer.isUsingItem() && mc.thePlayer.isAllowEdit()) {
                if (curBlockDamageMP > 0.0f) resetBlockRemoving();
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(
            method = "attackEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$resetSprint(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSprintReset() && MixcesAnimationsConfig.INSTANCE.enabled) SprintReset.setShouldStop(true);
    }
}
