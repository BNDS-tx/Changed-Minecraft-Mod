package net.ltxprogrammer.changed.extension.rubidium;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.jellysquid.mods.sodium.client.model.IndexBufferBuilder;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadWinding;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.format.ModelVertexSink;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.Map;
import java.util.WeakHashMap;

public class OptimizedVertexBuilder implements VertexConsumer {
    private static final float ORIGIN = 8.0f;

    // 让同一个 ChunkModelBuilder 复用一个 VB（避免每方块 new）
    private static final Map<ChunkModelBuilder, OptimizedVertexBuilder> CACHE = new WeakHashMap<>();

    public static OptimizedVertexBuilder forBuilder(ChunkModelBuilder builder) {
        return CACHE.computeIfAbsent(builder, OptimizedVertexBuilder::new);
    }

    private final ChunkModelBuilder wrapped;
    private final ModelVertexSink sink;

    // 暂存 4 个顶点（一个 quad）
    private final float[] xs = new float[4];
    private final float[] ys = new float[4];
    private final float[] zs = new float[4];
    private final float[] us = new float[4];
    private final float[] vs = new float[4];
    private final int[] colors = new int[4];
    private final int[] lights = new int[4];

    private int indexInQuad = 0;
    private ModelQuadFacing forcedFacing;
    private ModelQuadWinding forcedWinding;

    // 当前顶点属性
    private float cx, cy, cz;
    private float cu, cv;
    private int ccolor = 0xFFFFFFFF;
    private int clight = 0;

    private OptimizedVertexBuilder(ChunkModelBuilder wrapped) {
        this.wrapped = wrapped;
        this.sink = wrapped.getVertexSink();
    }

    public void addSprite(TextureAtlasSprite sprite) {
        this.wrapped.addSprite(sprite);
    }

    public void flush() {
        this.sink.flush();
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.cx = (float) x;
        this.cy = (float) y;
        this.cz = (float) z;
        return this;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        this.ccolor = (a & 0xFF) << 24 | (b & 0xFF) << 16 | (g & 0xFF) << 8 | (r & 0xFF);
        return this;
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        this.cu = u;
        this.cv = v;
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) { return this; }

    @Override
    public VertexConsumer uv2(int u, int v) {
        this.clight = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) { return this; }

    @Override
    public void endVertex() {
        int i = indexInQuad;

        xs[i] = cx; ys[i] = cy; zs[i] = cz;
        us[i] = cu; vs[i] = cv;
        colors[i] = ccolor;
        lights[i] = clight;

        indexInQuad++;

        if (indexInQuad == 4) {
            // 1) 推断 quad facing（关键）
            ModelQuadFacing facing = forcedFacing != null ? forcedFacing : inferFacing(xs, ys, zs);
            ModelQuadWinding winding = forcedWinding != null ? forcedWinding : ModelQuadWinding.CLOCKWISE;

            // 2) 获取正确的 index buffer
            IndexBufferBuilder index = wrapped.getIndexBufferBuilder(facing);

            // 3) 写顶点（Rubidium 需要 [-8,8)）
            int base = sink.getVertexCount();
            sink.ensureCapacity(4);

            int chunkId = wrapped.getChunkId();

            for (int v = 0; v < 4; v++) {
                sink.writeVertex(
                        xs[v] - ORIGIN,
                        ys[v] - ORIGIN,
                        zs[v] - ORIGIN,
                        colors[v],
                        us[v],
                        vs[v],
                        lights[v],
                        chunkId
                );
            }

            // 4) 写索引（若你仍遇到背面剔除，可尝试 CLOCKWISE）
            index.add(base, winding);

            indexInQuad = 0;
        }
    }

    private static ModelQuadFacing inferFacing(float[] x, float[] y, float[] z) {
        // 用 v0,v1,v2 计算法线： (v1-v0) x (v2-v0)
        float ax = x[1] - x[0], ay = y[1] - y[0], az = z[1] - z[0];
        float bx = x[2] - x[0], by = y[2] - y[0], bz = z[2] - z[0];

        float nx = ay * bz - az * by;
        float ny = az * bx - ax * bz;
        float nz = ax * by - ay * bx;

        float anx = Math.abs(nx), any = Math.abs(ny), anz = Math.abs(nz);

        if (any >= anx && any >= anz) {
            return ny >= 0 ? ModelQuadFacing.UP : ModelQuadFacing.DOWN;
        } else if (anz >= anx) {
            return nz >= 0 ? ModelQuadFacing.SOUTH : ModelQuadFacing.NORTH;
        } else {
            return nx >= 0 ? ModelQuadFacing.EAST : ModelQuadFacing.WEST;
        }
    }

    @Override
    public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float[] baseBrightness, float red, float green, float blue, int[] lightmap, int overlay, boolean readExistingColor) {
        Direction direction = quad.getDirection();
        this.forcedFacing = ModelQuadFacing.fromDirection(direction);
        this.forcedWinding = inferWinding(quad, direction);

        TextureAtlasSprite sprite = quad.getSprite();
        if (sprite != null) {
            this.wrapped.addSprite(sprite);
        }

        try {
            // Ignore baked vertex colors to avoid alpha=0 quads from custom bake output.
            VertexConsumer.super.putBulkData(pose, quad, baseBrightness, red, green, blue, lightmap, overlay, false);
        } finally {
            this.forcedFacing = null;
            this.forcedWinding = null;
        }
    }

    private static ModelQuadWinding inferWinding(BakedQuad quad, Direction direction) {
        int[] data = quad.getVertices();
        int stride = DefaultVertexFormat.BLOCK.getIntegerSize();

        float x0 = Float.intBitsToFloat(data[0]);
        float y0 = Float.intBitsToFloat(data[1]);
        float z0 = Float.intBitsToFloat(data[2]);

        float x1 = Float.intBitsToFloat(data[stride]);
        float y1 = Float.intBitsToFloat(data[stride + 1]);
        float z1 = Float.intBitsToFloat(data[stride + 2]);

        float x2 = Float.intBitsToFloat(data[stride * 2]);
        float y2 = Float.intBitsToFloat(data[stride * 2 + 1]);
        float z2 = Float.intBitsToFloat(data[stride * 2 + 2]);

        float ax = x1 - x0, ay = y1 - y0, az = z1 - z0;
        float bx = x2 - x0, by = y2 - y0, bz = z2 - z0;

        float nx = ay * bz - az * by;
        float ny = az * bx - ax * bz;
        float nz = ax * by - ay * bx;

        Vec3i normal = direction.getNormal();
        float dot = nx * normal.getX() + ny * normal.getY() + nz * normal.getZ();
        return dot >= 0.0f ? ModelQuadWinding.COUNTERCLOCKWISE : ModelQuadWinding.CLOCKWISE;
    }

    @Override public void defaultColor(int r, int g, int b, int a) {}
    @Override public void unsetDefaultColor() {}
}
