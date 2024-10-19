@file:JvmName("ItemBlacklist")

package me.mixces.animations.hook

import net.minecraft.item.ItemBanner
import net.minecraft.item.ItemSkull
import net.minecraft.item.ItemStack

/* these items are special therefore im excluding them lol */
private val blacklistedItems = mapOf(
    ItemSkull::class.java to true,
    ItemBanner::class.java to true
)

fun isPresent(stack: ItemStack): Boolean {
    return blacklistedItems.containsKey(stack.item::class.java)
}