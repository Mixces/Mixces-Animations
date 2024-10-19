package me.mixces.animations.mixin;

import me.mixces.animations.api.ISwing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovingObjectPosition;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    private int leftClickCounter;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public MovingObjectPosition objectMouseOver;

    @Shadow
    public WorldClient theWorld;

    @Shadow
    @Final
    private static Logger logger;

    @Redirect(
            method = "sendClickBlockToController",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"
            )
    )
    private boolean mixcesAnimations$disableUsingItemCheck(EntityPlayerSP instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isUsingItem();
    }

    @Redirect(
            method = "rightClickMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"
            )
    )
    private boolean mixcesAnimations$disableIsHittingCheck(PlayerControllerMP instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.getIsHittingBlock();
    }

    @Inject(
            method = "clickMouse",
            at = @At("HEAD")
    )
    private void mixcesAnimations$addLeftClickCheck(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldDelay() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (objectMouseOver == null) {
                /* not sure how this occurs */
                logger.error("Null returned as 'hitResult', this shouldn't happen!");
            } else if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && theWorld.isAirBlock(objectMouseOver.getBlockPos())) {
                /* fake swing */
                if (leftClickCounter > 0) ((ISwing) thePlayer).fakeSwingItem();
            } else {
                /* reset leftClickCounter if not clicking at an air block*/
                leftClickCounter = 0;
            }
        }
    }
}
