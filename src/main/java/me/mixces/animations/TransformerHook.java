package me.mixces.animations;

import me.mixces.animations.config.MixcesAnimationsConfig;

public class TransformerHook {

    public static float localVar(float original, float local)
    {
        if (MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled)
        {
            return local;
        }
        return original;
    }

}
