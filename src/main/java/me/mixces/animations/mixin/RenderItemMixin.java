package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.handler.SpriteHandler;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin {

    @Redirect(
            method = "renderItemModelTransform",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    private void simplified$renderEveryOtherType(RenderItem instance, ItemStack stack, IBakedModel model, ItemStack stack2, IBakedModel model2, ItemCameraTransforms. TransformType cameraTransformType) {
        if (MixcesAnimationsConfig.INSTANCE.getOldRender() && MixcesAnimationsConfig.INSTANCE.enabled && !model.isGui3d()) {
            SpriteHandler.INSTANCE.renderHeldItemWithLayer(stack, model);
        } else {
            instance.renderItem(stack, model);
        }
    }

    @Redirect(
            method = "renderItemIntoGUI",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    private void simplified$renderGuiItem(RenderItem instance, ItemStack stack, IBakedModel model) {
        if (MixcesAnimationsConfig.INSTANCE.getOldRender() && MixcesAnimationsConfig.INSTANCE.enabled && !model.isGui3d()) {
            SpriteHandler.INSTANCE.renderSpriteLayersWithGlint(stack, model);
        } else {
            instance.renderItem(stack, model);
        }
    }

}
