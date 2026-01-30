package net.ltxprogrammer.changed.client.latexparticles;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDescription;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class LatexParticleEngine implements PreparableReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation PARTICLES_ATLAS_INFO = Changed.modResource("latex_particles");
//    private static final FileToIdConverter PARTICLE_LISTER = FileToIdConverter.json("latex_particles");

    private final Minecraft minecraft;
    private final TextureManager textureManager;
    private final Map<ResourceLocation, MutableSpriteSet> spriteSets = Maps.newHashMap();
    private final TextureAtlas textureAtlas;

    private final Map<ParticleRenderType, List<LatexParticle>> particles = new HashMap<>();
    private boolean isReloading = false;

    public LatexParticleEngine(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.textureManager = minecraft.textureManager;
        this.textureAtlas = new TextureAtlas(LatexParticleRenderType.LOCATION_PARTICLES);
        textureManager.register(LatexParticleRenderType.LOCATION_PARTICLES, textureAtlas);
    }

    public int countParticles() {
        return this.particles.values().stream().mapToInt(Collection::size).sum();
    }

    public int getMaxParticles() {
        return switch (minecraft.options.particles) {
            case ALL -> 8000;
            case DECREASED -> 2000;
            case MINIMAL -> 500;
        };
    }

    public void addParticle(LatexParticleProvider<? extends LatexParticle> particleProvider) {
        if (countParticles() >= getMaxParticles())
            return;
        var particle = particleProvider.create(this.spriteSets.get(ChangedRegistry.LATEX_PARTICLE_TYPE.getKey(particleProvider.getParticleType())));
        particles.computeIfAbsent(particle.getRenderType(), type -> new ArrayList<>()).add(particle);
    }

    private long lastLevelTick = -1;
    public boolean tick() {
        if (this.minecraft.level == null) {
            particles.clear();
            return false;
        }

        if (pauseForReload())
            return false;

        if (this.minecraft.level.getGameTime() == lastLevelTick)
            return false;
        lastLevelTick = this.minecraft.level.getGameTime();
        // Only proceed with particles tick if the level ticked

        for (var particleSet : particles.values())
            for (var particle : particleSet)
                particle.tick();

        particles.values().forEach(particleSet -> {
            particleSet.removeIf(LatexParticle::shouldExpire);
        });

        return true;
    }

    public void purgeParticles() {
        particles.clear();
    }

    public List<LatexParticle> getAllParticlesForEntity(Entity entity) {
        List<LatexParticle> result = new ArrayList<>();

        for (var particleSet : particles.values())
            for (var particle : particleSet)
                if (particle.isForEntity(entity))
                    result.add(particle);

        return result;
    }

    public void render(PoseStack poseStack, LightTexture lightTexture, Camera camera, float partialTicks, @Nullable Frustum clippingHelper, SetupContext context) {
        lightTexture.turnOnLightLayer();
        RenderSystem.enableDepthTest();
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE2);
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        RenderSystem.applyModelViewMatrix();

        for(ParticleRenderType particlerendertype : this.particles.keySet()) {
            if (particlerendertype == ParticleRenderType.NO_RENDER) continue;
            var particleSet = this.particles.get(particlerendertype);
            if (particleSet != null) {
                RenderSystem.setShader(GameRenderer::getParticleShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tesselator.getBuilder();
                particlerendertype.begin(bufferbuilder, this.textureManager);

                for(var particle : particleSet) {
                    if (particle.getRenderType() != particlerendertype) continue;
                    if (clippingHelper != null && particle.shouldCull() && !clippingHelper.isVisible(particle.getBoundingBox())) continue;
                    try {
                        particle.renderFromEvent(bufferbuilder, camera, partialTicks, context);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering Latex Particle");
                        CrashReportCategory crashreportcategory = crashreport.addCategory("Latex Particle being rendered");
                        crashreportcategory.setDetail("LatexParticle", particle::toString);
                        crashreportcategory.setDetail("LatexParticle Type", particlerendertype::toString);
                        throw new ReportedException(crashreport);
                    }
                }

                particlerendertype.end(tesselator);
            }
        }

        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
    }

    private void clearParticles() {
        this.particles.clear();
    }

