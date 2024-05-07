package me.mixces.animations.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import me.mixces.animations.MixcesAnimations
import me.mixces.animations.config.MixcesAnimationsConfig


@Command(
    value = MixcesAnimations.MODID
)
class MixcesAnimationsCommand {

    @Main
    fun handle() {
        MixcesAnimationsConfig.openGui()
    }

}
