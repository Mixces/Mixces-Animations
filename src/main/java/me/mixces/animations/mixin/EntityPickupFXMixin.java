package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPickupFX.class)
public abstract class EntityPickupFXMixin {

    @Shadow private float field_174841_aA;
    @Shadow private Entity field_174843_ax;

    @Inject(
            method = "renderParticle",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/particle/EntityPickupFX;field_174841_aA:F"
            )
    )
    private void mixcesAnimations$factorInEyeHeight(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPickup() && MixcesAnimationsConfig.INSTANCE.enabled) {
            field_174841_aA = field_174843_ax.getEyeHeight() / 2;
        }
    }

}