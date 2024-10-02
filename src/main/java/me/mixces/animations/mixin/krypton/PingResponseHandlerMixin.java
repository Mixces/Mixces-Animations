package me.mixces.animations.mixin.krypton;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.PingResponseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PingResponseHandler.class)
public abstract class PingResponseHandlerMixin {

    @Inject(method = "channelRead", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void channelRead(ChannelHandlerContext ctx, Object msg, CallbackInfo ci) {
        if (!ctx.channel().isActive()) {
            ((ByteBuf) msg).clear();
            ci.cancel();
        }
    }
}
