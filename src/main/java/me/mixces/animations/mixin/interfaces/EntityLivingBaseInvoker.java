package me.mixces.animations.mixin.interfaces;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EntityLivingBase.class)
public interface EntityLivingBaseInvoker {

    @Invoker int invokeGetArmSwingAnimationEnd();

}
