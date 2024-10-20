package me.mixces.animations.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.utils.dsl.mc
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

    @Switch(name = "Armor Damage Tint")
    var oldArmor = true

    @Switch(name = "Deep Red Damage Tint")
    var damageTint = true

    @Switch(name = "Bugged Arrow Layer Lighting")
    var arrowLighting = true

    @Switch(name = "Better Item Pickup")
    var oldPickup = true

    @Switch(name = "Fast Items")
    var fastItems = true

    @Switch(name = "Alternative Potion Rendering")
    var oldPotion = true

    @Switch(name = "Full Re-equip Logic")
    var oldReequip = true

    @Switch(name = "Remove Miss Penalty Attack Behavior", subcategory = "Advantage")
    var oldDelay = true

    @Switch(name = "Auto Sprint Reset", subcategory = "Advantage")
    var sprintReset = true

    init {
        initialize()

        addListener("fastGrass") {
            mc.renderGlobal.loadRenderers()
        }
    }
}
