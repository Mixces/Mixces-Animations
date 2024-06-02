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
        name = "Mirrored Projectiles",
        category = "Old Animations"
    )
    var oldProjectiles = true

    @Switch(
        name = "Remove Heart Flashing",
        category = "Old Animations"
    )
    var oldHearts = true

    @Switch(
        name = "Armor Hurt Color Tint",
        category = "Old Animations"
    )
    var oldArmor = true

    @Switch(
        name = "Better Item Pickup",
        category = "Old Animations"
    )
    var oldPickup = true

    @Switch(
        name = "Remove Miss Penalty Attack Behavior",
        category = "Old Animations"
    )
    var oldDelay = true

    init {
        initialize()
    }
    
}