//    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier prepBarrier, ResourceManager resourceManager,
//                                          ProfilerFiller profilerA, ProfilerFiller profilerB, Executor execA, Executor execB) {
//        @OnlyIn(Dist.CLIENT)
//        record ParticleDefinition(ResourceLocation id, Optional<List<ResourceLocation>> sprites) { }
//        CompletableFuture<List<ParticleDefinition>> completablefuture = CompletableFuture.supplyAsync(() -> {
//            return PARTICLE_LISTER.listMatchingResources(resourceManager);
//        }, execA).thenCompose((resources) -> {
//            List<CompletableFuture<ParticleDefinition>> list = new ArrayList<>(resources.size());
//            resources.forEach((p_247903_, resource) -> {
//                ResourceLocation resourcelocation = PARTICLE_LISTER.fileToId(p_247903_);
//                list.add(CompletableFuture.supplyAsync(() -> {
//                    this.spriteSets.put(resourcelocation, new MutableSpriteSet());
//                    return new ParticleDefinition(resourcelocation, this.loadParticleDescription(resourcelocation, resource));
//                }, execA));
//            });
//            return Util.sequence(list);
//        });
//        CompletableFuture<SpriteLoader.Preparations> completablefuture1 = SpriteLoader.create(this.textureAtlas).loadAndStitch(resourceManager, PARTICLES_ATLAS_INFO, 0, execA).thenCompose(SpriteLoader.Preparations::waitForUpload);
//
//        return CompletableFuture.allOf(completablefuture1, completablefuture).thenCompose(prepBarrier::wait).thenAcceptAsync((p_247900_) -> {
//            this.clearParticles();
//            profilerB.startTick();
//            profilerB.push("upload");
//            SpriteLoader.Preparations spriteloader$preparations = completablefuture1.join();
//            this.textureAtlas.upload(spriteloader$preparations);
//            profilerB.popPush("bindSpriteSets");
//            Set<ResourceLocation> set = new HashSet<>();
//            TextureAtlasSprite textureatlassprite = spriteloader$preparations.missing();
//            completablefuture.join().forEach((definition) -> {
//                Optional<List<ResourceLocation>> optional = definition.sprites();
//                if (!optional.isEmpty()) {
//                    List<TextureAtlasSprite> list = new ArrayList<>();
//
//                    for(ResourceLocation resourcelocation : optional.get()) {
//                        TextureAtlasSprite textureatlassprite1 = spriteloader$preparations.regions().get(resourcelocation);
//                        if (textureatlassprite1 == null) {
//                            set.add(resourcelocation);
//                            list.add(textureatlassprite);
//                        } else {
//                            list.add(textureatlassprite1);
//                        }
//                    }
//
//                    if (list.isEmpty()) {
//                        list.add(textureatlassprite);
//                    }
//
//                    this.spriteSets.get(definition.id()).rebind(list);
//                }
//            });
//            if (!set.isEmpty()) {
//                LOGGER.warn("Missing particle sprites: {}", set.stream().sorted().map(ResourceLocation::toString).collect(Collectors.joining(",")));
//            }
//
//            profilerB.pop();
//            profilerB.endTick();
//        }, execB);
//    }

