package net.ltxprogrammer.changed.data;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BuiltinRepositorySource implements RepositorySource {
    private final String modId;

    private final Path modFile;
    private final boolean isJar;
    private final Set<String> packIds = new HashSet<>();
    private final PackType packType;
    private final String packsFolder;
    private static final EnumMap<PackType, String> NAMED_FOLDERS = Util.make(new EnumMap<>(PackType.class), map -> {
        map.put(PackType.CLIENT_RESOURCES, "resourcepacks");
        map.put(PackType.SERVER_DATA, "datapacks");
    });
    private static final String MCMETA = "pack.mcmeta";

//    public BuiltinRepositorySource(PackType type, String modId) throws IOException, NullPointerException {
//        this.modId = modId;
//        this.modFile = FMLLoader.getLoadingModList().getModFileById(modId).getFile().getFilePath();
//        this.packType = type;
//        this.packsFolder = NAMED_FOLDERS.getOrDefault(type, type.getDirectory());
//        var file = this.modFile.toFile();
//        if (file.isDirectory()) {
//            this.isJar = false;
//            Files.walk(this.modFile.resolve(packsFolder), 1).filter(path -> {
//                return path.resolve(MCMETA).toFile().isFile();
//            }).forEach(path -> packIds.add(path.getFileName().toString()));
//        }
//
//        else if (file.isFile()) { // Check jar file
//            this.isJar = true;
//            ZipFile jar = new ZipFile(this.modFile.toFile());
//            jar.stream().filter(ZipEntry::isDirectory).filter(entry -> {
//                return entry.getName().startsWith(packsFolder + "/") &&
//                        jar.getEntry(entry.getName() + MCMETA) != null;
//            }).forEach(entry -> packIds.add(new File(entry.getName()).getName()));
//            jar.close();
//        }
//
//        else
//            throw new IOException("Invalid mod format");
//    }
//
//    @Override
//    public void loadPacks(Consumer<Pack> out) {
//        for(String id : packIds) {
//            Pack pack = Pack.readMetaAndCreate(
//                    modId + ":" + id,
//                    new TranslatableComponent("builtin_resources." + modId + ":" + id),
//                    false,
//                    this.createSupplier(this.modFile.toFile(), id),
//                    this.packType, Pack.Position.TOP, PackSource.BUILT_IN);
//            if (pack instanceof PackExtender ext)
//                ext.setIncludeByDefault(false);
//            if (pack != null) {
//                out.accept(pack);
//            }
//        }
//    }
//
//    private Pack.ResourcesSupplier createSupplier(File file, String packName) {
//        return isJar ? name -> {
//            return new JarredPackResources(packName, file, packsFolder + "/" + packName + "/");
//        } : name -> {
//            return new PathPackResources(packName, Path.of(file.getPath(), packsFolder + "/" + packName), true);
//        };
//    }

    public BuiltinRepositorySource(PackType type, String modId) throws IOException {
        this.modId = modId;
        // 获取 Mod 文件路径
        this.modFile = FMLLoader.getLoadingModList().getModFileById(modId).getFile().getFilePath();
        this.packType = type;
        this.packsFolder = NAMED_FOLDERS.getOrDefault(type, type.getDirectory());

        File file = this.modFile.toFile();

        if (file.isDirectory()) {
            this.isJar = false;
            // 扫描文件夹
            Path folderPath = this.modFile.resolve(packsFolder);
            if (Files.exists(folderPath)) {
                Files.walk(folderPath, 1)
                        .filter(path -> Files.isRegularFile(path.resolve(MCMETA)))
                        .forEach(path -> packIds.add(path.getFileName().toString()));
            }
        } else if (file.isFile()) { // Check jar file
            this.isJar = true;
            try (ZipFile jar = new ZipFile(file)) {
                jar.stream()
                        .filter(ZipEntry::isDirectory)
                        .filter(entry -> {
                            String name = entry.getName();
                            // 简单的路径检查
                            if (!name.startsWith(packsFolder + "/")) return false;
                            // 检查是否存在 pack.mcmeta
                            return jar.getEntry(name + MCMETA) != null;
                        })
                        .forEach(entry -> {
                            // 提取文件夹名字
                            String name = entry.getName(); // e.g., "resourcepacks/mypack/"
                            String[] split = name.split("/");
                            if (split.length > 0) {
                                packIds.add(split[split.length - 1]);
                            }
                        });
            }
        } else {
            throw new IOException("Invalid mod format");
        }
    }

    @Override
    public void loadPacks(Consumer<Pack> out, Pack.PackConstructor constructor) {
        for (String id : packIds) {
            String location = modId + ":" + id;

            // 1.18.2: 构造 PackResources 的 Supplier
            Supplier<PackResources> supplier = createSupplier(this.modFile.toFile(), id);

            // 1.18.2: 使用 Pack.create
            // 参数: id, required, supplier, factory, position, source
            Pack pack = Pack.create(
                    location,
                    false, // required? 通常内置包非必须
                    supplier,
                    constructor, // PackConstructor
                    Pack.Position.TOP,
                    PackSource.BUILT_IN
            );

            if (pack != null) {
                out.accept(pack);
            }
        }
    }

    private Supplier<PackResources> createSupplier(File file, String packName) {
        if (isJar) {
            // Jar 模式：使用自定义的内部类处理 Zip 子路径
            return () -> new JarredPackResources(file, packsFolder + "/" + packName);
        } else {
            // 文件夹模式：直接使用 FolderPackResources 指向子目录
            return () -> new FolderPackResources(new File(file, packsFolder + "/" + packName));
        }
    }
}
