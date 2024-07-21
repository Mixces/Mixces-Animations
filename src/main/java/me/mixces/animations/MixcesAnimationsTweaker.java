package me.mixces.animations;

import me.mixces.animations.asm.ClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class MixcesAnimationsTweaker implements IFMLLoadingPlugin
{

    @Override
    public final String[] getASMTransformerClass()
    {
        return new String[] { ClassTransformer.class.getName() };
    }

    @Override
    public final String getModContainerClass()
    {
        return null;
    }

    @Override
    public final String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map)
    {
        /* no-op */
    }

    @Override
    public final String getAccessTransformerClass()
    {
        return null;
    }

}