package me.mixces.animations.mixin.interfaces;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = RendererLivingEntity.class)
public interface RendererLivingEntityMixinInterface
{

    @Invoker boolean invokeSetDoRenderBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks);

    @Invoker void invokeUnsetBrightness();

}