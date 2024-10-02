package me.mixces.animations.mixin;

import net.minecraft.entity.player.EntityPlayer;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBaseMixin {

    @Shadow
    public abstract boolean isUsingItem();

    @Redirect(
            method = "getEyeHeight",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;isSneaking()Z"
            )
    )
    private boolean mixcesAnimations$disableSneakTranslation(EntityPlayer instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) && isSneaking();
    }
}
