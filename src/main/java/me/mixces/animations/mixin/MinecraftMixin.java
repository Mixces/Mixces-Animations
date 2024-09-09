package me.mixces.animations.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public EntityRenderer entityRenderer;

    @Shadow
    private Timer timer;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public MovingObjectPosition objectMouseOver;

    @Shadow
    public PlayerControllerMP playerController;

    @Shadow
    public WorldClient theWorld;

    @Shadow protected abstract void clickMouse();

    @Shadow private int leftClickCounter;

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
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z",
                    ordinal = 0
            )
    )
    private void mixcesAnimations$addLeftClickCheck(CallbackInfo ci) {
//        if (MixcesAnimationsConfig.INSTANCE.getUpdateMouse() && MixcesAnimationsConfig.INSTANCE.enabled) {
//            if (thePlayer.isUsingItem()) return;
//            entityRenderer.getMouseOver(timer.renderPartialTicks);
//            if (objectMouseOver == null) mixcesAnimations$hitResult(null);
//            else {
//                switch (objectMouseOver.typeOfHit) {
//                    case ENTITY:
//                        mixcesAnimations$hitResult(() -> playerController.attackEntity(thePlayer, objectMouseOver.entityHit));
//                        break;
//                    case BLOCK:
//                        BlockPos blockpos = objectMouseOver.getBlockPos();
//                        mixcesAnimations$hitResult(() -> {
//                            if (!theWorld.isAirBlock(blockpos)) playerController.clickBlock(blockpos, objectMouseOver.sideHit);
//                        });
//                        break;
//                    case MISS:
//                    default:
//                        mixcesAnimations$hitResult(null);
//                }
//            }
//        }

        //todo: autoclicker
        if (MixcesAnimationsConfig.INSTANCE.getUpdateMouse() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (thePlayer.isUsingItem()) return;
            if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                while (gameSettings.keyBindAttack.isPressed()) clickMouse();
            } else {
                if (gameSettings.keyBindAttack.isKeyDown()) {
                    leftClickCounter = 0; /* yeah */
                    clickMouse();
                }
            }
        }
    }

    @Redirect(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z",
                    ordinal = 10
            )
    )
    private boolean mixcesAnimations$disableClickMouse(KeyBinding instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getUpdateMouse() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isPressed();
    }
    
//    @Unique
//    private void mixcesAnimations$hitResult(Runnable action) {
//        while (gameSettings.keyBindAttack.isPressed()) {
//            thePlayer.swingItem();
//            if (action == null) return;
//            action.run();
//        }
//    }
}
