package me.mixces.animations.util

import net.minecraft.client.renderer.GlStateManager

/**
 * I wrote this for fun :) It's impractical which is why I love it.
 */
object GlHelper {
    fun pitch(pitch: Float): GlHelper {
        GlStateManager.rotate(pitch, 1.0f, 0.0f, 0.0f)
        return this
    }

    fun yaw(yaw: Float): GlHelper {
        GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f)
        return this
    }

    fun roll(roll: Float): GlHelper {
        GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f)
        return this
    }

    fun translate(x: Float, y: Float, z: Float): GlHelper {
        GlStateManager.translate(x, y, z)
        return this
    }

    fun scale(x: Float, y: Float, z: Float): GlHelper {
        GlStateManager.scale(x, y, z)
        return this
    }
}
