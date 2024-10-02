package me.mixces.animations.mixin.access;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface IKeyBindingMixin {

    @Accessor
    void setPressTime(int time);
}
