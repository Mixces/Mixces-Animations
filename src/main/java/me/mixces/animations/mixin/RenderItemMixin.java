package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.entity.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.awt.*;

@Mixin(RenderItem.class)
public class RenderItemMixin {

    @ModifyArg(
            method = "renderEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;I)V"
            ),
            index = 1
    )
    private int mixcesAnimations$modifyEffectColor(int original) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled) {
            Color color = new Color(0.38F, 0.19F, 0.608F);
            return color.getRGB();
        }
        return original;
    }

}
