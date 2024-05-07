package me.mixces.animations.mixin;

import net.minecraftforge.client.GuiIngameForge;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(
        value = GuiIngameForge.class,
        remap = false
)
public class GuiIngameMixinForge {

    @ModifyVariable(
            method = "renderHealth",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "intValue=9",
                            ordinal = 2
                    ),
                    to = @At(
                            value = "CONSTANT",
                            args = "intValue=2",
                            ordinal = 4
                    )
            )
    )
    private boolean mixcesAnimations$renderHealth(boolean value) {
        return (!MixcesAnimationsConfig.INSTANCE.getOldHearts() || !MixcesAnimationsConfig.INSTANCE.enabled) && value;
    }

}
