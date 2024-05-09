@file:JvmName("SwingUtils")

package me.mixces.animations.util

import net.minecraft.client.entity.EntityPlayerSP
import me.mixces.animations.mixin.accessor.EntityLivingBaseInvoker

fun swingItem(thePlayer: EntityPlayerSP) {
    val armSwingAnimationEnd = (thePlayer as? EntityLivingBaseInvoker)?.invokeGetArmSwingAnimationEnd() ?: return
    if (!thePlayer.isSwingInProgress || thePlayer.swingProgressInt !in 0 until armSwingAnimationEnd / 2) {
        thePlayer.swingProgressInt = -1
        thePlayer.isSwingInProgress = true
    }

}