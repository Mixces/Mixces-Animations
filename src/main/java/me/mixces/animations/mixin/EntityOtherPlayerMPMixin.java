package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityOtherPlayerMP.class)
public abstract class EntityOtherPlayerMPMixin extends EntityLivingBaseMixin {

    @Inject(
            method = "attackEntityFrom",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mixcesAnimations$disableInvulnerability(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (MixcesAnimationsConfig.INSTANCE.getDisableInvulnerable() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (!Minecraft.getMinecraft().isSingleplayer()) {
                hurtResistantTime = 0;
                cir.setReturnValue(false);
                //todo: test if this does anything
            }
        }
    }
}
