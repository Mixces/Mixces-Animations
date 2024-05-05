package me.mixces.animations.mixin;

import net.minecraft.entity.player.EntityPlayer;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityMixin {

    @Redirect(
            method = "getEyeHeight",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;isSneaking()Z"
            )
    )
    private boolean mixcesAnimations$getEyeHeight(EntityPlayer instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isSneaking();
    }

    @Inject(
            method = "getEyeHeight",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    private void mixcesAnimations$getEyeHeight2(CallbackInfoReturnable<Float> cir) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            cir.setReturnValue(cir.getReturnValue() - mixcesAnimations$yOffset);
        }
    }

}
