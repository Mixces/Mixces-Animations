package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.GlintModel;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow
    @Final
    private TextureManager textureManager;

    @Shadow
    @Final
    private static ResourceLocation RES_ITEM_GLINT;

    @Shadow
    protected abstract void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d);

    @Unique
    private boolean mixcesAnimations$isGui;

    @Unique
    private boolean mixcesAnimations$isHeld;

    @Unique
    private IBakedModel mixcesAnimations$model = null;

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(value = "HEAD")
    )
    private void mixcesAnimations$captureModel(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        mixcesAnimations$model = model;
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(value = "TAIL")
    )
    private void mixcesAnimations$clearThreadLocalF1(CallbackInfo ci) {
        /* we need to clear this field */
        mixcesAnimations$model = null;
    }

    @ModifyArg(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItemModelTransform(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            ),
            index = 2
    )
    private ItemCameraTransforms.TransformType mixcesAnimations$changeTransformType(ItemCameraTransforms.TransformType cameraTransformType) {
        return MixcesAnimationsConfig.INSTANCE.getFastItems() && MixcesAnimationsConfig.INSTANCE.enabled ? ItemCameraTransforms.TransformType.GUI : cameraTransformType;
    }

    @ModifyArgs(
            method = "putQuadNormal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/WorldRenderer;putNormal(FFF)V"
            )
    )
    private void mixcesAnimations$modifyNormals(Args args) {
        if (MixcesAnimationsConfig.INSTANCE.getFastItems() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (!mixcesAnimations$isGui && !mixcesAnimations$isHeld && !mixcesAnimations$model.isGui3d()) {
                args.setAll(args.get(0), args.get(2), args.get(1));
            }
        }
    }

    @ModifyArg(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderEffect(Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    public IBakedModel mixcesAnimations$replaceModel(IBakedModel model) {
        return MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled ? GlintModel.INSTANCE.getModel(model) : model;
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

    @ModifyConstant(
            method = "renderEffect",
            constant = @Constant(
                    floatValue = 8.0F
            )
    )
    public float mixcesAnimations$modifyScale(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled) {
            original = 1.0F / original;
        }
        return original;
    }

    @Inject(
            method = "renderEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    public void mixcesAnimations$cancelGlint(CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.enabled) {
            /* we need to cancel the current glint to either replace it with ours */
            /* or flat out remove it for dropped/thrown items */
            if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && mixcesAnimations$isGui) ci.cancel();
            if (MixcesAnimationsConfig.INSTANCE.getFastItems() && !mixcesAnimations$isGui && !mixcesAnimations$isHeld) ci.cancel();
        }
    }

    @Inject(
            method = "renderItemIntoGUI",
            at = @At("HEAD")
    )
    public void mixcesAnimations$captureGuiMode(ItemStack stack, int x, int y, CallbackInfo ci) {
        mixcesAnimations$isGui = true;
    }

    @Inject(
            method = "renderItemIntoGUI",
            at = @At("TAIL")
    )
    public void mixcesAnimations$releaseGuiMode(ItemStack stack, int x, int y, CallbackInfo ci) {
        mixcesAnimations$isGui = false;
    }

    @Inject(
            method = "renderItemModelForEntity",
            at = @At("HEAD")
    )
    public void mixcesAnimations$captureGroundMode(ItemStack stack, EntityLivingBase entityToRenderFor, ItemCameraTransforms.TransformType cameraTransformType, CallbackInfo ci) {
        mixcesAnimations$isHeld = true;
    }

    @Inject(
            method = "renderItemModelForEntity",
            at = @At("TAIL")
    )
    public void mixcesAnimations$releaseGroundMode(ItemStack stack, EntityLivingBase entityToRenderFor, ItemCameraTransforms.TransformType cameraTransformType, CallbackInfo ci) {
        mixcesAnimations$isHeld = false;
    }

    @Inject(
            method = "renderItemAndEffectIntoGUI",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;zLevel:F",
                    ordinal = 1
            )
    )
    public void mixcesAnimations$useCustomGlint(ItemStack stack, int x, int y, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldGlint() && MixcesAnimationsConfig.INSTANCE.enabled && stack.hasEffect()) {
            mixcesAnimations$renderEffect(x, y);
        }
    }

    @Unique
    private void mixcesAnimations$renderEffect(int x, int y) {
        /* renders the gui glint. values taken from 1.7 */
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

        /* this is needed to adapt the glint to the gui */
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
        /* drawing the glint in one batch */
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        mixcesAnimations$draw(worldRenderer, (float) ((Minecraft.getSystemTime() % 3000L) / 3000.0), 0.0625F);
        mixcesAnimations$draw(worldRenderer, (float) ((Minecraft.getSystemTime() % 4873L) / 4873.0) - 0.0625F, 0.0625F);
        tessellator.draw();
    }

    @Unique
    private void mixcesAnimations$draw(WorldRenderer worldRenderer, double width, double height) {
        /* draws the glint. taken from 1.7 */
        worldRenderer.pos(0.0, 0.0, 0.0).tex(width + height * 4.0, height).endVertex();
        worldRenderer.pos(1.0, 0.0, 0.0).tex(width + height * 5.0, height).endVertex();
        worldRenderer.pos(1.0, 1.0, 0.0).tex(width + height, 0.0).endVertex();
        worldRenderer.pos(0.0, 1.0, 0.0).tex(width, 0.0).endVertex();
    }
}
