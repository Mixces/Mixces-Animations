package me.mixces.animations.mixin.krypton;

import io.netty.buffer.ByteBuf;
import me.mixces.animations.krypton.VarIntUtil;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.*;

@Mixin(PacketBuffer.class)
public abstract class PacketBufferMixin extends ByteBuf {

    @Shadow
    @Final
    private ByteBuf buf;

    /**
     * @author Andrew
     * @reason Use optimized VarInt byte size lookup table
     */
    @Overwrite
    public static int getVarIntSize(int value) {
        return VarIntUtil.getVarIntLength(value);
    }

    /**
     * @author Andrew
     * @reason optimized VarInt writing
     */
    @Overwrite
    public void writeVarIntToBuffer(int value) {
        // Peel the one and two byte count cases explicitly as they are the most common
        // VarInt sizes
        // that the proxy will write, to improve inlining.
        if ((value & (0xFFFFFFFF << 7)) == 0) {
            buf.writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            buf.writeShort(w);
        } else {
            mixcesAnimations$writeVarIntFull(buf, value);
        }
    }

    @Unique
    private static void mixcesAnimations$writeVarIntFull(ByteBuf buf, int value) {
        // See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
        if ((value & (0xFFFFFFFF << 7)) == 0) {
            buf.writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            buf.writeShort(w);
        } else if ((value & (0xFFFFFFFF << 21)) == 0) {
            int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
            buf.writeMedium(w);
        } else if ((value & (0xFFFFFFFF << 28)) == 0) {
            int w = (value & 0x7F | 0x80) << 24 | (((value >>> 7) & 0x7F | 0x80) << 16)
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | (value >>> 21);
            buf.writeInt(w);
        } else {
            int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
            buf.writeInt(w);
            buf.writeByte(value >>> 28);
        }
    }

}
