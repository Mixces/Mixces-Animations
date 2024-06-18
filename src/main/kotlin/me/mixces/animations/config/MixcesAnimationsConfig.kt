package me.mixces.animations.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.mixces.animations.MixcesAnimations

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
        name = "Old Potion Rendering",
        category = "Old Animations"
    )
    var oldPotion = true

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
        name = "Remove Miss Penalty Attack Behavior"
    )
    var oldDelay = true

    init {
        initialize()
    }
    
}
