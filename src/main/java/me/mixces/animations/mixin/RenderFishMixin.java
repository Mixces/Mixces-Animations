package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(RenderFish.class)
public class RenderFishMixin {

    //todo: find a better way of doing this
    @Redirect(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            at = @At(
                    value = "NEW",
                    target = "(DDD)Lnet/minecraft/util/Vec3;"
            )
    )
    private Vec3 mixcesAnimations$modifyLinePosition(double x, double y, double z) {
        if (MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled) {
            /* original values from 1.7 */
            return new Vec3(x - 0.14D, y, z + 0.45D);
        }
        return new Vec3(x, y, z);
    }

    @ModifyConstant(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            constant = @Constant(
                    doubleValue = 0.8F
            )
    )
    public double mixcesAnimations$moveLinePosition(double constant) {
        if (MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled) {
            /* original values from 1.7 */
            constant += 0.05D;
        }
        return constant;
    }

    @Redirect(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;isSneaking()Z"
            )
    )
    public boolean mixcesAnimations$removeSneakTranslation(EntityPlayer instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getPositions() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isSneaking();
    }
}
