package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.GlintModelHook;
import me.mixces.animations.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow 
    @Final 
    private TextureManager textureManager;

    @Unique
    private boolean mixcesAnimations$isGui;

    @Shadow
    @Final
    private static ResourceLocation RES_ITEM_GLINT;

    @Shadow
    protected abstract void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d);


    @ModifyArg(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    public IBakedModel mixcesAnimations$replaceModel(IBakedModel model) {
        return MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled ? GlintModelHook.INSTANCE.getGlint(model) : model;
    }

    @ModifyArg(
            method = "renderModel(Lnet/minecraft/client/resources/model/IBakedModel;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V"
            ),
            index = 1
    )
    public int mixcesAnimations$replaceColor(int color) {
        return MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled ? -10407781 : color;
    }

    @Inject(
            method = "renderEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    public void mixcesAnimations$cancelGlint(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (mixcesAnimations$isGui) ci.cancel();
        }
    }

    @ModifyConstant(
            method = "renderEffect",
            constant = @Constant(floatValue = 8.0F)
    )
    public float mixcesAnimations$modifyScale(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled) {
            original = 1.0F / original;
        }
        return original;
    }

    @Inject(
            method = "renderItemAndEffectIntoGUI",
            at = @At("HEAD")
    )
    public void mixcesAnimations$captureGuiMode(ItemStack stack, int x, int y, CallbackInfo ci) {
        mixcesAnimations$isGui = true;
    }

    @Inject(
            method = "renderItemIntoGUI",
            at = @At("TAIL")
    )
    public void mixcesAnimations$useCustomGlint(ItemStack stack, int x, int y, CallbackInfo ci) {
        mixcesAnimations$isGui = false;
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled && stack.hasEffect()) {
            mixcesAnimations$renderEffect(x, y);
        }
    }

    @Unique
    private void mixcesAnimations$renderEffect(int x, int y) {
        /* renders the gui glint. taken from 1.7 */
        GlStateManager.enableRescaleNormal();
        GlStateManager.depthFunc(GL11.GL_GEQUAL);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        textureManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(772, 1, 0, 0);
        GlStateManager.color(0.5F, 0.25F, 0.8F, 1.0F);

        GlStateManager.pushMatrix();
        setupGuiTransform(x, y, false);
        GlHelper.INSTANCE.scale(0.5f, 0.5f, 0.5f).translate(-0.5f, -0.5f, -0.5f);
        mixcesAnimations$renderFace();
        GlStateManager.popMatrix();

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
    }

    @Unique
    private void mixcesAnimations$renderFace() {
        /* we're drawing the glint in one batch */
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        mixcesAnimations$draw(worldRenderer, (float) ((Minecraft.getSystemTime() % 3000L) / 3000.0), 0.0625F);
        mixcesAnimations$draw(worldRenderer, (float) ((Minecraft.getSystemTime() % 4873L) / 4873.0) - 0.0625F, 0.0625F);
        tessellator.draw();
    }

    @Unique
    private void mixcesAnimations$draw(WorldRenderer worldRenderer, double u, double v) {
        /* draws the glint. taken from 1.7 */
        worldRenderer.pos(0.0, 0.0, 0.0).tex(u + v * 4.0, v).endVertex();
        worldRenderer.pos(1.0, 0.0, 0.0).tex(u + v * 5.0, v).endVertex();
        worldRenderer.pos(1.0, 1.0, 0.0).tex(u + v, 0.0).endVertex();
        worldRenderer.pos(0.0, 1.0, 0.0).tex(u, 0.0).endVertex();
    }
}
