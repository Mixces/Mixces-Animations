package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.init.PotionComponents;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow public abstract void renderItem(ItemStack stack, IBakedModel model);
    @Shadow public abstract ItemModelMesher getItemModelMesher();

    @Redirect(
            method = "renderItemModelTransform",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    protected void mixcesAnimations$renderPotion(RenderItem instance, ItemStack stack, IBakedModel model) {
        if (MixcesAnimationsConfig.INSTANCE.getOldPotion() && MixcesAnimationsConfig.INSTANCE.enabled && stack.getItem() instanceof ItemPotion) {
            renderItem(stack, PotionComponents.getPotionOverlayModel(getItemModelMesher()));
            renderItem(PotionComponents.getPotionBottleStack(stack), PotionComponents.getPotionBottleModel(getItemModelMesher(), stack));
        } else {
            renderItem(stack, model);
        }
    }

}
