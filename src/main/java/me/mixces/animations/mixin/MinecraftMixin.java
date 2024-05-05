package me.mixces.animations.mixin;

import me.mixces.animations.util.SwingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Redirect(
            method = "sendClickBlockToController",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"
            )
    )
    private boolean mixcesAnimations$sendClickBlockToController(EntityPlayerSP instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isUsingItem();
    }

    @Redirect(
            method = "sendClickBlockToController",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;swingItem()V"
            )
    )
    public void mixcesAnimations$sendClickBlockToController2(EntityPlayerSP instance) {
        if (MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled && instance.isUsingItem()) {
            SwingUtils.swingItem(instance);
        } else {
            instance.swingItem();
        }
    }

    @Redirect(
            method = "rightClickMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"
            )
    )
    private boolean mixcesAnimations$rightClickMouse(PlayerControllerMP instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.getIsHittingBlock();
    }

}
