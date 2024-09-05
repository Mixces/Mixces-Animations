package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.SprintReset;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MovementInputFromOptionsMixin extends MovementInput {

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
            jump = SprintReset.getShouldJump();
            if (SprintReset.getShouldJump()) SprintReset.setShouldJump(false);
        }
    }
}
