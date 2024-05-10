package me.mixces.animations.mixin;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerControllerMP.class)
public abstract class PlayerControllerMPMixin {

    @Shadow protected abstract boolean isHittingPosition(BlockPos pos);
    @Shadow public abstract void resetBlockRemoving();
    @Shadow @Final private Minecraft mc;
    @Shadow private float curBlockDamageMP;

    @Redirect(
            method = "onPlayerDamageBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;isHittingPosition(Lnet/minecraft/util/BlockPos;)Z"
            )
    )
    private boolean mixcesAnimations$onPlayerDamageBlock(PlayerControllerMP instance, BlockPos pos) {
        return (!MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled || instance.getIsHittingBlock()) && isHittingPosition(pos);
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
    private void mixcesAnimations$onPlayerDamageBlock2(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        if (!MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.isAllowEdit()) {
            if (curBlockDamageMP > 0.0f) {
                resetBlockRemoving();
            }
            cir.setReturnValue(true);
        }
    }
    
}
