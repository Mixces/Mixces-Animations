package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.RenderItemHook;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow public abstract void renderItem(ItemStack stack, IBakedModel model);
    @Shadow public abstract ItemModelMesher getItemModelMesher();

//    @Redirect(
//            method = "renderItemModelTransform",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraftforge/client/ForgeHooksClient;handleCameraTransforms(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)Lnet/minecraft/client/resources/model/IBakedModel;"
//            )
//    )
//    protected IBakedModel mixcesAnimations$disableTransforms(IBakedModel pair, ItemCameraTransforms.TransformType model, ItemStack stack, IBakedModel model2, ItemCameraTransforms. TransformType cameraTransformType) {
//        if (MixcesAnimationsConfig.INSTANCE.getOldPotion() && MixcesAnimationsConfig.INSTANCE.enabled && stack.getItem() instanceof ItemPotion) {
//            return null;
//        }
//        return ForgeHooksClient.handleCameraTransforms(pair, model);
//    }

    @Redirect(
            method = "renderItemModelTransform",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    protected void mixcesAnimations$renderPotion(RenderItem instance, ItemStack stack, IBakedModel model, ItemStack stack2, IBakedModel model2, ItemCameraTransforms. TransformType cameraTransformType) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPotion() && MixcesAnimationsConfig.INSTANCE.enabled && stack.getItem() instanceof ItemPotion) {
            GlStateManager.pushMatrix();
            renderItem(stack, ForgeHooksClient.handleCameraTransforms(RenderItemHook.getPotionOverlayModel(getItemModelMesher()), cameraTransformType));
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            renderItem(RenderItemHook.getPotionBottleStack(stack), ForgeHooksClient.handleCameraTransforms(RenderItemHook.getPotionBottleModel(getItemModelMesher(), stack), cameraTransformType));
            GlStateManager.popMatrix();
        } else {
            renderItem(stack, model);
        }
    }

}
