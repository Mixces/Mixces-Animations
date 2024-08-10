package me.mixces.animations.mixin;

import me.mixces.animations.hook.ItemBlacklist;
import me.mixces.animations.util.GlHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerHeldItem.class)
public abstract class LayerHeldItemMixin {

    @Unique
    private static final ThreadLocal<ItemStack> mixcesAnimations$itemStack = ThreadLocal.withInitial(() -> null);

    @ModifyVariable(
            method = "doRenderLayer",
            at = @At("STORE"),
            index = 9
    )
    private ItemStack mixcesAnimations$captureF1(ItemStack value) {
        mixcesAnimations$itemStack.set(value);
        return value;
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBiped;postRenderArm(F)V"
            )
    )
    private void mixcesAnimations$addSneakTranslation(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (entitylivingbaseIn.isSneaking()) GlHelper.INSTANCE.translate(0.0F, 0.2F, 0.0F);
        }
    }

    @Redirect(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;isSneaking()Z"
            )
    )
    private boolean mixcesAnimations$disableSneakTranslation(EntityLivingBase instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getSmoothSneaking() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isSneaking();
    }

    @Redirect(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getRenderType()I"
            )
    )
    private int mixcesAnimations$disableBlockTypeCheck(Block instance) {
        /* we need to allow all 3d blocks to use these hidden 1.7 transformations */
        return MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled ? 3 : instance.getRenderType();
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$applyHeldItemLayerTransforms(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getPositions() || !MixcesAnimationsConfig.INSTANCE.enabled) return;
        if (ItemBlacklist.INSTANCE.isPresent(mixcesAnimations$itemStack.get())) return;
        final Item item = mixcesAnimations$itemStack.get().getItem();
        GlHelper builder = GlHelper.INSTANCE;
        float var7;
        /* original transformations from 1.7 */
        if (item instanceof ItemBlock && Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(mixcesAnimations$itemStack.get())) {
            var7 = 0.375F;
            builder.translate(0.0F, 0.1875F, -0.3125F).pitch(20.0F).yaw(45.0F).scale(-var7, -var7, var7);
        } else if (item == Items.bow) {
            GlStateManager.cullFace(1028); /* undo polypatcher cullface */
            var7 = 0.625F;
            builder.translate(0.0F, 0.125F, 0.3125F).yaw(-20.0F).scale(var7, -var7, var7).pitch(-100.0F).yaw(45.0F);
        } else if (item.isFull3D()) {
            GlStateManager.cullFace(1028); /* undo polypatcher cullface */
            var7 = 0.625F;
            if (item.shouldRotateAroundWhenRendering()) {
                builder.roll(180.0F).translate(0.0F, -0.125F, 0.0F);
            }
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).getItemInUseCount() > 0 && ((EntityPlayer) entitylivingbaseIn).isBlocking()) {
                builder.translate(0.05F, 0.0F, -0.1F).yaw(-50.0F).pitch(-10.0F).roll(-60.0F);
            }
            builder.translate(0.0F, 0.1875F, 0.0F).scale(var7, -var7, var7).pitch(-100.0F).yaw(45.0F);
        } else {
            var7 = 0.375F;
            GlStateManager.cullFace(1029); /* ignore non-tool items */
            builder.translate(0.25F, 0.1875F, -0.1875F).scale(var7, var7, var7).roll(60.0F).pitch(-90.0F).roll(20.0F);
        }
    }

    @ModifyArg(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            ),
            index = 2
    )
    private ItemCameraTransforms.TransformType mixcesAnimations$changeTransformType(ItemCameraTransforms.TransformType transform) {
        return MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled && !ItemBlacklist.INSTANCE.isPresent(mixcesAnimations$itemStack.get()) ? ItemCameraTransforms.TransformType.NONE : transform;
    }

    @Inject(
            method = "doRenderLayer",
            at = @At("TAIL")
    )
    private void mixcesAnimations$clearThreadLocalItemStack(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale, CallbackInfo ci) {
        /* we need to clear the threadlocal */
        mixcesAnimations$itemStack.remove();
    }
}