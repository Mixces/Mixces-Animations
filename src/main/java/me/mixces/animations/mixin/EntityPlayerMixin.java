package me.mixces.animations.mixin;

import net.minecraft.entity.player.EntityPlayer;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBaseMixin
{

    @Shadow public abstract boolean isUsingItem();
    @Unique private static final ThreadLocal<Float> mixcesAnimations$f1 = ThreadLocal.withInitial(() -> 1.62F);

    @ModifyVariable(
            method = "getEyeHeight",
            at = @At(
                    value = "STORE"
            ),
            index = 1
    )
    private float mixcesAnimations$captureF(float f)
    {
        mixcesAnimations$f1.set(f);
        return f;
    }

    @Inject(
            method = "getEyeHeight",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;isSneaking()Z"
            ),
            cancellable = true
    )
    private void mixcesAnimations$movePlayerCamera(CallbackInfoReturnable<Float> cir)
    {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            cir.setReturnValue(mixcesAnimations$f1.get() - mixcesAnimations$yOffset);
        }
    }

}
