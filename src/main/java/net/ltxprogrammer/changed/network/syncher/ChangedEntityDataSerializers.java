package net.ltxprogrammer.changed.network.syncher;

import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ai.DarkLatexAttackCondition;
import net.ltxprogrammer.changed.entity.ai.DarkLatexAttackType;
import net.ltxprogrammer.changed.entity.ai.DarkLatexFavor;
import net.ltxprogrammer.changed.entity.ai.DarkLatexTargetType;
import net.ltxprogrammer.changed.entity.decoration.WallSignVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.jetbrains.annotations.NotNull;

public class ChangedEntityDataSerializers {
    public static final EntityDataSerializer<BasicPlayerInfo> BASIC_PLAYER_INFO = new EntityDataSerializer<BasicPlayerInfo>() {
        public void write(FriendlyByteBuf buffer, BasicPlayerInfo info) {
            var tag = new CompoundTag();
            info.save(tag);
            buffer.writeNbt(tag);
        }

        public BasicPlayerInfo read(FriendlyByteBuf buffer) {
            BasicPlayerInfo info = new BasicPlayerInfo();
            info.load(buffer.readNbt());
            return info;
        }

        public BasicPlayerInfo copy(BasicPlayerInfo info) {
            BasicPlayerInfo newInfo = new BasicPlayerInfo();
            newInfo.copyFrom(info);
            return newInfo;
        }
    };

//    public static final EntityDataSerializer<WallSignVariant> WALL_SIGN_VARIANT = EntityDataSerializer.simpleId(ChangedRegistry.WALL_SIGN_VARIANT.asIdMap());
//    public static final EntityDataSerializer<DarkLatexTargetType> DARK_LATEX_TARGET_TYPE = EntityDataSerializer.simpleEnum(DarkLatexTargetType.class);
//    public static final EntityDataSerializer<DarkLatexAttackType> DARK_LATEX_ATTACK_TYPE = EntityDataSerializer.simpleEnum(DarkLatexAttackType.class);
//    public static final EntityDataSerializer<DarkLatexAttackCondition> DARK_LATEX_ATTACK_CONDITION = EntityDataSerializer.simpleEnum(DarkLatexAttackCondition.class);
//    public static final EntityDataSerializer<DarkLatexFavor> DARK_LATEX_FAVOR = EntityDataSerializer.simpleEnum(DarkLatexFavor.class);

    public static final EntityDataSerializer<WallSignVariant> WALL_SIGN_VARIANT = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, @NotNull WallSignVariant value) {
            // 使用你的 RegistryHolder 获取 ID 并写入 buffer
            buffer.writeVarInt(ChangedRegistry.WALL_SIGN_VARIANT.getID(value));
        }

        @Override
        public @NotNull WallSignVariant read(FriendlyByteBuf buffer) {
            // 从 buffer 读取 ID，并通过 RegistryHolder 找回对象
            return ChangedRegistry.WALL_SIGN_VARIANT.getValue(buffer.readVarInt());
        }

        @Override
        public @NotNull WallSignVariant copy(@NotNull WallSignVariant value) {
            // 对于注册表项（通常是单例），直接返回原对象即可
            return value;
        }
    };
    public static final EntityDataSerializer<DarkLatexTargetType> DARK_LATEX_TARGET_TYPE = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, @NotNull DarkLatexTargetType value) {
            // 1.18.2 FriendlyByteBuf 直接支持写 Enum (底层就是写 ordinal)
            buffer.writeEnum(value);
        }

        @Override
        public @NotNull DarkLatexTargetType read(FriendlyByteBuf buffer) {
            // 读取时传入 Enum 的类对象即可
            return buffer.readEnum(DarkLatexTargetType.class);
        }

        @Override
        public @NotNull DarkLatexTargetType copy(@NotNull DarkLatexTargetType value) {
            // Enum 是单例且不可变的，直接返回即可
            return value;
        }
    };
    public static final EntityDataSerializer<DarkLatexAttackType> DARK_LATEX_ATTACK_TYPE = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, @NotNull DarkLatexAttackType value) {
            // 1.18.2 FriendlyByteBuf 直接支持写 Enum (底层就是写 ordinal)
            buffer.writeEnum(value);
        }

        @Override
        public @NotNull DarkLatexAttackType read(FriendlyByteBuf buffer) {
            // 读取时传入 Enum 的类对象即可
            return buffer.readEnum(DarkLatexAttackType.class);
        }

        @Override
        public @NotNull DarkLatexAttackType copy(@NotNull DarkLatexAttackType value) {
            // Enum 是单例且不可变的，直接返回即可
            return value;
        }
    };
    public static final EntityDataSerializer<DarkLatexAttackCondition> DARK_LATEX_ATTACK_CONDITION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, @NotNull DarkLatexAttackCondition value) {
            // 1.18.2 FriendlyByteBuf 直接支持写 Enum (底层就是写 ordinal)
            buffer.writeEnum(value);
        }

        @Override
        public @NotNull DarkLatexAttackCondition read(FriendlyByteBuf buffer) {
            // 读取时传入 Enum 的类对象即可
            return buffer.readEnum(DarkLatexAttackCondition.class);
        }

        @Override
        public @NotNull DarkLatexAttackCondition copy(@NotNull DarkLatexAttackCondition value) {
            // Enum 是单例且不可变的，直接返回即可
            return value;
        }
    };
    public static final EntityDataSerializer<DarkLatexFavor> DARK_LATEX_FAVOR = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, @NotNull DarkLatexFavor value) {
            // 1.18.2 FriendlyByteBuf 直接支持写 Enum (底层就是写 ordinal)
            buffer.writeEnum(value);
        }

        @Override
        public @NotNull DarkLatexFavor read(FriendlyByteBuf buffer) {
            // 读取时传入 Enum 的类对象即可
            return buffer.readEnum(DarkLatexFavor.class);
        }

        @Override
        public @NotNull DarkLatexFavor copy(@NotNull DarkLatexFavor value) {
            // Enum 是单例且不可变的，直接返回即可
            return value;
        }
    };

    static {
        EntityDataSerializers.registerSerializer(BASIC_PLAYER_INFO);
        EntityDataSerializers.registerSerializer(WALL_SIGN_VARIANT);
        EntityDataSerializers.registerSerializer(DARK_LATEX_TARGET_TYPE);
        EntityDataSerializers.registerSerializer(DARK_LATEX_ATTACK_TYPE);
        EntityDataSerializers.registerSerializer(DARK_LATEX_ATTACK_CONDITION);
        EntityDataSerializers.registerSerializer(DARK_LATEX_FAVOR);
    }
}
