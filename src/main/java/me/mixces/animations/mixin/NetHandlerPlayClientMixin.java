package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import me.mixces.animations.hook.SprintReset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {

    @Unique
    private ScheduledExecutorService mixcesAnimations$scheduler = Executors.newScheduledThreadPool(1);

    @Unique
    private Random mixcesAnimations$random = new Random();

    @Inject(
            method = "handleEntityVelocity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"
            )
    )
    private void mixcesAnimations$prepareJump(S12PacketEntityVelocity packetIn, CallbackInfo ci) {
        if (MixcesAnimationsConfig.INSTANCE.getJumpReset() && MixcesAnimationsConfig.INSTANCE.enabled &&  packetIn.getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
            int randomDelay = mixcesAnimations$random.nextInt(40); /* 2 ticks */
            boolean randomChance = mixcesAnimations$random.nextFloat() < 50.0f / 100.0f; /* 25% chance */
            if (!randomChance) return;
            mixcesAnimations$scheduler.schedule(this::mixcesAnimations$setJump, randomDelay, TimeUnit.MILLISECONDS);
        }
    }

    @Unique
    private void mixcesAnimations$setJump() {
        SprintReset.setShouldJump(true);
    }
}
