package me.mixces.animations.mixin;

import me.mixces.animations.mixin.access.IRendererLivingEntityMixin;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import me.mixces.animations.config.MixcesAnimationsConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBaseMixin {

    @Shadow
    @Final
    private RendererLivingEntity<?> renderer;

    @Unique
    private static final ThreadLocal<ModelBase> mixcesAnimations$t = ThreadLocal.withInitial(() -> null);

    @ModifyVariable(
            method = "renderLayer",
            at = @At("STORE"),
            index = 12
    )
    private ModelBase mixcesAnimations$captureT(ModelBase value) {
        mixcesAnimations$t.set(value);
        return value;
    }

    @Inject(
            method = "renderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void mixcesAnimations$addDamageBrightness(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getOldArmor() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (((IRendererLivingEntityMixin) renderer).invokeSetDoRenderBrightness(entitylivingbaseIn, partialTicks)) {
                mixcesAnimations$t.get().render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                ((IRendererLivingEntityMixin) renderer).invokeUnsetBrightness();
            }
        }
    }

    @Inject(
            method = "renderLayer",
            at = @At("TAIL")
    )
    private void mixcesAnimations$clearThreadLocalT(CallbackInfo ci) {
        /* we need to clear the threadlocal */
        mixcesAnimations$t.remove();
    }
}
