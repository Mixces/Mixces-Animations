package me.mixces.animations.api

import net.minecraft.entity.EntityLivingBase

interface IDamageTint {
    fun setupDamageTint(entityLivingBase: EntityLivingBase, partialTicks: Float): Boolean
}