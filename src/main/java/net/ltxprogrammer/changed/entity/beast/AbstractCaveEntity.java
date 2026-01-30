package net.ltxprogrammer.changed.entity.beast;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Random;

public abstract class AbstractCaveEntity extends ChangedEntity {
    public AbstractCaveEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    public static <T extends ChangedEntity> boolean checkEntitySpawnRules(EntityType<T> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
        if (!world.getLevel().getGameRules().getBoolean(ChangedGameRules.RULE_AUTOMATIC_SPAWN_ENTITY))
            return false;
        if (!isDarkEnoughToSpawn(world, pos, random))
            return false;
        if (pos.getY() > world.getLevel().getSeaLevel() - 10)
            return false;
        if (!checkSpawnBlock(world, reason, pos))
            return false;
        return Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }
}