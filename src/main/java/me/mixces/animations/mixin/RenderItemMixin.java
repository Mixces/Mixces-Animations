package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.init.PotionModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow public abstract void renderItem(ItemStack stack, IBakedModel model);
    @Unique private static final ThreadLocal<ItemStack> mixcesAnimations$stack = ThreadLocal.withInitial(() -> null);

    @ModifyVariable(
            method = "renderItemModelTransform",
            at = @At(
                    value = "HEAD",
                    ordinal = 0
            ),
            index = 1,
            argsOnly = true
    )
    private ItemStack mixcesAnimations$captureStack(ItemStack stack) {
        mixcesAnimations$stack.set(stack);
        return stack;
    }

    @ModifyArg(
            method = "renderItemModelTransform",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"
            ),
            index = 1
    )
    private IBakedModel mixcesAnimations$swapModel(IBakedModel model) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPotion() && MixcesAnimationsConfig.INSTANCE.enabled && mixcesAnimations$stack.get().getItem() instanceof ItemPotion) {
            return PotionModel.BOTTLE_OVERLAY.getBakedModel();
        }
        return model;
    }

    @Inject(
            method = "renderItemModelTransform",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$renderBottle(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPotion() && MixcesAnimationsConfig.INSTANCE.enabled && stack.getItem() instanceof ItemPotion) {
            renderItem(new ItemStack(Items.glass_bottle), mixcesAnimations$getBottleModel(stack));
        } else {
            renderItem(stack, model);
        }
    }

    @Unique
    private IBakedModel mixcesAnimations$getBottleModel(ItemStack stack) {
        return ItemPotion.isSplash(stack.getMetadata()) ? PotionModel.BOTTLE_SPLASH_EMPTY.getBakedModel() : PotionModel.BOTTLE_DRINKABLE_EMPTY.getBakedModel();
    }

}
