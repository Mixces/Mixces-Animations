package me.mixces.animations.util

import net.minecraft.client.renderer.GlStateManager

object GlHelper
{

    fun pitch(pitch: Float): GlHelper
    {
        GlStateManager.rotate(pitch, 1.0f, 0.0f, 0.0f)
        return (this)
    }

    fun yaw(yaw: Float): GlHelper
    {
        GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f)
        return (this)
    }

    fun roll(roll: Float): GlHelper
    {
        GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f)
        return (this)
    }

    fun scale(scale: Float)
    {
        GlStateManager.scale(scale, scale, scale)
    }

}
