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
public class RenderGlobalMixin
{

//    @Unique private int mixcesAnimations$timer;
//
//    @Dynamic
//    @Inject(
//            method = "updateChunks",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;",
//                    ordinal = 2
//            )
//    )
//    private void mixcesAnimations$lazyChunkLoading(long finishTimeNano, CallbackInfo ci)
//    {
//        if (!MixcesAnimationsConfig.INSTANCE.enabled)
//        {
//            return;
//        }
//
//        int amount = MixcesAnimationsConfig.INSTANCE.getChunkLoading() * (100 / 6);
//
//        if (mixcesAnimations$timer > 0)
//        {
//            --mixcesAnimations$timer;
//        }
//        else
//        {
//            mixcesAnimations$timer = amount;
//        }
//    }

}
