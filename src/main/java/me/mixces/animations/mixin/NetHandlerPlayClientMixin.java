package me.mixces.animations.mixin;

import net.minecraft.client.network.NetHandlerPlayClient;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {

    @ModifyArg(
            method = "handleCollectItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/EntityPickupFX;<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;F)V"
            ),
            index = 3
    )
    private float mixcesAnimations$handleCollectItem(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPickup() && MixcesAnimationsConfig.INSTANCE.enabled) {
            original += 0.5F;
        }
        return original;
    }

}