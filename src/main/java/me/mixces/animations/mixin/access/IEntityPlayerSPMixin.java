package me.mixces.animations.mixin.access;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public interface IEntityPlayerSPMixin {

    @Accessor
    void setServerSprintState(boolean sprintState);
}
