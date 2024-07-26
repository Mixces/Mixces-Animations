package me.mixces.animations.util;

import net.minecraft.client.renderer.GlStateManager;

@SuppressWarnings("UnusedReturnValue")
public class GlHelper {

    public static final GlHelper INSTANCE = new GlHelper();

    public GlHelper pitch(Float pitch)
    {
        GlStateManager.rotate(pitch, 1.0f, 0.0f, 0.0f);
        return (this);
    }

    public GlHelper yaw(Float yaw)
    {
        GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f);
        return (this);
    }

    public GlHelper roll(Float roll)
    {
        GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f);
        return (this);
    }

    public GlHelper translate(float x, float y, float z)
    {
        GlStateManager.translate(x, y, z);
        return this;
    }

}
