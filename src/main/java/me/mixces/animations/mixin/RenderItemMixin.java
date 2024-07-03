package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.GlintModelHook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin {

    @ModifyArg(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    public IBakedModel mixcesAnimations$replaceModel(IBakedModel model) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return GlintModelHook.INSTANCE.getGlint(model);
        }
        return model;
    }

    @ModifyArg(
            method = "renderEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
            ),
            index = 0
    )
    private float mixcesAnimations$modifyF(float x) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled) {
            x *= 64.0F;
        }
        return x;
    }

    @ModifyArgs(
            method = "renderEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"
            )
    )
    public void mixcesAnimations$modifyScale(Args args) {
        if (!MixcesAnimationsConfig.INSTANCE.getOldGlint() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        for (int i : new int[]{0, 1, 2}) {
            args.set(i, 1 / (float) args.get(i));
        }
    }

    @Inject(method = "renderItemModelTransform", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
    public void mixcesAnimations$modifyModelPosition(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.enabled) {
            boolean isRod = stack.getItem().shouldRotateAroundWhenRendering();
            boolean isBlock = stack.getItem() instanceof ItemBlock;
            boolean isCarpet = false;
            if (isBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                isCarpet = block instanceof BlockCarpet || block instanceof BlockSnow;
            }
            if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON) {
                if (isRod) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(50.0F, 0.0F, 0.0F, 1.0F);
                } else if (isCarpet) {
                    GlStateManager.translate(0.0F, -5.25F * 0.0625F, 0.0F);
                }
            } else if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON) {
                if (isRod) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(110.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(0.002F, 0.037F, -0.003F);
                } else if (isCarpet) {
                    GlStateManager.translate(0.0F, -0.25F, 0.0F);
                }
                if (isBlock) {
                    if (Block.getBlockFromItem(stack.getItem()).getRenderType() != 2) {
                        GlStateManager.translate(-0.0285F, -0.0375F, 0.0285F);
                        GlStateManager.rotate(-5.0f, 1.0f, 0.0f, 0.0f);
                        GlStateManager.rotate(-5.0f, 0.0f, 0.0f, 1.0f);
                    }
                    GlStateManager.scale(-1.0F, 1.0F, -1.0F);
                }
            }
        }
    }

}
