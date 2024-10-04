package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow
    private Minecraft mc;

    @Shadow
    private Entity pointedEntity;

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

    @Inject(
        method = "getMouseOver",
        at = @At(
                value = "FIELD",
                opcode = Opcodes.GETFIELD,
                target = "Lnet/minecraft/client/renderer/EntityRenderer;pointedEntity:Lnet/minecraft/entity/Entity;",
                ordinal = 5,
                shift = At.Shift.AFTER
        )
    )
    public void mixcesAnimations$setRotation(float partialTicks, CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getAimAssist() || !MixcesAnimationsConfig.INSTANCE.enabled) return;
        if (pointedEntity == null || !pointedEntity.isEntityAlive()) return;
        Entity entity = mc.getRenderViewEntity();
        float size = pointedEntity.getCollisionBorderSize();
        AxisAlignedBB boundingBox = pointedEntity.getEntityBoundingBox().expand(size, size, size);
        double posX = entity.posX;
        double posY = entity.posY + entity.getEyeHeight();
        double posZ = entity.posZ;
        Vec3 vec3 = new Vec3(
                MathHelper.clamp_double(posX, boundingBox.minX, boundingBox.maxX),
                MathHelper.clamp_double(posY, boundingBox.minY, boundingBox.maxY),
                MathHelper.clamp_double(posZ, boundingBox.minZ, boundingBox.maxZ)
        );
        double x = vec3.xCoord - posX;
        double y = vec3.yCoord - posY;
        double z = vec3.zCoord - posZ;
        double g = MathHelper.sqrt_double(x * x + z * z);
        entity.rotationYaw = (float) (MathHelper.atan2(z, x) * 180f / Math.PI) - 90.0f;
        entity.rotationPitch = (float) (MathHelper.atan2(y, g) * 180f / Math.PI) * -1.0f;
    }
}
