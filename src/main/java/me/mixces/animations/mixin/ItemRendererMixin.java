package me.mixces.animations.mixin;

import cc.polyfrost.oneconfig.libs.checker.units.qual.A;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.*;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow private ItemStack itemToRender;
    @Unique private static final ThreadLocal<Float> mixcesAnimations$f1 = ThreadLocal.withInitial(() -> 0.0F);

    @ModifyVariable(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "STORE"
            ),
            index = 4
    )
    private float mixcesAnimations$captureF1(float f1) {
        mixcesAnimations$f1.set(f1);
        return f1;
    }

    @ModifyArg(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;performDrinking(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;doBowTransformations(FLnet/minecraft/client/entity/AbstractClientPlayer;)V"
                    )
            ),
            index = 1
    )
    private float mixcesAnimations$useF1(float swingProgress) {
        if (MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return mixcesAnimations$f1.get();
        }
        return swingProgress;
    }

    @Inject(
            method = "doBlockTransformations",
            at = @At(
                    value = "TAIL"
            )
    )
    public void mixcesAnimations$lunarBlockTransform(CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getLunar() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        mixcesAnimations$doLunarBlockTransformations();
    }

    @Unique
    private void mixcesAnimations$doLunarBlockTransformations() {
        GlStateManager.translate(-0.55F, 0.2F, 0.1F);
        GlStateManager.scale(0.85F, 0.85F, 0.85F);
        GlStateManager.rotate(1.0F, 0.0F, 0.0F, -1.0F);
        GlStateManager.rotate(1.0F, 0.25F, 0.0F, 0.0F);
        GlStateManager.rotate(2.0F, 0.0F, 2.0F, 0.0F);
    }

    @ModifyArg(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V",
                    ordinal = 2
            ),
            index = 0
    )
    private float overflowAnimations$lunarModifyEquip(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getLunar() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return 0.2F;
        }
        return original;
    }

    @ModifyArg(
            method = "performDrinking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V",
                    ordinal = 1
            ),
            index = 0
    )
    public float mixcesAnimations$lunarDrinkingTranslation(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getLunar() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return original * 0.66F / 0.6F;
        }
        return original;
    }

    @ModifyArg(
            method = "performDrinking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 1
            ),
            index = 0
    )
    public float mixcesAnimations$lunarDrinkingRotation(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getLunar() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return original * 5.0F / 10.0F;
        }
        return original;
    }

    @ModifyArg(
            method = "performDrinking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 2
            ),
            index = 0
    )
    public float mixcesAnimations$lunarDrinkingRotation2(float original) {
        if (MixcesAnimationsConfig.INSTANCE.getLunar() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return original * 28.0F / 30.0F;
        }
        return original;
    }

    @Inject(
            method = "doBowTransformations",
            at = @At(
                    value = "HEAD"
            )
    )
    public void mixcesAnimations$lunarBowPosition(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getLunar() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        GlStateManager.translate(-0.2D, 0.0D, -0.175D);
        GlStateManager.rotate(1.0F, 0.0F, 0.0F, -1.25F);
    }

    @Inject(
            method = "transformFirstPersonItem(FF)V",
            at = @At(
                    "HEAD"
            )
    )
    public void overflowAnimations$lunarItemTransforms(float equipProgress, float swingProgress, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getLunar() && MixcesAnimationsConfig.INSTANCE.enabled) {
            Item item = itemToRender.getItem();
            if (item != null) {
                if (item instanceof ItemSword) {
                    GlStateManager.translate(0.0F, 0.0F, -0.02F);
                    GlStateManager.rotate(1.0F, 0.0F, 0.0F, -0.1F);
                } else if (item instanceof ItemPotion) {
                    GlStateManager.translate(-0.0225F, -0.02F, 0.0F);
                    GlStateManager.rotate(1.0F, 0.0F, 0.0F, 0.1F);
                } else if (item.shouldRotateAroundWhenRendering()) {
                    GlStateManager.translate(0.08F, -0.0275F, -0.33F);
                    GlStateManager.scale(0.949999988079071D, 1.0D, 1.0D);
                } else if (item instanceof ItemBow) {
                    GlStateManager.translate(0.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(0.9F, 0.0F, 0.001F, 0.0F);
                } else if (item instanceof ItemBlock) {
                    Block block = ((ItemBlock) item).getBlock();
                    if (block instanceof BlockCarpet || block instanceof BlockSnow) {
                        GlStateManager.translate(0.0F, -0.25F, 0.0F);
                    }
                }
            }
        }
    }

}
