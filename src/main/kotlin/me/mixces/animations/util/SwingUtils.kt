@file:JvmName("SwingUtils")

package me.mixces.animations.util

import net.minecraft.client.entity.EntityPlayerSP
import me.mixces.animations.mixin.accessor.EntityLivingBaseInvoker

fun swingItem(thePlayer: EntityPlayerSP) {
    val armSwingAnimationEnd = (thePlayer as EntityLivingBaseInvoker).invokeGetArmSwingAnimationEnd()
    if (!thePlayer.isSwingInProgress || thePlayer.swingProgressInt < 0 || thePlayer.swingProgressInt >= armSwingAnimationEnd / 2) {
        thePlayer.swingProgressInt = -1
        thePlayer.isSwingInProgress = true
    }
}