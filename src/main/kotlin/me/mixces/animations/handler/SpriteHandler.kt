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

    private fun renderHeldItem(sprite: TextureAtlasSprite, color: Color) {
        val var17 = 0.5f * (sprite.minU.toDouble() - sprite.maxU.toDouble()) / sprite.iconWidth
        val var18 = 0.5f * (sprite.maxV.toDouble() - sprite.minV.toDouble()) / sprite.iconHeight

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
                    for (var19 in 0 until sprite.iconWidth) {
                        val var20 = var19 / sprite.iconWidth.toDouble()
                        val var21 = var3 + (var4 - var3) * var20 - var17
                        val var22 = var20 + 1.0f / sprite.iconWidth

                        worldrenderer.pos(var22, 1.0, -0.0625)  .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var22, 1.0, 0.0)      .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var22, 0.0, 0.0)      .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var22, 0.0, -0.0625)  .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(1.0f, 0.0f, 0.0f).endVertex()
                    }
                }
                EnumFacing.EAST -> {
                    for (var19 in 0 until sprite.iconWidth) {
                        val var20 = var19 / sprite.iconWidth.toDouble()
                        val var21 = var3 + (var4 - var3) * var20 - var17

                        worldrenderer.pos(var20, 0.0, -0.0625)  .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var20, 0.0, 0.0)      .tex(var21, var6).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var20, 1.0, 0.0)      .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                        worldrenderer.pos(var20, 1.0, -0.0625)  .tex(var21, var5).color(color.red, color.green, color.blue, color.alpha).normal(-1.0f, 0.0f, 0.0f).endVertex()
                    }
                }
                EnumFacing.UP -> {
                    for (var19 in 0 until sprite.iconHeight) {
                        val var20 = var19 / sprite.iconHeight.toDouble()
                        val var21 = var6 + (var5 - var6) * var20 - var18
                        val var22 = var20 + 1.0f / sprite.iconHeight

                        worldrenderer.pos(0.0, var22, 0.0)      .tex(var3, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                        worldrenderer.pos(1.0, var22, 0.0)      .tex(var4, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                        worldrenderer.pos(1.0, var22, -0.0625)  .tex(var4, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                        worldrenderer.pos(0.0, var22, -0.0625)  .tex(var3, var21).color(color.red, color.green, color.blue, color.alpha).normal(0.0f, 1.0f, 0.0f).endVertex()
                    }
                }
                EnumFacing.DOWN -> {
                    for (var19 in 0 until sprite.iconHeight) {
                        val var20 = var19 / sprite.iconHeight.toDouble()
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

        renderFace(EnumFacing.NORTH, sprite.minU.toDouble(), sprite.maxU.toDouble(), sprite.minV.toDouble(), sprite.maxV.toDouble(), color)
        renderFace(EnumFacing.SOUTH, sprite.minU.toDouble(), sprite.maxU.toDouble(), sprite.minV.toDouble(), sprite.maxV.toDouble(), color)
        renderFace(EnumFacing.WEST, sprite.minU.toDouble(), sprite.maxU.toDouble(), sprite.minV.toDouble(), sprite.maxV.toDouble(), color)
        renderFace(EnumFacing.EAST, sprite.minU.toDouble(), sprite.maxU.toDouble(), sprite.minV.toDouble(), sprite.maxV.toDouble(), color)
        renderFace(EnumFacing.UP, sprite.minU.toDouble(), sprite.maxU.toDouble(), sprite.minV.toDouble(), sprite.maxV.toDouble(), color)
        renderFace(EnumFacing.DOWN, sprite.minU.toDouble(), sprite.maxU.toDouble(), sprite.minV.toDouble(), sprite.maxV.toDouble(), color)

//        for (v in 0 until sprite.iconWidth) {
//            for (u in 0 until sprite.iconHeight) {
//                buildSideQuad(EnumFacing.WEST, sprite, u, v, color)
//                buildSideQuad(EnumFacing.EAST, sprite, u, v, color)
//                buildSideQuad(EnumFacing.UP, sprite, u, v, color)
//                buildSideQuad(EnumFacing.NORTH, sprite, u, v, color)
//            }
//        }
    }

//    private fun buildSideQuad(side: EnumFacing, sprite: TextureAtlasSprite, u: Int, v: Int, color: Color) {
//        val eps0 = 30e-5f
//        val eps1 = 45e-5f
//        val eps2 = .5f
//        val eps3 = .5f
//        var x0 = u.toFloat() / sprite.iconWidth
//        var y0 = v.toFloat() / sprite.iconHeight
//        var x1 = x0
//        var y1 = y0
//        var z1 = 7.5f / 16f - eps1
//        var z2 = 8.5f / 16f + eps1
//        when (side) {
//            EnumFacing.WEST -> {
//                z1 = 8.5f / 16f + eps1
//                z2 = 7.5f / 16f - eps1
//                y1 = (v + 1f) / sprite.iconHeight
//            }
//
//            EnumFacing.EAST -> y1 = (v + 1f) / sprite.iconHeight
//            EnumFacing.DOWN -> {
//                z1 = 8.5f / 16f + eps1
//                z2 = 7.5f / 16f - eps1
//                x1 = (u + 1f) / sprite.iconWidth
//            }
//
//            EnumFacing.UP -> x1 = (u + 1f) / sprite.iconWidth
//            else -> throw java.lang.IllegalArgumentException("can't handle z-oriented side")
//        }
//        var u0 = 16f * (x0 - side.directionVec.x * eps3 / sprite.iconWidth)
//        var u1 = 16f * (x1 - side.directionVec.x * eps3 / sprite.iconWidth)
//        var v0 = 16f * (1f - y0 - side.directionVec.y * eps3 / sprite.iconHeight)
//        var v1 = 16f * (1f - y1 - side.directionVec.y * eps3 / sprite.iconHeight)
//        when (side) {
//            EnumFacing.WEST, EnumFacing.EAST -> {
//                y0 -= eps1
//                y1 += eps1
//                v0 -= eps2 / sprite.iconHeight
//                v1 += eps2 / sprite.iconHeight
//            }
//
//            EnumFacing.DOWN, EnumFacing.UP -> {
//                x0 -= eps1
//                x1 += eps1
//                u0 += eps2 / sprite.iconWidth
//                u1 -= eps2 / sprite.iconWidth
//            }
//
//            else -> throw java.lang.IllegalArgumentException("can't handle z-oriented side")
//        }
//        when (side) {
//            EnumFacing.WEST -> {
//                x0 += eps0
//                x1 += eps0
//            }
//
//            EnumFacing.EAST -> {
//                x0 -= eps0
//                x1 -= eps0
//            }
//
//            EnumFacing.DOWN -> {
//                y0 -= eps0
//                y1 -= eps0
//            }
//
//            EnumFacing.UP -> {
//                y0 += eps0
//                y1 += eps0
//            }
//
//            else -> throw java.lang.IllegalArgumentException("can't handle z-oriented side")
//        }
//        worldrenderer.pos(x0.toDouble(), y0.toDouble(), z1.toDouble()).tex(sprite.getInterpolatedU(u0.toDouble()).toDouble(), sprite.getInterpolatedV(v0.toDouble()).toDouble()).color(color.red, color.green, color.blue, color.alpha).normal(side.frontOffsetX.toFloat(), side.frontOffsetY.toFloat(), side.frontOffsetZ.toFloat()).endVertex()
//        worldrenderer.pos(x1.toDouble(), y1.toDouble(), z1.toDouble()).tex(sprite.getInterpolatedU(u1.toDouble()).toDouble(), sprite.getInterpolatedV(v1.toDouble()).toDouble()).color(color.red, color.green, color.blue, color.alpha).normal(side.frontOffsetX.toFloat(), side.frontOffsetY.toFloat(), side.frontOffsetZ.toFloat()).endVertex()
//        worldrenderer.pos(x1.toDouble(), y1.toDouble(), z2.toDouble()).tex(sprite.getInterpolatedU(u1.toDouble()).toDouble(), sprite.getInterpolatedV(v1.toDouble()).toDouble()).color(color.red, color.green, color.blue, color.alpha).normal(side.frontOffsetX.toFloat(), side.frontOffsetY.toFloat(), side.frontOffsetZ.toFloat()).endVertex()
//        worldrenderer.pos(x0.toDouble(), y0.toDouble(), z2.toDouble()).tex(sprite.getInterpolatedU(u0.toDouble()).toDouble(), sprite.getInterpolatedV(v0.toDouble()).toDouble()).color(color.red, color.green, color.blue, color.alpha).normal(side.frontOffsetX.toFloat(), side.frontOffsetY.toFloat(), side.frontOffsetZ.toFloat()).endVertex()
//    }

    fun renderHeldItemWithLayer(stack: ItemStack, model: IBakedModel) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.translate(-0.5f, -0.5f, 0.03125f)
        val layer0 = model.particleTexture
        val layer1 = getOverlay(model, stack)
        renderHeldItem(layer0, Color(stack.item.getColorFromItemStack(stack, 0)))
        layer1?.let {
            renderHeldItem(layer1, Color(stack.item.getColorFromItemStack(stack, 1)))
        }
        if (stack.hasEffect()) {
            renderGlintHeld(model)
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

    private fun renderGlintHeld(model: IBakedModel) {
        GlStateManager.depthFunc(514)
        GlStateManager.disableLighting()
        mc.textureManager.bindTexture(RES_ITEM_GLINT)
        GlStateManager.enableBlend()
        OpenGlHelper.glBlendFunc(768, 1, 1, 0)
        GlStateManager.matrixMode(5890)

        GlStateManager.pushMatrix()
        // switch to 8.0f for 1.8
        GlStateManager.scale(0.125f, 0.125f, 0.125f)
        // switch * to / for 1.8
        val f = (Minecraft.getSystemTime() % 3000L) / 3000.0f * 8.0f
        GlStateManager.translate(f, 0.0f, 0.0f)
        GlStateManager.rotate(-50.0f, 0.0f, 0.0f, 1.0f)
        renderHeldItemGlint(0.0, 0.0, -1.0, 1.0, 16, 16, Color(-10407781))
        // switch to this method for 1.8
//        renderHeldItem(model.particleTexture, Color(-10407781))
        GlStateManager.popMatrix()

        GlStateManager.pushMatrix()
        // switch to 8.0f for 1.8
        GlStateManager.scale(0.125f, 0.125f, 0.125f)
        // switch * to / for 1.8
        val f1 = (Minecraft.getSystemTime() % 4873L) / 4873.0f * 8.0f
        GlStateManager.translate(-f1, 0.0f, 0.0f)
        GlStateManager.rotate(10.0f, 0.0f, 0.0f, 1.0f)
        renderHeldItemGlint(0.0, 0.0, -1.0, 1.0, 16, 16, Color(-10407781))
        // switch to this method for 1.8
//        renderHeldItem(model.particleTexture, Color(-10407781))
        GlStateManager.popMatrix()

        GlStateManager.matrixMode(5888)
        GlStateManager.blendFunc(770, 771)
        GlStateManager.disableBlend()
        GlStateManager.enableLighting()
        GlStateManager.depthFunc(515)
    }

    //todo: figure out how to make with forge files
    private fun renderHeldItemGlint(var3: Double, var5: Double, var4: Double, var6: Double, var14: Int, var15: Int, color: Color) {
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
    
}
