package me.mixces.animations

import me.mixces.animations.config.MixcesAnimationsConfig
import me.mixces.animations.init.CustomModelBakery
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

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

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        CustomModelBakery
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        println("$NAME has been initialized!")
        MixcesAnimationsConfig
    }
}
