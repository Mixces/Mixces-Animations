package me.mixces.animations.mixin;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends EntityPlayerMixin
{

    @Shadow public MovementInput movementInput;

    @Inject(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;pushOutOfBlocks(DDD)Z",
                    ordinal = 0
            )
    )
    private void mixcesAnimations$onSneakYSize(CallbackInfo ci)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        if (movementInput.sneak && mixcesAnimations$ySize < 0.2F)
        {
            mixcesAnimations$ySize = 0.2F;
        }
    }

    @Inject(
            method = "swingItem",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = (true)
    )
    private void mixcesAnimations$removeSwingPackets(CallbackInfo ci)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        if (isUsingItem())
        {
            ci.cancel();
            mixcesAnimations$swingItem();
        }
    }

    @Unique
    private void mixcesAnimations$swingItem()
    {
        final int armSwingAnimationEnd = mixcesAnimations$getArmSwingAnimationEnd();

        if (!isSwingInProgress || swingProgressInt >= armSwingAnimationEnd / 2 || swingProgressInt < 0)
        {
            swingProgressInt = -1;
            isSwingInProgress = (true);
        }
    }

}
