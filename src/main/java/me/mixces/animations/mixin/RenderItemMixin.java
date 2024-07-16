package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.GlintModelHook;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin
{

    @ModifyArg(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    public IBakedModel mixcesAnimations$replaceModel(IBakedModel model)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
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
    private float mixcesAnimations$modifyF(float x)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
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
    public void mixcesAnimations$modifyScale(Args args)
    {
        if (!MixcesAnimationsConfig.INSTANCE.getOldGlint() || !MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return;
        }

        for (int i : new int[]{0, 1, 2})
        {
            args.set(i, 1 / (float) args.get(i));
        }
    }

}
