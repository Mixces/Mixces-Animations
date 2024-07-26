package me.mixces.animations.mixin;

import net.minecraftforge.client.GuiIngameForge;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(
        value = GuiIngameForge.class,
        remap = (false)
)
public abstract class GuiIngameMixinForge
{

    @ModifyVariable(
            method = "renderHealth",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            ),
            index = 5
    )
    private boolean mixcesAnimations$disableFlashingCheck(boolean value)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldHearts() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return (false);
        }
        return value;
    }

}
