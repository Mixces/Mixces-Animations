package me.mixces.animations.mixin.lazychunk;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {

    @Unique private int mixcesAnimations$timer;

    @Dynamic("OptiFine")
    @Inject(
            method = "updateChunks",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;",
                    ordinal = 2
            ),
            cancellable = true
    )
    private void mixcesAnimations$lazyChunkLoading(long finishTimeNano, CallbackInfo ci) {
        int amount = (int) MixcesAnimationsConfig.INSTANCE.getChunkLoading();
        if (amount != 1) {
            if (this.mixcesAnimations$timer > 0) {
                --this.mixcesAnimations$timer;
                ci.cancel();
            }
            this.mixcesAnimations$timer = amount;
        }
    }

}