//    private Optional<List<ResourceLocation>> loadParticleDescription(ResourceLocation location, Resource p_248793_) {
//        if (!this.spriteSets.containsKey(location)) {
//            LOGGER.debug("Redundant texture list for particle: {}", (Object)location);
//            return Optional.empty();
//        } else {
//            try (Reader reader = p_248793_.openAsReader()) {
//                ParticleDescription particledescription = ParticleDescription.fromJson(GsonHelper.parse(reader));
//                return Optional.of(particledescription.getTextures());
//            } catch (IOException ioexception) {
//                throw new IllegalStateException("Failed to load description for particle " + location, ioexception);
//            }
//        }
//    }

    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier prepBarrier, ResourceManager resourceManager,
                                          ProfilerFiller profilerA, ProfilerFiller profilerB, Executor execA, Executor execB) {

        // 1. 定义临时数据结构
        @OnlyIn(Dist.CLIENT)
        record ParticleDefinition(ResourceLocation id, Optional<List<ResourceLocation>> sprites) { }

        // 2. 第一阶段异步：加载 JSON 描述文件
        CompletableFuture<List<ParticleDefinition>> loadDefinitionsFuture = CompletableFuture.supplyAsync(() -> {
            List<ParticleDefinition> definitions = new ArrayList<>();
            String directory = "latex_particles"; // 对应原版的 FileToIdConverter

            // 1.18.2 listResources 返回 Collection<ResourceLocation> (文件路径)
            Collection<ResourceLocation> resources = resourceManager.listResources(directory, (filename) -> filename.endsWith(".json"));

            for (ResourceLocation fileLoc : resources) {
                // 手动模拟 fileToId 逻辑：去除前缀目录和后缀 .json
                String path = fileLoc.getPath();
                String idPath = path.substring(directory.length() + 1, path.length() - ".json".length());
                ResourceLocation id = new ResourceLocation(fileLoc.getNamespace(), idPath);

                try {
                    // 1.18.2 获取资源
                    Resource resource = resourceManager.getResource(fileLoc);
                    // 加载描述
                    Optional<List<ResourceLocation>> sprites = this.loadParticleDescription(id, resource);

                    // 这里需要同步访问 this.spriteSets 或者稍后处理，建议此时只收集数据
                    // 1.20原代码在这里就 put 了，我们也保持一致，但要注意线程安全（如果是 ConcurrentHashMap 则没问题）
                    // 如果 this.spriteSets 不是线程安全的，建议放到主线程处理
                    // 这里假设你是为了收集所有用到的 sprite
                    definitions.add(new ParticleDefinition(id, sprites));

                } catch (IOException e) {
                    LOGGER.error("Failed to load particle definition {}", id, e);
                }
            }
            return definitions;
        }, execA);

        // 3. 第二阶段异步：缝合纹理 (Stitch)
        // 必须等待 JSON 加载完毕，因为我们需要知道到底要缝合哪些纹理
        CompletableFuture<TextureAtlas.Preparations> stitchFuture = loadDefinitionsFuture.thenApplyAsync((definitions) -> {
            profilerA.startTick();
            profilerA.push("stitching");

            // 收集所有 JSON 中引用的纹理 ID
            Set<ResourceLocation> allSpritesToStitch = new HashSet<>();
            for (ParticleDefinition def : definitions) {
                def.sprites().ifPresent(allSpritesToStitch::addAll);
            }

            // 如果有其他硬编码的纹理源 (原代码中的 PARTICLES_ATLAS_INFO)，也在这里添加
            // allSpritesToStitch.add(...)

            // 1.18.2 的缝合 API
            // 注意：0 是 mipLevel，根据你的需求调整
            TextureAtlas.Preparations preparations = this.textureAtlas.prepareToStitch(resourceManager, allSpritesToStitch.stream(), profilerA, 0);

            profilerA.pop();
            profilerA.endTick();
            return preparations;
        }, execA);

        // 4. 等待所有异步任务完成，进入同步阶段
        return CompletableFuture.allOf(stitchFuture, loadDefinitionsFuture).thenCompose(prepBarrier::wait).thenAcceptAsync((voidResult) -> {
            this.clearParticles();
            profilerB.startTick();
            profilerB.push("upload");

            // 获取缝合结果
            TextureAtlas.Preparations preparations = stitchFuture.join();
            // 1.18.2 上传纹理
            this.textureAtlas.reload(preparations);

            profilerB.popPush("bindSpriteSets");

            // 重新初始化 spriteSets (如果之前只是占位)
            List<ParticleDefinition> definitions = loadDefinitionsFuture.join();

            // 处理纹理绑定逻辑 (移植自原代码)
            Set<ResourceLocation> missingSprites = new HashSet<>();
            // 1.18.2 获取丢失材质的方法
            TextureAtlasSprite missingSprite = this.textureAtlas.getSprite(MissingTextureAtlasSprite.getLocation());

            for (ParticleDefinition definition : definitions) {
                // 在这里安全地放入 map (主线程)
                this.spriteSets.put(definition.id(), new MutableSpriteSet());

                Optional<List<ResourceLocation>> optional = definition.sprites();
                if (optional.isPresent()) { // !optional.isEmpty() -> isPresent()
                    List<TextureAtlasSprite> spriteList = new ArrayList<>();

                    for (ResourceLocation spriteLoc : optional.get()) {
                        // 1.18.2 从 preparations 获取 sprite
                        TextureAtlasSprite sprite = this.textureAtlas.getSprite(spriteLoc); // 直接访问 regions 字段

                        if (sprite.getName().equals(MissingTextureAtlasSprite.getLocation()) && !spriteLoc.equals(MissingTextureAtlasSprite.getLocation())) {
                            missingSprites.add(spriteLoc);
                            spriteList.add(missingSprite);
                        } else {
                            spriteList.add(sprite);
                        }
                    }

                    if (spriteList.isEmpty()) {
                        spriteList.add(missingSprite);
                    }

                    this.spriteSets.get(definition.id()).rebind(spriteList);
                }
            }

            if (!missingSprites.isEmpty()) {
                LOGGER.warn("Missing particle sprites: {}", missingSprites.stream().sorted().map(ResourceLocation::toString).collect(Collectors.joining(",")));
            }

            profilerB.pop();
            profilerB.endTick();
        }, execB);
    }

    // 辅助方法移植
    private Optional<List<ResourceLocation>> loadParticleDescription(ResourceLocation location, Resource resource) {
        // 1.20 原逻辑检查了 key 是否存在，这里根据你的需求保留或删除
        // if (!this.spriteSets.containsKey(location)) ...

        // 1.18.2 资源读取方式
        try (InputStream inputStream = resource.getInputStream();
             Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            ParticleDescription particledescription = ParticleDescription.fromJson(GsonHelper.parse(reader));
            return Optional.of(particledescription.getTextures());

        } catch (IOException ioexception) {
            throw new IllegalStateException("Failed to load description for particle " + location, ioexception);
        }
    }

    public void close() {
        this.textureAtlas.clearTextureData();
    }

    public boolean pauseForReload() {
        return isReloading;
    }

    @OnlyIn(Dist.CLIENT)
    static class MutableSpriteSet implements SpriteSet {
        private List<TextureAtlasSprite> sprites;

        public TextureAtlasSprite get(int p_107413_, int p_107414_) {
            return this.sprites.get(p_107413_ * (this.sprites.size() - 1) / p_107414_);
        }

        public TextureAtlasSprite get(Random p_107418_) {
            return this.sprites.get(p_107418_.nextInt(this.sprites.size()));
        }

        public void rebind(List<TextureAtlasSprite> p_107416_) {
            this.sprites = ImmutableList.copyOf(p_107416_);
        }
    }
}
