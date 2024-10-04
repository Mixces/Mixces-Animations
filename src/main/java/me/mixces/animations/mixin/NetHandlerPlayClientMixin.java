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

import java.util.*;

@Mixin(NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {

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
        if (MixcesAnimationsConfig.INSTANCE.getJumpReset() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (Minecraft.getMinecraft().pointedEntity == null) return; /* avoid kiting */
            if (packetIn.getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
                boolean randomChance = mixcesAnimations$random.nextFloat() < 50.0f / 100.0f; /* 50% chance */
                if (!SprintReset.getShouldJump() && randomChance) {
                    SprintReset.setShouldJump(true);
                }
            }
        }
    }
}
