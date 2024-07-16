package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderEntityItem.class)
public abstract class RenderEntityItemMixin extends Render<EntityItem>
{

    @Unique private static final ThreadLocal<Boolean> mixcesAnimations$isGui3d = ThreadLocal.withInitial(() -> null);

    protected RenderEntityItemMixin(RenderManager renderManager)
    {
        super(renderManager);
    }

    @ModifyVariable(
            method = "func_177077_a",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            index = 12
    )
    private boolean mixcesAnimations$captureIsGui3d(boolean isGui3d)
    {
        mixcesAnimations$isGui3d.set(isGui3d);
        return isGui3d;
    }

    @ModifyArg(
            method = "func_177077_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"
            ),
            index = 0
    )
    private float mixcesAnimations$replaceRotation(float angle)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getFastItems() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return angle;
        }

        if (mixcesAnimations$isGui3d.get())
        {
            return angle;
        }
        return 180.0F - renderManager.playerViewY;
    }

}
