package me.mixces.animations.hook

import me.mixces.animations.init.CustomModelBakery
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.item.ItemStack

object SkullModelHook {

    fun getSkullModel(stack: ItemStack): IBakedModel {
        return when (stack.metadata) {
            0 -> CustomModelBakery.SKULL_SKELETON.bakedModel
            1 -> CustomModelBakery.SKULL_WITHER.bakedModel
            2 -> CustomModelBakery.SKULL_ZOMBIE.bakedModel
            4 -> CustomModelBakery.SKULL_CREEPER.bakedModel
            else -> CustomModelBakery.SKULL_CHAR.bakedModel
        }
    }

}