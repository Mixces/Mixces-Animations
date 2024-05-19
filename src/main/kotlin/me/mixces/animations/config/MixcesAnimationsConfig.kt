package me.mixces.animations.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.mixces.animations.MixcesAnimations

object MixcesAnimationsConfig : Config(Mod(MixcesAnimations.NAME, ModType.PVP), MixcesAnimations.MODID + ".json") {

    @Switch(
        name = "Block-Hitting Animation",
        category = "Old Animations"
    )
    var oldBlockHitting = true

    @Switch(
        name = "Smooth Sneaking",
        category = "Old Animations"
    )
    var smoothSneaking = true

    @Switch(
        name = "Old Projectiles",
        category = "Old Animations"
    )
    var oldProjectiles = true

    @Switch(
        name = "Old Heart Flashing",
        category = "Old Animations"
    )
    var oldHearts = true

    @Switch(
        name = "Old Potion Rendering",
        category = "Old Animations"
    )
    var oldPotion = true

    @Switch(
        name = "Armor Hurt Color Tint",
        category = "Old Animations"
    )
    var oldArmor = true

    @Switch(
        name = "Old Item Pickup",
        category = "Old Animations"
    )
    var oldPickup = true

    @Switch(
        name = "Old Miss Penalty",
        category = "Old Animations"
    )
    var oldDelay = true

    init {
        initialize()
    }
    
}
