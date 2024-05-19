package me.mixces.animations.init

import me.mixces.animations.MixcesAnimations
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameRegistry

object PotionComponents {

    lateinit var itemBottleDrinkableEmpty: Item
    lateinit var itemBottleOverlay: Item
    lateinit var itemBottleSplashEmpty: Item

    fun preInit() {
        itemBottleDrinkableEmpty = ItemBottleDrinkableEmpty()
        itemBottleOverlay = ItemBottleOverlay()
        itemBottleSplashEmpty = ItemBottleSplashEmpty()

        setCustomModelResourceLocation(itemBottleDrinkableEmpty, MixcesAnimations.MODID + ":bottle_drinkable_empty")
        setCustomModelResourceLocation(itemBottleOverlay, MixcesAnimations.MODID + ":bottle_overlay")
        setCustomModelResourceLocation(itemBottleSplashEmpty, MixcesAnimations.MODID + ":bottle_splash_empty")
    }

    private fun registerItem(item: Item, name: String): Item {
        return item.apply { registryName = name; unlocalizedName = name; GameRegistry.registerItem(this) }
    }

    private fun setCustomModelResourceLocation(item: Item?, modelResourceLocation: String) {
        ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(modelResourceLocation, "inventory"))
    }

    class ItemBottleDrinkableEmpty : Item() { init { registerItem(this, "item_bottle_drinkable_empty") } }
    class ItemBottleOverlay : Item() { init { registerItem(this, "item_bottle_overlay") } }
    class ItemBottleSplashEmpty : Item() { init { registerItem(this, "item_bottle_splash_empty") } }

}