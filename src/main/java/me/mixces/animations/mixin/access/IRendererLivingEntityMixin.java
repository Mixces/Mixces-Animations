package me.mixces.animations.mixin.access;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RendererLivingEntity.class)
public interface IRendererLivingEntityMixin {

    @Invoker
    boolean invokeSetDoRenderBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks);

    @Invoker
    void invokeUnsetBrightness();
}