@file:JvmName("GlintHelper")

package me.mixces.animations.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager as Gl /* for fun xD */
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

fun renderEffect(textureManager: TextureManager, glintTexture: ResourceLocation, setupGuiTransform: Runnable) {
    /* renders the gui glint. values taken from 1.7 */
    Gl.enableRescaleNormal()
    Gl.depthFunc(GL11.GL_GEQUAL)
    Gl.disableLighting()
    Gl.depthMask(false)
    textureManager.bindTexture(glintTexture)
    Gl.enableAlpha()
    Gl.alphaFunc(GL11.GL_GREATER, 0.1f)
    Gl.enableBlend()
    Gl.tryBlendFuncSeparate(GL11.GL_DST_ALPHA, GL11.GL_ONE, GL11.GL_ZERO, GL11.GL_ZERO)
    Gl.color(0.5f, 0.25f, 0.8f, 1.0f)

    Gl.pushMatrix()
    setupGuiTransform.run()
    /* this is needed to adapt the glint to the gui */
    GlHelper.translate(-0.25f, -0.25f, -0.25f).scale(0.5f, 0.5f, 0.5f)
    renderFace()
    Gl.popMatrix()

    Gl.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
    Gl.depthMask(true)
    Gl.enableLighting()
    Gl.depthFunc(GL11.GL_LEQUAL)
    Gl.disableAlpha()
    Gl.disableRescaleNormal()
    Gl.disableLighting()
    textureManager.bindTexture(TextureMap.locationBlocksTexture)
}

private fun renderFace() {
    /* drawing the glint in one batch */
    val tessellator = Tessellator.getInstance()
    val worldRenderer = tessellator.worldRenderer
    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
    draw(worldRenderer, ((Minecraft.getSystemTime() % 3000L) / 3000.0f).toDouble(), 0.0625)
    draw(worldRenderer, (((Minecraft.getSystemTime() % 4873L) / 4873.0f) - 0.0625f).toDouble(), 0.0625)
    tessellator.draw()
}

private fun draw(worldRenderer: WorldRenderer, width: Double, height: Double) {
    /* draws the glint. taken from 1.7 */
    worldRenderer.pos(0.0, 0.0, 0.0).tex(width + height * 4.0, height).endVertex()
    worldRenderer.pos(1.0, 0.0, 0.0).tex(width + height * 5.0, height).endVertex()
    worldRenderer.pos(1.0, 1.0, 0.0).tex(width + height, 0.0).endVertex()
    worldRenderer.pos(0.0, 1.0, 0.0).tex(width, 0.0).endVertex()
}
