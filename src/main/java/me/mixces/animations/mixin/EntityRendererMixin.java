package me.mixces.animations.mixin;

import cc.polyfrost.oneconfig.gui.animations.EaseOutQuad;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow
    private Minecraft mc;

    @Shadow
    private Entity pointedEntity;

//    @Redirect(
//            method = "updateCameraAndRender",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;setAngles(FF)V"
//            )
//    )
//    private void mixcesAnimations$cancelRotations(EntityPlayerSP instance, float yaw, float pitch) {
//        if (!MixcesAnimationsConfig.INSTANCE.getAimAssist() || !MixcesAnimationsConfig.INSTANCE.enabled || pointedEntity == null) {
//            instance.setAngles(yaw, pitch);
//        }
//    }

    @Inject(
            method = "updateCameraAndRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;setAngles(FF)V"
            )
    )
    private void mixcesAnimations$cancelPitch(float partialTicks, long nanoTime, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getAimAssist() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (pointedEntity != null) {
                float preRotationPitch = mc.thePlayer.rotationPitch;
                EaseOutQuad quad = new EaseOutQuad(150, preRotationPitch, 0.0f, false);
                mc.thePlayer.rotationPitch = quad.get();
            }
        }
    }

//    @Inject(
//            method = "getMouseOver",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.GETFIELD,
//                    target = "Lnet/minecraft/client/renderer/EntityRenderer;pointedEntity:Lnet/minecraft/entity/Entity;",
//                    ordinal = 5,
//                    shift = At.Shift.AFTER
//            )
//    )
//    public void mixcesAnimations$setRotation(float partialTicks, CallbackInfo ci) {
//        if (!MixcesAnimationsConfig.INSTANCE.getAimAssist() || !MixcesAnimationsConfig.INSTANCE.enabled) return;
//        if (pointedEntity == null || !pointedEntity.isEntityAlive()) return;
//        Entity entity = mc.getRenderViewEntity();
//        if (entity.getDistanceToEntity(pointedEntity) <= 1.0) return;
//        float size = pointedEntity.getCollisionBorderSize() + 1.0f;
//        AxisAlignedBB boundingBox = pointedEntity.getEntityBoundingBox().expand(size, size, size);
//        double posX = entity.posX;
//        double posY = entity.posY + entity.getEyeHeight();
//        double posZ = entity.posZ;
//        Vec3 vec3 = new Vec3(
//                MathHelper.clamp_double(posX, boundingBox.minX, boundingBox.maxX),
//                MathHelper.clamp_double(posY, boundingBox.minY, boundingBox.maxY),
//                MathHelper.clamp_double(posZ, boundingBox.minZ, boundingBox.maxZ)
//        );
//        double x = vec3.xCoord - posX;
//        double y = vec3.yCoord - posY;
//        double z = vec3.zCoord - posZ;
//        double g = MathHelper.sqrt_double(x * x + z * z);
//        entity.prevRotationYaw = entity.rotationYaw;
//        entity.prevRotationPitch = entity.rotationPitch;
//        float yaw = (float) (MathHelper.atan2(z, x) * 180f / Math.PI) - 90.0f;
//        float pitch = (float) (MathHelper.atan2(y, g) * 180f / Math.PI) * -1.0f;
////        if (Math.abs(entity.prevRotationYaw - yaw) <= 90) {
//            entity.rotationYaw = yaw;
//            entity.rotationPitch = pitch;
////        }
//    }
}
