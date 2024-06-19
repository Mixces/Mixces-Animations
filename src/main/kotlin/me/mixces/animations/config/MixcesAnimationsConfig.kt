package me.mixces.animations.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.mixces.animations.MixcesAnimations
import net.minecraft.client.Minecraft



object MixcesAnimationsConfig : Config(Mod(MixcesAnimations.NAME, ModType.PVP), MixcesAnimations.MODID + ".json") {

    @Switch(
        name = "Block-Hitting Animation"
    )
    var oldBlockHitting = true

    @Switch(
        name = "Smooth Sneaking"
    )
    var smoothSneaking = true

    @Switch(
        name = "Mirrored Projectiles"
    )
    var oldProjectiles = true

    @Switch(
        name = "Remove Heart Flashing"
    )
    var oldHearts = true
    @Switch(
        name = "Armor Hurt Color Tint"
    )
    var oldArmor = true

    @Switch(
        name = "Better Item Pickup"
    )
    var oldPickup = true

    @Switch(
        name = "Fast Grass Sides"
    )
    var fastGrass = true

    @Switch(
        name = "Alternative Potion Rendering"
    )
    var oldPotion = true

    @Switch(
        name = "Alternative Skull Rendering"
    )
    var oldSkulls = true

    @Switch(
        name = "Remove Miss Penalty Attack Behavior"
    )
    var oldDelay = true

    init {
        initialize()

        val reloadWorld = Runnable { Minecraft.getMinecraft().renderGlobal.loadRenderers() }
        addListener("fastGrass", reloadWorld)
    }
    
}
