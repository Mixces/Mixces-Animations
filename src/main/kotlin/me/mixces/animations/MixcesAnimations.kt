package me.mixces.animations

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import me.mixces.animations.command.MixcesAnimationsCommand
import me.mixces.animations.config.MixcesAnimationsConfig
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent


@Mod(
    modid = MixcesAnimations.MODID,
    name = MixcesAnimations.NAME,
    version = MixcesAnimations.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object MixcesAnimations
{

    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?)
    {
        MixcesAnimationsConfig
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent?)
    {
        CommandManager.INSTANCE.registerCommand(MixcesAnimationsCommand())
    }

}
