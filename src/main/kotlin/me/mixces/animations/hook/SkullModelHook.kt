package me.mixces.animations.hook

import me.mixces.animations.init.CustomModelBakery
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.item.ItemStack

object SkullModelHook {

    private val map = HashMap<Int, IBakedModel>(5)

    init {
        map[0] = CustomModelBakery.SKULL_SKELETON.bakedModel
        map[1] = CustomModelBakery.SKULL_WITHER.bakedModel
        map[2] = CustomModelBakery.SKULL_ZOMBIE.bakedModel
        map[3] = CustomModelBakery.SKULL_CHAR.bakedModel
        map[4] = CustomModelBakery.SKULL_CREEPER.bakedModel
    }

    fun getSkullModel(stack: ItemStack): IBakedModel? {
        val metadata: Int = stack.metadata
        if (metadata in 0 until map.size) {
            return map[metadata]
        }
        return map[3]
    }

}