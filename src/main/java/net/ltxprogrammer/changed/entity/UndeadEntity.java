package net.ltxprogrammer.changed.entity;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
import net.ltxprogrammer.changed.entity.robot.Exoskeleton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import java.util.Objects;

public interface UndeadEntity extends AzurebyssCreate {

    EntityDataAccessor<Boolean> setUndyingSynced();
    EntityDataAccessor<Boolean> isDeadSynced();
    EntityDataAccessor<Integer> healingChanceSynced();
    SynchedEntityData getEntityUndeathData();

    default boolean getIsDead() { return getEntityUndeathData().get(isDeadSynced()); }

    default boolean getAllowedUndeath() { return getEntityUndeathData().get(setUndyingSynced()); }
    default void setAllowedUndeath(boolean value) { getEntityUndeathData().set(setUndyingSynced(), value); }

    default boolean isAble2Healing() { return getEntityUndeathData().get(healingChanceSynced()) > 0;}
    default boolean shouldDisable() { return getEntityUndeathData().get(healingChanceSynced()) < 0; }
    default int getHealingChance() { return Math.max(getEntityUndeathData().get(healingChanceSynced()), 0); }

    default void increaseHealingChance() {
        if (getEntityUndeathData().get(healingChanceSynced()) <= 0)
            getEntityUndeathData().set(healingChanceSynced(), 1);
        else if (getEntityUndeathData().get(healingChanceSynced()) < 3)
            getEntityUndeathData().set(healingChanceSynced(), getEntityUndeathData().get(healingChanceSynced()) + 1);

    }
    default void decreaseHealingChance() {
        if (getEntityUndeathData().get(healingChanceSynced()) >= 0)
            getEntityUndeathData().set(healingChanceSynced(), getEntityUndeathData().get(healingChanceSynced()) - 1);
    }

    default void defineUndeathData() {
        getEntityUndeathData().define(setUndyingSynced(), true);
        getEntityUndeathData().define(isDeadSynced(), false);
        getEntityUndeathData().define(healingChanceSynced(), 3);
    }

    default void readUndeathSaveData(CompoundTag tag) {
        if (tag.contains("Undying"))
            setAllowedUndeath(tag.getBoolean("Undying"));
        if (tag.contains("HealingChance"))
            getEntityUndeathData().set(healingChanceSynced(), tag.getInt("HealingChance"));
    }
    default void addUndeathSaveData(CompoundTag tag) {
        tag.putBoolean("Undying", getAllowedUndeath());
        tag.putInt("HealingChance", getEntityUndeathData().get(healingChanceSynced()));
    }

    default boolean setDisable(ChangedEntity entity, boolean isDisabled, boolean shouldDisable, boolean isForced) {
        LivingEntity liveEntity = entity.maybeGetUnderlying();
        if (!(liveEntity instanceof Player)) return isDisabled;

        if (isDisabled != shouldDisable || isForced) {
            var attributes = entity.getAttributes();

            if (!shouldDisable) {
                entity.setAttributes(attributes);
            } else {
                Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(0);
                Objects.requireNonNull(attributes.getInstance(ForgeMod.SWIM_SPEED.get())).setBaseValue(0);
                Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(1);
            }

            var instance = IAbstractChangedEntity.forEitherSafe(entity.maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).orElse(null);
            if (instance != null) {
                instance.itemUseMode = !shouldDisable ? UseItemMode.NORMAL : UseItemMode.NONE;
                instance.miningStrength = !shouldDisable ? MiningStrength.NORMAL : MiningStrength.WEAK;

                instance.refreshAttributes();
            }
        }

        return shouldDisable;
    }

    default void tickCheck(ChangedEntity entity) {
        boolean isDead = getEntityUndeathData().get(isDeadSynced());
        if (entity.tickCount <=1) isDead = setDisable(entity, isDead, false, true);

        boolean hasExo = Exoskeleton.getEntityExoskeleton(entity.getUnderlyingPlayer()).isPresent();
        if (hasExo) {
            isDead = setDisable(entity, isDead, false, false);
            getEntityUndeathData().set(isDeadSynced(), isDead);
            return;
        }

        if (entity.getHealth() <= 4F) { if (isDead != shouldDisable()) isDead = setDisable(entity, isDead, shouldDisable(), false); }
        else { if (isDead) isDead = setDisable(entity, isDead, false, false); }

        if (isDead) {
            entity.maybeGetUnderlying().addEffect(new MobEffectInstance(MobEffects.JUMP, 5 * 20, -50));
        } else {
            if (entity.maybeGetUnderlying().hasEffect(MobEffects.JUMP)
                    && Objects.requireNonNull(entity.maybeGetUnderlying().getEffect(MobEffects.JUMP)).getAmplifier() == -50)
                entity.maybeGetUnderlying().removeEffect(MobEffects.JUMP);
        }

        getEntityUndeathData().set(isDeadSynced(), isDead);
    }
}
