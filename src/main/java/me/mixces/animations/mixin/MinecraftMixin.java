package me.mixces.animations.mixin;

import me.mixces.animations.hook.MinecraftHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.client.settings.GameSettings;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public GameSettings gameSettings;
    @Shadow public GuiScreen currentScreen;
    @Shadow public boolean inGameHasFocus;
    @Shadow private int leftClickCounter;
    @Unique private boolean mixcesAnimations$leftClick;

    @ModifyVariable(
            method = "sendClickBlockToController",
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            index = 1,
            argsOnly = true
    )
    private boolean mixcesAnimations$disableLeftClickCheck(boolean original) {
        mixcesAnimations$leftClick = original;
        return !MixcesAnimationsConfig.INSTANCE.getOldDelay() || !MixcesAnimationsConfig.INSTANCE.enabled || original;


    }

    @ModifyVariable(
            method = "sendClickBlockToController",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            ),
            index = 1,
            argsOnly = true
    )
    private boolean mixcesAnimations$useCorrectLeftClickCheck(boolean original) {
        if (MixcesAnimationsConfig.INSTANCE.getOldDelay() && MixcesAnimationsConfig.INSTANCE.enabled) {
            return mixcesAnimations$leftClick;
        }
        return original;
    }

    @Redirect(
            method = "sendClickBlockToController",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"
            )
    )
    private boolean mixcesAnimations$disableUsingItemCheck(EntityPlayerSP instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.isUsingItem();
    }

    @Redirect(
            method = "sendClickBlockToController",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;swingItem()V"
            )
    )
    public void mixcesAnimations$switchSwingType(EntityPlayerSP instance) {
        if (MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() && MixcesAnimationsConfig.INSTANCE.enabled && instance.isUsingItem()) {
            MinecraftHook.swingItem(instance);
        } else {
            instance.swingItem();
        }
    }

    @Redirect(
            method = "rightClickMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"
            )
    )
    private boolean mixcesAnimations$disableIsHittingCheck(PlayerControllerMP instance) {
        return (!MixcesAnimationsConfig.INSTANCE.getOldBlockHitting() || !MixcesAnimationsConfig.INSTANCE.enabled) && instance.getIsHittingBlock();
    }

    @Inject(
            method = "runTick",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/Minecraft;thePlayer:Lnet/minecraft/client/entity/EntityPlayerSP;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V",
                            ordinal = 5
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z",
                            ordinal = 0
                    )
            )
    )
    private void mixcesAnimations$addLeftClickCheck(CallbackInfo ci) {
        if (!MixcesAnimationsConfig.INSTANCE.getOldDelay() || !MixcesAnimationsConfig.INSTANCE.enabled) { return; }
        boolean leftClick = currentScreen == null && gameSettings.keyBindAttack.isKeyDown() && inGameHasFocus;
        if (!leftClick) {
            leftClickCounter = 0;
        }
    }

}
