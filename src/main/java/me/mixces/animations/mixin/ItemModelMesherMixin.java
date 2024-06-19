package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.SkullModelHook;
import me.mixces.animations.init.CustomModelBakery;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelMesher.class)
public class ItemModelMesherMixin {

    @Inject(
            method = "getItemModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/resources/model/IBakedModel;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void mixcesAnimations$useCustomBlockModel(ItemStack stack, CallbackInfoReturnable<IBakedModel> cir) {
        if (stack == null) return;
        if (!MixcesAnimationsConfig.INSTANCE.enabled) return;
        if (MixcesAnimationsConfig.INSTANCE.getOldSkulls() && stack.getItem() instanceof ItemSkull) {
            cir.setReturnValue(SkullModelHook.INSTANCE.getSkullModel(stack));
        }
        if (MixcesAnimationsConfig.INSTANCE.getFastGrass() && stack.getItem() instanceof ItemBlock &&
                ((ItemBlock) stack.getItem()).getBlock() == Blocks.grass) {
            cir.setReturnValue(CustomModelBakery.LAYERLESS_GRASS.getBakedModel());
        }
    }

}