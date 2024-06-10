package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.handler.SpriteHandler;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderEntityItem.class)
public abstract class RenderEntityItemMixin extends Render<EntityItem> {

    @Unique private static final ThreadLocal<Boolean> mixcesAnimations$flag = ThreadLocal.withInitial(() -> null);

    protected RenderEntityItemMixin(RenderManager renderManager) {
        super(renderManager);
    }

    @ModifyVariable(
            method = "func_177077_a",
            at = @At(
                    value = "STORE"
            ),
            index = 12
    )
    private boolean mixcesAnimations$captureF1(boolean flag) {
        mixcesAnimations$flag.set(flag);
        return flag;
    }

    @ModifyArg(
            method = "func_177077_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"
            ),
            index = 0
    )
    private float mixcesAnimations$itemFacePlayer(float angle) {
        if (MixcesAnimationsConfig.INSTANCE.getOldRender() && MixcesAnimationsConfig.INSTANCE.enabled && !mixcesAnimations$flag.get()) {
            return 180.0F - renderManager.playerViewY;
        }
        return angle;
    }

    @Redirect(
            method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    private void simplified$renderDroppedItem(RenderItem instance, ItemStack stack, IBakedModel model) {
        if (MixcesAnimationsConfig.INSTANCE.getOldRender() && MixcesAnimationsConfig.INSTANCE.enabled && !model.isGui3d()) {
            SpriteHandler.INSTANCE.renderSpriteLayersWithGlint(stack, model);
        } else {
            instance.renderItem(stack, model);
        }
    }

}
