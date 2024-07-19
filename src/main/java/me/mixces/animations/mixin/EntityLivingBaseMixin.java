package me.mixces.animations.mixin;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends EntityMixin
{

    @Shadow protected abstract int getArmSwingAnimationEnd();

    @Unique
    public int mixcesAnimations$getArmSwingAnimationEnd()
    {
        return getArmSwingAnimationEnd();
    }

}
