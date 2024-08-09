package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.GlintModelHook;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @ModifyArg(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    public IBakedModel mixcesAnimations$replaceModel(IBakedModel model) {
        return MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled ? GlintModelHook.INSTANCE.getGlint(model) : model;
    }

    @ModifyArg(
            method = "renderModel(Lnet/minecraft/client/resources/model/IBakedModel;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V"
            ),
            index = 1
    )
    public int mixcesAnimations$replaceColor(int color) {
        return MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled ? -10407781 : color;
    }

    @ModifyConstant(
            method = "renderEffect",
            constant = @Constant(
                    floatValue = 8.0F
            )
    )
    public float mixcesAnimations$modifyScale(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled) {
            original = 1.0F / original;
        }
        return original;
    }
}
