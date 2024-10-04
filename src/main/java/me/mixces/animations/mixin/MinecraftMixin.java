package me.mixces.animations.mixin;

import me.mixces.animations.api.ISwing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

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
    public GameSettings gameSettings;

    @Shadow
    protected abstract void clickMouse();

    @Shadow protected abstract void rightClickMouse();

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
            at = @At("HEAD"),
            cancellable = true
    )
    private void mixcesAnimations$addLeftClickCheck(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getHitSelect() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (objectMouseOver == null || objectMouseOver.entityHit == null) {
                if (MixcesAnimationsConfig.INSTANCE.getFakeSwing()) {
                    ((ISwing) thePlayer).fakeSwingItem();
                }
                ci.cancel();
            }
            if (objectMouseOver != null && objectMouseOver.entityHit != null &&
                    objectMouseOver.entityHit instanceof EntityLivingBase && ((EntityLivingBase) objectMouseOver.entityHit).hurtTime > 0) {
                if (MixcesAnimationsConfig.INSTANCE.getFakeSwing()) {
                    ((ISwing) thePlayer).fakeSwingItem();
                }
                ci.cancel();
            }
        }

        if (MixcesAnimationsConfig.INSTANCE.getOldDelay() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && theWorld.isAirBlock(objectMouseOver.getBlockPos())) {
                /* fake swing */
                if (leftClickCounter > 0) ((ISwing) thePlayer).fakeSwingItem();
            } else {
                /* reset leftClickCounter if not clicking at an air block*/
                leftClickCounter = 0;
            }
        }
    }

    @Unique
    private Random mixcesAnimations$random = new Random();

    @Unique
    private long mixcesAnimations$lastClickTime = 0;

    @Inject(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z",
                    ordinal = 0
            )
    )
    private void mixcesAnimations$autoClicker(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getAutoClicker() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (thePlayer.isUsingItem()) return;
            if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                while (gameSettings.keyBindAttack.isPressed()) clickMouse();
            } else {
                if (gameSettings.keyBindAttack.isKeyDown() && mixcesAnimations$shouldClick()) {
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
        return (!MixcesAnimationsConfig.INSTANCE.getAutoClicker() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isPressed();
    }

    @Inject(
            method = "clickMouse",
            at = @At(value = "TAIL")
    )
    private void mixcesAnimations$autoBlocker(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getAutoBlocker() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                if (thePlayer.getHeldItem() != null && thePlayer.getHeldItem().getItem() != null && thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    rightClickMouse();
                }
            }
        }
    }

    @Unique
    private boolean mixcesAnimations$shouldClick() {
        if (System.currentTimeMillis() - mixcesAnimations$lastClickTime >= mixcesAnimations$random.nextInt(83)) {
            mixcesAnimations$lastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
