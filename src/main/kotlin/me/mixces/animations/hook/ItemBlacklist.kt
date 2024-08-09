package me.mixces.animations.hook

import net.minecraft.item.ItemSkull
import net.minecraft.item.ItemStack

object ItemBlacklist {

    fun isPresent(stack: ItemStack): Boolean = stack.item is ItemSkull
}