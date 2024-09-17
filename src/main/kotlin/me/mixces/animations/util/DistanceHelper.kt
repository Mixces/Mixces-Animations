@file:JvmName("DistanceHelper")

package me.mixces.animations.util

import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3

fun distance(enemy: Entity): Double {
    val player = mc.renderViewEntity
    val playerCamera = Vec3(player.posX, player.posY + player.eyeHeight, player.posZ)
    val size: Double = enemy.collisionBorderSize.toDouble()
    val enemyAABB = enemy.entityBoundingBox.expand(size, size, size)
    return playerCamera.distanceTo(playerCamera.coerceInto(enemyAABB))
}

private fun Vec3.coerceInto(box: AxisAlignedBB) = Vec3(
    xCoord.coerceIn(box.minX, box.maxX),
    yCoord.coerceIn(box.minY, box.maxY),
    zCoord.coerceIn(box.minZ, box.maxZ),
)