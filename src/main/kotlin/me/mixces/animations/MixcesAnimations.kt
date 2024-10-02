package me.mixces.animations

import me.mixces.animations.config.MixcesAnimationsConfig
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
    modid = MixcesAnimations.MODID,
    name = MixcesAnimations.NAME,
    version = MixcesAnimations.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object MixcesAnimations {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    val keyBindUseItem = KeyBinding("Second Use Item Key", -99, "Mixces Animations")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        println("$NAME has been initialized!")
        MixcesAnimationsConfig

        /* register custom use item keybind */
        ClientRegistry.registerKeyBinding(keyBindUseItem)
    }
}
