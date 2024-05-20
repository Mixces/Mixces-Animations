package me.mixces.animations.init

import me.mixces.animations.MixcesAnimations
import net.minecraft.client.renderer.ItemModelMesher
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemStack
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameRegistry

object PotionComponents {

    class ItemBottleDrinkableEmpty : Item() { init { registerItem(this, "item_bottle_drinkable_empty") } }
    class ItemBottleOverlay : Item() { init { registerItem(this, "item_bottle_overlay") } }
    class ItemBottleSplashEmpty : Item() { init { registerItem(this, "item_bottle_splash_empty") } }

    private lateinit var itemBottleDrinkableEmpty: Item
    private lateinit var itemBottleOverlay: Item
    private lateinit var itemBottleSplashEmpty: Item

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

    @JvmStatic
    fun getPotionOverlayModel(itemModelMesher: ItemModelMesher) : IBakedModel {
        return getModel(itemModelMesher, ModelResourceLocation(MixcesAnimations.MODID + ":bottle_overlay", "inventory"))
    }

    @JvmStatic
    fun getPotionBottleModel(itemModelMesher: ItemModelMesher, stack: ItemStack): IBakedModel {
        val modelResourceLocation = ModelResourceLocation(
            MixcesAnimations.MODID + ":" +
                    if (ItemPotion.isSplash(stack.metadata)) { "bottle_splash_empty" } else { "bottle_drinkable_empty" },
            "inventory"
        )
        return getModel(itemModelMesher, modelResourceLocation)
    }

    @JvmStatic
    fun getPotionBottleStack(stack : ItemStack) : ItemStack {
        return ItemStack(if (ItemPotion.isSplash(stack.metadata)) { itemBottleSplashEmpty } else { itemBottleDrinkableEmpty })
    }

    private fun getModel(itemModelMesher: ItemModelMesher, modelResourceLocation: ModelResourceLocation): IBakedModel {
        return itemModelMesher.modelManager.getModel(modelResourceLocation)
    }

}