@file:JvmName("RenderItemHook")

package me.mixces.animations.hook

import me.mixces.animations.MixcesAnimations
import me.mixces.animations.init.PotionComponents
import net.minecraft.client.renderer.ItemModelMesher
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemStack

fun getPotionOverlayModel(itemModelMesher: ItemModelMesher) : IBakedModel {
    return itemModelMesher.modelManager.getModel(ModelResourceLocation(MixcesAnimations.MODID + ":bottle_overlay", "inventory"))
}

fun getPotionBottleModel(itemModelMesher: ItemModelMesher, stack: ItemStack): IBakedModel {
    val modelResourceLocation = ModelResourceLocation(
        MixcesAnimations.MODID + ":" +
                if (ItemPotion.isSplash(stack.metadata)) { "bottle_splash_empty" } else { "bottle_drinkable_empty" },
        "inventory"
    )
    return itemModelMesher.modelManager.getModel(modelResourceLocation)
}

fun getPotionBottleStack(stack : ItemStack) : ItemStack {
    return ItemStack(
        if (ItemPotion.isSplash(stack.metadata)) { PotionComponents.itemBottleSplashEmpty } else { PotionComponents.itemBottleDrinkableEmpty }
    )
}