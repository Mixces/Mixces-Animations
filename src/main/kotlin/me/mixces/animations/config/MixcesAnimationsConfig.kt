package me.mixces.animations.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.mixces.animations.MixcesAnimations

object MixcesAnimationsConfig : Config(Mod(MixcesAnimations.NAME, ModType.PVP, "/mixcesanimations.svg"), MixcesAnimations.MODID + ".json") {

    @Switch(name = "Block-Hitting Animation")
    var blockHitting = true

    @Switch(name = "Old Item Positions")
    var positions = true

    @Switch(name = "Smooth Sneaking")
    var smoothSneaking = true

    @Switch(name = "Better Glint")
    var oldGlint = true

    @Switch(name = "Mirrored Projectiles")
    var oldProjectiles = true

    @Switch(name = "Remove Heart Flashing")
    var oldHearts = true

    @Switch(name = "Armor Hurt Color Tint")
    var oldArmor = true

    @Switch(name = "Better Item Pickup")
    var oldPickup = true

    @Switch(name = "Fast Items")
    var fastItems = true

    @Switch(name = "Full Re-equip Logic")
    var oldReequip = true

    @Switch(name = "Remove Miss Penalty Attack Behavior", subcategory = "Advantage")
    var oldDelay = true

    @Switch(name = "Auto Jump Reset", subcategory = "Advantage")
    var jumpReset = true

    @Switch(name = "Auto Sprint Reset", subcategory = "Advantage")
    var sprintReset = true

    @Switch(name = "Aim Assist", subcategory = "Advantage")
    var aimAssist = true

    init {
        initialize()
    }
}
