package me.mixces.animations.mixin;

import cc.polyfrost.oneconfig.gui.animations.EaseOutQuad;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow
    private Minecraft mc;

    @Shadow
    private Entity pointedEntity;

//    @Unique
//    private boolean mixcesAnimations$validRange;
//
//    @Unique
//    private Entity mixcesAnimations$validEntity;
//
//    @Inject(
//            method = "updateCameraAndRender",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;setAngles(FF)V",
//                    ordinal = 1
//            )
//    )
//    private void mixcesAnimations$setPitch(float partialTicks, long nanoTime, CallbackInfo ci) {
//        if (MixcesAnimationsConfig.INSTANCE.getAimAssist() && MixcesAnimationsConfig.INSTANCE.enabled) {
//            if (!mixcesAnimations$validRange || mixcesAnimations$validEntity == null) return;
////                EaseOutQuad quad = new EaseOutQuad(150, mc.thePlayer.rotationPitch, 0f, false);
////                mc.thePlayer.rotationPitch = quad.get();
////                mixcesAnimations$validRange = false;
//
//            Entity entity = mc.getRenderViewEntity();
//            float size = mixcesAnimations$validEntity.getCollisionBorderSize();
//            AxisAlignedBB boundingBox = mixcesAnimations$validEntity.getEntityBoundingBox().expand(size, size, size);
//            double posX = entity.posX;
//            double posY = entity.posY + entity.getEyeHeight();
//            double posZ = entity.posZ;
//            Vec3 vec3 = new Vec3(
//                    MathHelper.clamp_double(posX, boundingBox.minX, boundingBox.maxX),
//                    MathHelper.clamp_double(posY, boundingBox.minY, boundingBox.maxY),
//                    MathHelper.clamp_double(posZ, boundingBox.minZ, boundingBox.maxZ)
//            );
//            double x = vec3.xCoord - posX;
//            double y = vec3.yCoord - posY;
//            double z = vec3.zCoord - posZ;
//            double g = MathHelper.sqrt_double(x * x + z * z);
//
//            float yaw = (float) (MathHelper.atan2(z, x) * 180f / Math.PI) /*- 90.0f*/;
//            float pitch = (float) (MathHelper.atan2(y, g) * 180f / Math.PI) /* -1.0f*/;
//
//            EaseOutQuad yawQuad = new EaseOutQuad(150, entity.rotationPitch, yaw, false);
//            EaseOutQuad pitchQuad = new EaseOutQuad(150, entity.rotationPitch, pitch, false);
//
//            entity.rotationYaw = yawQuad.get();
//            entity.rotationPitch = pitchQuad.get();
//        }
//    }
//
//    @Inject(
//            method = "getMouseOver",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.PUTFIELD,
//                    target = "Lnet/minecraft/client/renderer/EntityRenderer;pointedEntity:Lnet/minecraft/entity/Entity;",
//                    ordinal = 0
//            )
//    )
//    private void mixcesAnimations$resetValidEntity(float partialTicks, CallbackInfo ci) {
//        mixcesAnimations$validRange = false;
//        mixcesAnimations$validEntity = null;
//    }
//
//    @Inject(
//            method = "getMouseOver",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.PUTFIELD,
//                    target = "Lnet/minecraft/client/renderer/EntityRenderer;pointedEntity:Lnet/minecraft/entity/Entity;",
//                    ordinal = 4
//            )
//    )
//    private void mixcesAnimations$setValidEntity(float partialTicks, CallbackInfo ci) {
//        mixcesAnimations$validEntity = pointedEntity;
//    }
//
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
//    private void mixcesAnimations$setValidEntity2(float partialTicks, CallbackInfo ci) {
//        ++mc.thePlayer.movementInput.moveForward;
//        ++mc.thePlayer.movementInput.moveStrafe;
//
//        UChat.chat("move tally: " + mc.thePlayer.movementInput.moveForward);
//    }
//
//    @Inject(
//            method = "getMouseOver",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.PUTFIELD,
//                    target = "Lnet/minecraft/client/Minecraft;objectMouseOver:Lnet/minecraft/util/MovingObjectPosition;"
//            ),
//            slice = @Slice(
//                    from = @At(
//                            value = "INVOKE",
//                            target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"
//                    ),
//                    to = @At(
//                            value = "INVOKE",
//                            target = "Lnet/minecraft/profiler/Profiler;endSection()V"
//                    )
//            )
//    )
//    private void mixcesAnimations$setValidRange(float partialTicks, CallbackInfo ci) {
//        mixcesAnimations$validRange = true;
//    }
}
