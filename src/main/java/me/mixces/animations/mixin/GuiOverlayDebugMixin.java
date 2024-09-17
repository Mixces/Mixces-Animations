package me.mixces.animations.mixin;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public class GuiOverlayDebugMixin {

    @Shadow
    @Final
    private Minecraft mc;

    @Inject(
            method = "call",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    private void mixcesAnimations$addPingElement(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();
        if (MixcesAnimationsConfig.INSTANCE.getShowPing() && MixcesAnimationsConfig.INSTANCE.enabled) {
            if (mc.isSingleplayer()) return;
            long ping = mc.getCurrentServerData().pingToServer;
            if (ping >= 0) list.add(String.format("Ping: %d ms", ping));
            else list.add("No connection :(");
        }
        cir.setReturnValue(list);
    }
}
