package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.ItemBlacklist;
import me.mixces.animations.util.GlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    private int equippedItemSlot;

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    @Final
    private RenderItem itemRenderer;

    @Unique
    private boolean mixcesAnimations$isSwingInProgress;

    @Unique
    private static final ThreadLocal<Float> mixcesAnimations$f1 = ThreadLocal.withInitial(() -> 0.0F);

    @ModifyVariable(
            method = "renderItemInFirstPerson",
            at = @At("STORE"),
            index = 4
    )
    private float mixcesAnimations$captureF1(float value) {
        mixcesAnimations$f1.set(value);
        return value;
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
        return MixcesAnimationsConfig.INSTANCE.getBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled ? mixcesAnimations$f1.get() : swingProgress;
    }

    @Inject(
            method = "doBowTransformations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"
            )
    )
    private void mixcesAnimations$preBowTransform(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled) {
            /* original transformations from 1.7 */
            GlHelper.INSTANCE.roll(-335.0F).yaw(-50.0F);
        }
    }

    @Inject(
            method = "doBowTransformations",
            at = @At("TAIL")
    )
    private void mixcesAnimations$postBowTransform(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled) {
            /* original transformations from 1.7 */
            GlHelper.INSTANCE.yaw(50.0F).roll(335.0F);
        }
    }

    @Inject(
            method = "renderItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItemModelForEntity(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$applyHeldItemTransforms(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getPositions() || !MixcesAnimationsConfig.INSTANCE.enabled) return;
        if (itemRenderer.shouldRenderItemIn3D(heldStack) || ItemBlacklist.isPresent(heldStack)) return;
        GlHelper builder = GlHelper.INSTANCE;
        /* original transformations from 1.7 */
        builder.translate(0.0F, -0.3F, 0.0F).scale(1.5F, 1.5F, 1.5F).yaw(50.0F).roll(335.0F).translate(-0.9375F, -0.0625F, 0.0F);
        /* we need to adapt the 1.7 transformations to fit in 1.8 */
        float width = 0.03125F; /* this is half of 1/16, a common fraction present in this game */
        float offset = 0.5F; /* undo the offset perpetrated by RenderItem#renderItem */
        builder.yaw(180.0F).translate(-offset, offset, width);
    }

    @Inject(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void mixcesAnimations$applyRodRotation(float partialTicks, CallbackInfo ci) {
        /* original transformation from 1.7 */
        if (MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (itemToRender.getItem().shouldRotateAroundWhenRendering()) GlHelper.INSTANCE.yaw(180.0F);
        }
    }

    @ModifyArg(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            ),
            index = 2
    )
    private ItemCameraTransforms.TransformType mixcesAnimations$changeTransformType(ItemCameraTransforms.TransformType transform) {
        return MixcesAnimationsConfig.INSTANCE.getPositions() && MixcesAnimationsConfig.INSTANCE.enabled && !ItemBlacklist.isPresent(itemToRender) ? ItemCameraTransforms.TransformType.NONE : transform;
    }

    @Inject(
            method = "renderItemInFirstPerson",
            at = @At(value = "TAIL")
    )
    private void mixcesAnimations$clearThreadLocalF1(CallbackInfo ci) {
        /* we need to clear the threadlocal */
        mixcesAnimations$f1.remove();
    }

    @Redirect(
            method = "updateEquippedItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getIsItemStackEqual(Lnet/minecraft/item/ItemStack;)Z"
            )
    )
    public boolean mixcesAnimations$addEqualityCheck(ItemStack instance, ItemStack p_179549_1_) {
        return ((!MixcesAnimationsConfig.INSTANCE.getOldReequip() || !MixcesAnimationsConfig.INSTANCE.enabled) || (equippedItemSlot == mc.thePlayer.inventory.currentItem)) && instance.isItemEqual(p_179549_1_);
    }
}
