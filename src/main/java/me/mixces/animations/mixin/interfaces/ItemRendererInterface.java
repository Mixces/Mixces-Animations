package me.mixces.animations.mixin.interfaces;

import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemRenderer.class)
public interface ItemRendererInterface
{

    @Accessor int getEquippedItemSlot();

}
