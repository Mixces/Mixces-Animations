package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.MovingObjectPosition;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Redirect(
            method = "getMouseOver",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/Minecraft;objectMouseOver:Lnet/minecraft/util/MovingObjectPosition;",
                    ordinal = 0
            )
    )
    private MovingObjectPosition mixcesAnimations$fixReach(Minecraft instance) {
        MovingObjectPosition original = instance.objectMouseOver;
        if (MixcesAnimationsConfig.INSTANCE.getFixReach() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return original != null && instance.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.MISS ? original : null;
        }
        return original;
    }

    @Inject(
            method = "hurtCameraEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mixcesAnimations$cancelHurtCam(float partialTicks, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getNoHurtCam() && MixcesAnimationsConfig.INSTANCE.enabled) {
            ci.cancel();
        }
    }
}
