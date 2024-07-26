package me.mixces.animations.hook

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.client.resources.model.SimpleBakedModel
import net.minecraft.util.EnumFacing

object GlintModelHook
{

    private val glintMap = hashMapOf<HashedModel, IBakedModel>()

    fun getGlint(model: IBakedModel): IBakedModel
    {
        return glintMap.computeIfAbsent(HashedModel(model))
        {
            SimpleBakedModel.Builder(model, JustUV).makeBakedModel()
        }
    }

    data class HashedModel(val data: List<Int>)
    {
        constructor(model: IBakedModel) : this((EnumFacing.entries.flatMap { face -> model.getFaceQuads(face) } + model.generalQuads).flatMap { it.vertexData.slice(0..2) })
    }

    object JustUV : TextureAtlasSprite("uv")
    {
        override fun getInterpolatedU(u: Double) = (-u / 16).toFloat()

        override fun getInterpolatedV(v: Double) = (v / 16).toFloat()
    }

}