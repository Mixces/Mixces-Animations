package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.init.PotionComponents;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow @Final private TextureManager textureManager;
    @Shadow public abstract void renderItem(ItemStack stack, IBakedModel model);
    @Shadow public abstract ItemModelMesher getItemModelMesher();
    @Shadow protected abstract void preTransform(ItemStack stack);

    @Inject(
            method = "renderItemModelTransform",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    protected void mixcesAnimations$renderPotion(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPotion() && MixcesAnimationsConfig.INSTANCE.enabled && stack.getItem() instanceof ItemPotion) {
            ci.cancel();
            textureManager.bindTexture(TextureMap.locationBlocksTexture);
            textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
            preTransform(stack);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            renderItem(stack, ForgeHooksClient.handleCameraTransforms(PotionComponents.getPotionOverlayModel(getItemModelMesher()), cameraTransformType));
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            renderItem(PotionComponents.getPotionBottleStack(stack), ForgeHooksClient.handleCameraTransforms(PotionComponents.getPotionBottleModel(getItemModelMesher(), stack), cameraTransformType));
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            textureManager.bindTexture(TextureMap.locationBlocksTexture);
            textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        }
    }

}
