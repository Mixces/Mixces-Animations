package me.mixces.animations.mixin;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.SprintReset;
import me.mixces.animations.util.DistanceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//todo: unfuck?
@Mixin(MovementInputFromOptions.class)
public class MovementInputFromOptionsMixin extends MovementInput {

    @Shadow
    @Final
    private GameSettings gameSettings;

    @Unique
    public boolean mixcesAnimations$pressingForward;

    @Unique
    public boolean mixcesAnimations$pressingBack;

    @Unique
    public boolean mixcesAnimations$pressingLeft;

    @Unique
    public boolean mixcesAnimations$pressingRight;

    @Unique
    private boolean mixcesAnimations$prevForward;

    @Unique
    private boolean mixcesAnimations$prevStrafe;

    @Inject(
            method = "updatePlayerMoveState",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/util/MovementInputFromOptions;gameSettings:Lnet/minecraft/client/settings/GameSettings;",
                    ordinal = 4
            )
    )
    private void mixcesAnimations$doSpace(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getAutoSpace() && MixcesAnimationsConfig.INSTANCE.enabled) {
            Minecraft mc = Minecraft.getMinecraft();
            Entity enemy = mc.objectMouseOver.entityHit;
            if (gameSettings.keyBindBack.isKeyDown()) return;
            if (mc.objectMouseOver == null || enemy == null) return;
            if (((EntityLivingBase) enemy).hurtTime <= 0) return;
            double distance = DistanceHelper.distance(enemy);
            UChat.chat(distance);

            if (distance <= 3.0) {
                moveForward = 0.0f;
            }
//            moveForward = (float) distance - 3.0f;
        }
    }

    @Inject(
            method = "updatePlayerMoveState",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/util/MovementInputFromOptions;gameSettings:Lnet/minecraft/client/settings/GameSettings;",
                    ordinal = 5
            )
    )
    private void mixcesAnimations$doJump(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getJumpReset() && MixcesAnimationsConfig.INSTANCE.enabled) {
            jump = gameSettings.keyBindJump.isKeyDown() || SprintReset.getShouldJump();
            if (SprintReset.getShouldJump()) SprintReset.setShouldJump(false);
        }
    }

    @Inject(
            method = "updatePlayerMoveState",
            at = @At("HEAD")
    )
    private void mixcesAnimations$setKeyStateFields(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getNullMovement() && MixcesAnimationsConfig.INSTANCE.enabled) {
            mixcesAnimations$pressingForward = gameSettings.keyBindForward.isKeyDown();
            mixcesAnimations$pressingBack = gameSettings.keyBindBack.isKeyDown();
            mixcesAnimations$pressingLeft = gameSettings.keyBindLeft.isKeyDown();
            mixcesAnimations$pressingRight = gameSettings.keyBindRight.isKeyDown();
        }
    }

    @Inject(
            method = "updatePlayerMoveState",
            at = @At("TAIL")
    )
    private void mixcesAnimations$nullMovement(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getNullMovement() && MixcesAnimationsConfig.INSTANCE.enabled) {
            mixcesAnimations$prevForward = mixcesAnimations$pressingForward && !mixcesAnimations$pressingBack || ((mixcesAnimations$pressingForward || !mixcesAnimations$pressingBack) && mixcesAnimations$prevForward);
            mixcesAnimations$prevStrafe = mixcesAnimations$pressingLeft && !mixcesAnimations$pressingRight || ((mixcesAnimations$pressingLeft || !mixcesAnimations$pressingRight) && mixcesAnimations$prevStrafe);
            if (mixcesAnimations$pressingForward && mixcesAnimations$pressingBack) mixcesAnimations$move(true, mixcesAnimations$prevForward);
            if (mixcesAnimations$pressingLeft && mixcesAnimations$pressingRight) mixcesAnimations$move(false, mixcesAnimations$prevStrafe);
        }
    }

    @Unique
    private void mixcesAnimations$move(boolean forward, boolean multiplier) {
        float movement = multiplier ? -1 : 1;
        if (sneak) movement *= 0.3f;
        if (forward) moveForward = movement;
        else moveStrafe = movement;
    }
}
