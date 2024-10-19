package me.mixces.animations

import org.spongepowered.asm.lib.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class MixcesAnimationsMixinPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String) {
        println("MixcesAnimations mixins have loaded!")
    }

    override fun getRefMapperConfig(): String {
        return ""
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        return true
    }

    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) {
        /* no-op */
    }

    override fun getMixins(): List<String>? {
        return null
    }

    override fun preApply(targetClassName: String, targetClass: ClassNode, mixinClassName: String, mixinInfo: IMixinInfo) {
        /* no-op */
    }

    override fun postApply(targetClassName: String, targetClass: ClassNode, mixinClassName: String, mixinInfo: IMixinInfo) {
        /* no-op */
    }
}