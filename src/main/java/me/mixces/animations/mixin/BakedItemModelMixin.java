package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.vecmath.Matrix4f;

@Mixin(
        targets = "net.minecraftforge.client.model.ItemLayerModel$BakedItemModel",
        remap = false
)
public class BakedItemModelMixin {

    @Shadow @Final private boolean isCulled;
    @Shadow @Final private IFlexibleBakedModel otherModel;

    @Inject(
            method = "handlePerspective",
            at = @At(
                    value = "RETURN",
                    ordinal = 2
            ),
            cancellable = true
    )
    private void mixcesAnimations$cullDroppedItemQuadSides(ItemCameraTransforms.TransformType type, CallbackInfoReturnable<Pair<? extends IFlexibleBakedModel, Matrix4f>> cir) {
        if (!MixcesAnimationsConfig.INSTANCE.getFastDropped() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        if (type == ItemCameraTransforms.TransformType.GROUND && !isCulled && cir.getReturnValue().getRight() == null) {
            cir.setReturnValue(Pair.of(otherModel, null));
        } else if (type != ItemCameraTransforms.TransformType.GROUND && isCulled) {
            cir.setReturnValue(Pair.of(otherModel, cir.getReturnValue().getRight()));
        }
    }

}
