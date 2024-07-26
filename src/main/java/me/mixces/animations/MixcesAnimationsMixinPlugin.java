package me.mixces.animations;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixcesAnimationsMixinPlugin implements IMixinConfigPlugin
{

    @Override
    public void onLoad(String mixinPackage)
    {
        System.out.println(MixcesAnimations.NAME + " mixins have loaded!");
    }

    @Override
    public String getRefMapperConfig()
    {
        return (null);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return (true);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
    {
        /* no-op */
    }

    @Override
    public List<String> getMixins()
    {
        return (null);
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {
        /* no-op */
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {
        /* no-op */
    }

}