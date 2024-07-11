package me.mixces.animations

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.mixces.animations.command.MixcesAnimationsCommand
import me.mixces.animations.config.MixcesAnimationsConfig
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent


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

    var cacheTime: Long = 0
    var tooltipCache: List<String>? = null

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?)
    {
        MixcesAnimationsConfig
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent?)
    {
        CommandManager.INSTANCE.registerCommand(MixcesAnimationsCommand())
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent)
    {
        if (event.phase === TickEvent.Phase.START)
        {
            if ((System.currentTimeMillis() - cacheTime) < 200)
            {
                return
            }
            tooltipCache = null
            cacheTime = System.currentTimeMillis()
        }
    }

}
