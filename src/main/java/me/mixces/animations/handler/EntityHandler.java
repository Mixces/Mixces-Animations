package me.mixces.animations.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;

import java.util.List;

public class EntityHandler {

    public static boolean getMouseOver(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        WorldClient world = mc.theWorld;
        MovingObjectPosition objectMouseOver;
        if (entity != null && world != null) {
            double d0 = mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            if (mc.playerController.extendedReach()) {
                d0 = 6.0D;
                d1 = 6.0D;
            } else {
                if (d0 > 3.0D) d1 = 3.0D;
                d0 = d1;
            }
            if (objectMouseOver != null) d1 = objectMouseOver.hitVec.distanceTo(vec3);
            Vec3 vec31 = entity.getLook(partialTicks);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            float f1 = 1.0F;
            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f1, f1, f1));
            double d2 = d1;
            for (Entity entity1 : list) {
                if (entity1.canBeCollidedWith()) {
                    float f2 = entity1.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f2, f2, f2);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                    if (axisalignedbb.isVecInside(vec3)) {
                        if (0.0D < d2 || d2 == 0.0D) d2 = 0.0D;
                    } else if (movingobjectposition != null) {
                        double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                        if ((d3 < d2 || d2 == 0.0D) && (entity1 != entity1.ridingEntity || entity1.canRiderInteract())) d2 = d3;
                    }
                }
            }
            return d2 < d1 || objectMouseOver == null;
        }
        return false;
    }
}
