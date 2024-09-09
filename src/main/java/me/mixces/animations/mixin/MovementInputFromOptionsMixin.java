package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.handler.EntityHandler;
import me.mixces.animations.hook.SprintReset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MovementInputFromOptionsMixin extends MovementInput {

    @Shadow
    @Final
    private GameSettings gameSettings;

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
            if (EntityHandler.getMouseOver(1.0f) && Minecraft.getMinecraft().getRenderViewEntity().onGround && !gameSettings.keyBindBack.isKeyDown()) {
                moveForward = 0.0f;
                moveStrafe = 0.0f;
            }
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
            if (SprintReset.getShouldJump()) {
                SprintReset.setShouldJump(false);
            }
        }
    }
}
