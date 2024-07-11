package me.mixces.animations.mixin.lazychunk;

import me.mixces.animations.config.MixcesAnimationsConfig;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(AnvilChunkLoader.class)
public abstract class AnvilChunkLoaderMixin
{

//    @Shadow private Map<ChunkCoordIntPair, NBTTagCompound> chunksToRemove;
//    @Shadow @Final public File chunkSaveLocation;
//    @Shadow(remap = false) protected abstract Object[] checkedReadChunkFromNBT__Async(World worldIn, int x, int z, NBTTagCompound p_75822_4_);
//    @Unique private static final ExecutorService mixcesAnimations$executorService = Executors.newCachedThreadPool();
//
//    @Inject(
//            method = "loadChunk",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    private void mixcesAnimations$multiThreadChunk(World worldIn, int x, int z, CallbackInfoReturnable<Chunk> cir) throws ExecutionException, InterruptedException
//    {
//        if (!MixcesAnimationsConfig.INSTANCE.getChunkMultiThread() || !MixcesAnimationsConfig.INSTANCE.enabled)
//        {
//            return;
//        }
//
//        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(x, z);
//        AtomicReference<NBTTagCompound> atomicReference = new AtomicReference<>(chunksToRemove.get(chunkCoordIntPair));
//
//        Future<?> future = mixcesAnimations$executorService.submit(() ->
//        {
//            if (atomicReference.get() == null)
//            {
//                try (DataInputStream dataInputStream = RegionFileCache.getChunkInputStream(chunkSaveLocation, x, z))
//                {
//                    if (dataInputStream != null)
//                    {
//                        atomicReference.set(CompressedStreamTools.read(dataInputStream));
//                    }
//                }
//                catch (IOException ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//            return checkedReadChunkFromNBT__Async(worldIn, x, z, atomicReference.get());
//        });
//        cir.setReturnValue((Chunk) future.get());
//    }

}
