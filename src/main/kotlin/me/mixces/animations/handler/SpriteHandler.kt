package me.mixces.animations.handler

import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.item.Item
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import java.awt.Color

object SpriteHandler {

    private val RES_ITEM_GLINT = ResourceLocation("textures/misc/enchanted_item_glint.png")
    private val tessellator: Tessellator = Tessellator.getInstance()
    private val worldrenderer: WorldRenderer = Tessellator.getInstance().worldRenderer

    private fun renderSpriteWithLayer(model: IBakedModel, stack: ItemStack, item: Item) {
        val layer0 = model.particleTexture
        val layer1 = getOverlay(model, stack)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL)
        drawSprite(
            layer0.minU.toDouble(),
            layer0.maxU.toDouble(),
            layer0.minV.toDouble(),
            layer0.maxV.toDouble(),
            Color(item.getColorFromItemStack(stack, 0))
        )
        layer1?.let {
            drawSprite(
                it.minU.toDouble(),
                it.maxU.toDouble(),
                it.minV.toDouble(),
                it.maxV.toDouble(),
                Color(item.getColorFromItemStack(stack, 1))
            )
        }
        tessellator.draw()
    }

    private fun drawSprite(uMin: Double, uMax: Double, vMin: Double, vMax: Double, color: Color) {
        worldrenderer.pos(-0.5, -0.25, 0.0) .tex(uMin, vMax).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
        worldrenderer.pos(0.5, -0.25, 0.0)  .tex(uMax, vMax).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
        worldrenderer.pos(0.5, 0.75, 0.0)   .tex(uMax, vMin).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
        worldrenderer.pos(-0.5, 0.75, 0.0)  .tex(uMin, vMin).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
    }

    private fun getOverlay(model: IBakedModel, stack: ItemStack): TextureAtlasSprite? {
        val textureMap = mc.textureMapBlocks
        val updatedSpriteName = when (val spriteName = model.particleTexture.iconName) {
            "minecraft:items/potion_overlay" -> {
                "minecraft:items/potion_bottle" + if (ItemPotion.isSplash(stack.metadata)) "_splash" else "_drinkable"
            }
            "minecraft:items/leather_helmet",
            "minecraft:items/leather_chestplate",
            "minecraft:items/leather_leggings",
            "minecraft:items/leather_boots",
            "minecraft:items/spawn_egg",
            "minecraft:items/fireworks_charge" -> {
                spriteName + "_overlay"
            }
            else -> null
        }

        return updatedSpriteName?.let { textureMap.getAtlasSprite(it) }
    }

    private fun renderHeldItem(var3: Double, var5: Double, var4: Double, var6: Double, var14: Int, var15: Int, color: Color) {
        val var17 = 0.5f * (var3 - var4) / var14
        val var18 = 0.5f * (var6 - var5) / var15

        fun renderFace(facing: EnumFacing, var3: Double, var4: Double, var5: Double, var6: Double, color: Color) {
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL)

            when (facing) {
                EnumFacing.NORTH -> {
                    worldrenderer.pos(0.0, 0.0, 0.0).tex(var3, var6).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, 1.0f).endVertex()
                    worldrenderer.pos(1.0, 0.0, 0.0).tex(var4, var6).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, 1.0f).endVertex()
                    worldrenderer.pos(1.0, 1.0, 0.0).tex(var4, var5).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, 1.0f).endVertex()
                    worldrenderer.pos(0.0, 1.0, 0.0).tex(var3, var5).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, 1.0f).endVertex()
                }
                EnumFacing.SOUTH -> {
                    worldrenderer.pos(0.0, 1.0, -0.0625).tex(var3, var5).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, -1.0f).endVertex()
                    worldrenderer.pos(1.0, 1.0, -0.0625).tex(var4, var5).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, -1.0f).endVertex()
                    worldrenderer.pos(1.0, 0.0, -0.0625).tex(var4, var6).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, -1.0f).endVertex()
                    worldrenderer.pos(0.0, 0.0, -0.0625).tex(var3, var6).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 0.0f, -1.0f).endVertex()
                }
                EnumFacing.WEST -> {
                    for (var19 in 0 until var14) {
                        val var20 = var19 / var14.toDouble()
                        val var21 = var3 + (var4 - var3) * var20 - var17
                        val var22 = var20 + 1.0f / var14

                        worldrenderer.pos(var22, 1.0, -0.0625)  .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var22, 1.0, 0.0)      .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var22, 0.0, 0.0)      .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var22, 0.0, -0.0625)  .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                    }
                }
                EnumFacing.EAST -> {
                    for (var19 in 0 until var14) {
                        val var20 = var19 / var14.toDouble()
                        val var21 = var3 + (var4 - var3) * var20 - var17

                        worldrenderer.pos(var20, 0.0, -0.0625)  .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var20, 0.0, 0.0)      .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var20, 1.0, 0.0)      .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var20, 1.0, -0.0625)  .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                    }
                }
                EnumFacing.UP -> {
                    for (var19 in 0 until var15) {
                        val var20 = var19 / var15.toDouble()
                        val var21 = var6 + (var5 - var6) * var20 - var18
                        val var22 = var20 + 1.0f / var15

                        worldrenderer.pos(0.0, var22, 0.0)      .tex(var3, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                        worldrenderer.pos(1.0, var22, 0.0)      .tex(var4, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                        worldrenderer.pos(1.0, var22, -0.0625)  .tex(var4, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                        worldrenderer.pos(0.0, var22, -0.0625)  .tex(var3, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                    }
                }
                EnumFacing.DOWN -> {
                    for (var19 in 0 until var15) {
                        val var20 = var19 / var15.toDouble()
                        val var21 = var6 + (var5 - var6) * var20 - var18

                        worldrenderer.pos(1.0, var20, 0.0)      .tex(var4, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, -1.0f, 0.0f).endVertex()
                        worldrenderer.pos(0.0, var20, 0.0)      .tex(var3, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, -1.0f, 0.0f).endVertex()
                        worldrenderer.pos(0.0, var20, -0.0625)  .tex(var3, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, -1.0f, 0.0f).endVertex()
                        worldrenderer.pos(1.0, var20, -0.0625)  .tex(var4, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, -1.0f, 0.0f).endVertex()
                    }
                }

                else -> { throw IllegalArgumentException("can't handle z-oriented side") }
            }

            tessellator.draw()
        }

        renderFace(EnumFacing.NORTH, var3, var4, var5, var6, color)
        renderFace(EnumFacing.SOUTH, var3, var4, var5, var6, color)
        renderFace(EnumFacing.WEST, var3, var4, var5, var6, color)
        renderFace(EnumFacing.EAST, var3, var4, var5, var6, color)
        renderFace(EnumFacing.UP, var3, var4, var5, var6, color)
        renderFace(EnumFacing.DOWN, var3, var4, var5, var6, color)
    }

    fun renderHeldItemWithLayer(stack: ItemStack, model: IBakedModel) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.translate(-0.5f, -0.5f, 0.03125f)
        val layer0 = model.particleTexture
        val layer1 = getOverlay(model, stack)
        renderHeldItem(
            layer0.minU.toDouble(),
            layer0.minV.toDouble(),
            layer0.maxU.toDouble(),
            layer0.maxV.toDouble(),
            layer0.iconWidth,
            layer0.iconHeight,
            Color(stack.item.getColorFromItemStack(stack, 0))
        )
        layer1?.let {
            renderHeldItem(
                layer1.minU.toDouble(),
                layer1.minV.toDouble(),
                layer1.maxU.toDouble(),
                layer1.maxV.toDouble(),
                layer1.iconWidth,
                layer1.iconHeight,
                Color(stack.item.getColorFromItemStack(stack, 1))
            )
        }
        if (stack.hasEffect()) {
            renderGlintHeld()
        }
    }

    fun renderSpriteLayersWithGlint(stack: ItemStack, model: IBakedModel) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.translate(0.0f, -0.25f, 0.0f)
        renderSpriteWithLayer(model, stack, stack.item)
        if (stack.hasEffect()) {
            GlStateManager.depthFunc(514)
            GlStateManager.disableLighting()
            GlStateManager.depthMask(false)
            mc.textureManager.bindTexture(RES_ITEM_GLINT)
            GlStateManager.enableAlpha()
            GlStateManager.enableBlend()
            GlStateManager.pushMatrix()
            renderGlintGui(Color(-8372020))
            GlStateManager.popMatrix()
            OpenGlHelper.glBlendFunc(770, 771, 1, 0)
            GlStateManager.depthMask(true)
            GlStateManager.enableLighting()
            GlStateManager.depthFunc(515)
        }
    }

    private fun renderGlintGui(color: Color) {
        val var7 = 0.00390625
        val currentTime = Minecraft.getSystemTime()
        OpenGlHelper.glBlendFunc(772, 1, 0, 0)

        for (var6 in 0..1) {
            val var9 = (currentTime % (3000 + var6 * 1873).toLong()) / (3000.0f + (var6 * 1873).toFloat()) * 256.0f
            val var12 = if ((var6 == 1)) -1.0 else 4.0

            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
            worldrenderer.pos(-0.5, -0.25, 0.0) .tex(((var9 + 20.0f * var12) * var7), (20.0f * var7))           .color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldrenderer.pos(0.5, -0.25, 0.0)  .tex(((var9 + 20.0f + 20.0f * var12) * var7), (20.0f * var7))   .color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldrenderer.pos(0.5, 0.75, 0.0)   .tex(((var9 + 20.0f) * var7), 0.0)                           .color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldrenderer.pos(-0.5, 0.75, 0.0)  .tex(((var9) * var7), 0.0)                                   .color(color.red, color.green, color.blue, color.alpha).endVertex()
            tessellator.draw()
        }
    }

    private fun renderGlintHeld() {
        GlStateManager.depthFunc(514)
        GlStateManager.disableLighting()
        mc.textureManager.bindTexture(RES_ITEM_GLINT)
        GlStateManager.enableBlend()
        OpenGlHelper.glBlendFunc(768, 1, 1, 0)
        GlStateManager.matrixMode(5890)

        GlStateManager.pushMatrix()
        GlStateManager.scale(0.125f, 0.125f, 0.125f)
        val f = (Minecraft.getSystemTime() % 3000L) / 3000.0f * 8.0f
        GlStateManager.translate(f, 0.0f, 0.0f)
        GlStateManager.rotate(-50.0f, 0.0f, 0.0f, 1.0f)
        renderHeldItem(0.0, 0.0, -1.0, 1.0, 16, 16, Color(-10407781))
        GlStateManager.popMatrix()

        GlStateManager.pushMatrix()
        GlStateManager.scale(0.125f, 0.125f, 0.125f)
        val f1 = (Minecraft.getSystemTime() % 4873L) / 4873.0f * 8.0f
        GlStateManager.translate(-f1, 0.0f, 0.0f)
        GlStateManager.rotate(10.0f, 0.0f, 0.0f, 1.0f)
        renderHeldItem(0.0, 0.0, -1.0, 1.0, 16, 16, Color(-10407781))
        GlStateManager.popMatrix()

        GlStateManager.matrixMode(5888)
        GlStateManager.blendFunc(770, 771)
        GlStateManager.disableBlend()
        GlStateManager.enableLighting()
        GlStateManager.depthFunc(515)
    }
    
}
