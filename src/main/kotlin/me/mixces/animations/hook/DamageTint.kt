@file:JvmName("DamageTint")

package me.mixces.animations.hook

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager

fun unsetDamageTint() {
    GlStateManager.depthFunc(515)
    GlStateManager.disableBlend()
    GlStateManager.enableAlpha()
    GlStateManager.enableTexture2D()
    Minecraft.getMinecraft().entityRenderer.enableLightmap()
}