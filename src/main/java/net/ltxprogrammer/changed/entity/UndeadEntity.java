package net.ltxprogrammer.changed.entity;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.robot.Exoskeleton;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import java.util.Objects;

public interface UndeadEntity {

    boolean getAllowedUndeath();
    void setAllowedUndeath(boolean value);

    boolean isAble2Healing();
    boolean shouldDisable();
    int getHealingChance();

    void increaseHealingChance();
    void decreaseHealingChance();

    default Boolean setDisable(ChangedEntity entity, boolean isDead, boolean isDisabled, boolean isForced) {
        LivingEntity liveEntity = entity.maybeGetUnderlying();
        if (!(liveEntity instanceof Player)) return null;

        if (isDead != isDisabled || isForced) {
            var attributes = entity.getAttributes();

            if (!isDisabled) {
                entity.setAttributes(attributes);
            } else {
                Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(0);
                Objects.requireNonNull(attributes.getInstance(ForgeMod.SWIM_SPEED.get())).setBaseValue(0);
                Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(1);
            }

            isDead = isDisabled;

            var instance = IAbstractChangedEntity.forEitherSafe(entity.maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).orElse(null);
            if (instance != null) {
                instance.itemUseMode = !isDisabled ? UseItemMode.NORMAL : UseItemMode.NONE;
                instance.miningStrength = !isDisabled ? MiningStrength.NORMAL : MiningStrength.WEAK;

                instance.refreshAttributes();
            }
        }

        return isDead;
    }

    default Boolean tickCheck(ChangedEntity entity, boolean isDead) {
        if (entity.tickCount <=1) isDead = setDisable(entity, isDead, false, true);

        boolean hasExo = Exoskeleton.getEntityExoskeleton(entity.getUnderlyingPlayer()).isPresent();
        if (hasExo) { isDead = setDisable(entity, isDead, false, false); return isDead; }

        if (entity.getHealth() <= 4F) { if (isDead != shouldDisable()) isDead = setDisable(entity, isDead, shouldDisable(), false); }
        else { if (isDead) isDead = setDisable(entity, isDead, false, false); }

        if (isDead) {
            entity.maybeGetUnderlying().addEffect(new MobEffectInstance(MobEffects.JUMP, 5 * 20, -50));
        } else {
            if (entity.maybeGetUnderlying().hasEffect(MobEffects.JUMP)
                    && Objects.requireNonNull(entity.maybeGetUnderlying().getEffect(MobEffects.JUMP)).getAmplifier() == -50)
                entity.maybeGetUnderlying().removeEffect(MobEffects.JUMP);
        }

        return isDead;
    }
}
