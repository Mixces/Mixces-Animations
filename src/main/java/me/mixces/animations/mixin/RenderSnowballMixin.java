package me.mixces.animations.mixin;

import me.mixces.animations.handler.SpriteHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderSnowball;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderSnowball.class)
public abstract class RenderSnowballMixin {

//    @Shadow @Final private RenderItem field_177083_e;

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 0
            ),
            index = 0
    )
    private float mixcesAnimations$rotateProjectile(float angle) {
        if (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return 180.0F + angle;
        }
        return angle;
    }

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 1
            ),
            index = 0
    )
    private float mixcesAnimations$useProperCameraView(float angle) {
        return (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled ? -1F : 1F) * angle;
    }

//    @Redirect(
//            method = "doRender",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
//            )
//    )
//    private void simplified$renderDroppedItem(RenderItem instance, ItemStack itemStack, ItemCameraTransforms.TransformType stack) {
//        if (MixcesAnimationsConfig.INSTANCE.getOldProjectiles() && MixcesAnimationsConfig.INSTANCE.enabled) {
//            GlStateManager.scale(2.0f, 2.0f, 2.0f);
//            GlStateManager.translate(0.0f, 0.25f, 0.25f);
//            SpriteHandler.INSTANCE.renderSpriteLayersWithGlint(itemStack, instance.getItemModelMesher().getItemModel(itemStack));
//        } else {
//            field_177083_e.renderItem(itemStack, stack);
//        }
//    }

}
