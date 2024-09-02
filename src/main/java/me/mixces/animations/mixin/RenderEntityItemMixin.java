package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderEntityItem.class)
public abstract class RenderEntityItemMixin extends Render<EntityItem> {

    @Unique
    private static final ThreadLocal<Boolean> mixcesAnimations$isGui3d = ThreadLocal.withInitial(() -> null);

    protected RenderEntityItemMixin(RenderManager renderManager) {
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
    private boolean mixcesAnimations$captureIsGui3d(boolean isGui3d) {
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
    private float mixcesAnimations$replaceRotation(float angle) {
        if (MixcesAnimationsConfig.INSTANCE.getFastItems() && MixcesAnimationsConfig.INSTANCE.enabled && !mixcesAnimations$isGui3d.get()) {
            return 180.0F - renderManager.playerViewY;
        }
        return angle;
    }

    @Inject(
            method = "func_177077_a",
            at = @At("TAIL")
    )
    private void mixcesAnimations$clearThreadLocalIsGui3d(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_, CallbackInfoReturnable<Integer> cir) {
        /* we need to clear the threadlocal */
        mixcesAnimations$isGui3d.remove();
    }

    @ModifyArg(
            method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/ForgeHooksClient;handleCameraTransforms(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)Lnet/minecraft/client/resources/model/IBakedModel;"
            ),
            index = 1
    )
    private ItemCameraTransforms.TransformType mixcesAnimations$replaceTransform(ItemCameraTransforms.TransformType cameraTransformType) {
        return MixcesAnimationsConfig.INSTANCE.getFastItems() && MixcesAnimationsConfig.INSTANCE.enabled ? ItemCameraTransforms.TransformType.GUI : cameraTransformType;
    }
}
