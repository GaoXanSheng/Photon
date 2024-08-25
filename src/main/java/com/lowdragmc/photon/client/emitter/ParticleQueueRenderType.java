package com.lowdragmc.photon.client.emitter;

import com.lowdragmc.photon.client.particle.LParticle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.apache.commons.lang3.ArrayUtils;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/ParticleQueueRenderType.class */
public class ParticleQueueRenderType extends PhotonParticleRenderType {
    public static final ParticleQueueRenderType INSTANCE = new ParticleQueueRenderType();
    private static final BufferBuilder[] BUFFERS = new BufferBuilder[ForkJoinPool.getCommonPoolParallelism() + 1];
    protected final Map<PhotonParticleRenderType, Queue<LParticle>> particles = new HashMap();
    private Camera camera;
    private float pPartialTicks;
    private boolean isRenderingQueue;

    static {
        for (int i = 0; i < BUFFERS.length; i++) {
            BUFFERS[i] = new BufferBuilder(256);
        }
    }

    public boolean isRenderingQueue() {
        return this.isRenderingQueue;
    }

    @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
    public void begin(BufferBuilder builder) {
        this.particles.clear();
        this.camera = null;
        this.isRenderingQueue = false;
    }

    @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
    public void end(BufferBuilder builder) {
        this.isRenderingQueue = true;
        for (Map.Entry<PhotonParticleRenderType, Queue<LParticle>> entry : this.particles.entrySet()) {
            PhotonParticleRenderType type = entry.getKey();
            Queue<LParticle> list = entry.getValue();
            if (!list.isEmpty()) {
                RenderSystem.m_157427_(GameRenderer::m_172829_);
                RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
                type.prepareStatus();
                if (type.isParallel()) {
                    ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
                    ForkJoinTask<List<BufferBuilder>> task = forkJoinPool.submit(new ParallelRenderingTask(BUFFERS, type, list.spliterator()));
                    try {
                        for (BufferBuilder buffer : task.get()) {
                            type.end(buffer);
                        }
                    } catch (Throwable ignored) {
                        try {
                            ignored.printStackTrace();
                            forkJoinPool.shutdown();
                        } finally {
                            forkJoinPool.shutdown();
                        }
                    }
                } else {
                    type.begin(builder);
                    for (LParticle particle : list) {
                        particle.m_5744_(builder, this.camera, this.pPartialTicks);
                    }
                    type.end(builder);
                }
                type.releaseStatus();
            }
        }
        this.isRenderingQueue = false;
    }

    public void pipeQueue(@Nonnull PhotonParticleRenderType type, @Nonnull Queue<LParticle> queue, Camera camera, float pPartialTicks) {
        this.particles.computeIfAbsent(type, t -> {
            return new ArrayDeque();
        }).addAll(queue);
        if (this.camera == null) {
            this.camera = camera;
            this.pPartialTicks = pPartialTicks;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/ParticleQueueRenderType$ParallelRenderingTask.class */
    class ParallelRenderingTask extends RecursiveTask<List<BufferBuilder>> {
        private final BufferBuilder[] buffers;
        private final PhotonParticleRenderType type;
        private final Spliterator<LParticle> particles;

        public ParallelRenderingTask(BufferBuilder[] buffers, PhotonParticleRenderType type, Spliterator<LParticle> particles) {
            this.buffers = buffers;
            this.type = type;
            this.particles = particles;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.RecursiveTask
        public List<BufferBuilder> compute() {
            if (this.buffers.length > 1) {
                Spliterator<LParticle> split = this.particles.trySplit();
                ForkJoinTask<List<BufferBuilder>> task1 = new ParallelRenderingTask((BufferBuilder[]) ArrayUtils.subarray(this.buffers, 0, this.buffers.length / 2), this.type, this.particles).fork();
                if (split != null) {
                    ForkJoinTask<List<BufferBuilder>> task2 = new ParallelRenderingTask((BufferBuilder[]) ArrayUtils.subarray(this.buffers, this.buffers.length / 2, this.buffers.length), this.type, split).fork();
                    ArrayList<BufferBuilder> result = new ArrayList<>(task1.join());
                    result.addAll(task2.join());
                    return result;
                }
                return task1.join();
            }
            this.type.begin(this.buffers[0]);
            this.particles.forEachRemaining(particle -> {
                particle.m_5744_(this.buffers[0], ParticleQueueRenderType.this.camera, ParticleQueueRenderType.this.pPartialTicks);
            });
            return List.of(this.buffers[0]);
        }
    }
}
