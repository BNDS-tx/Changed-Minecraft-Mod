package net.ltxprogrammer.changed.entity.beast;

import net.ltxprogrammer.changed.ability.handler.DodgeAbilityInstance;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.*;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class AzurebyssEntity extends ChangedEntity {
    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PHASE3 =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean shouldBleed;
    private boolean setUndying = true;
    private int healingChance = 3;

    public AzurebyssEntity(EntityType<? extends AzurebyssEntity> type, Level level) {
        super(type, level);
        this.setAttributes(getAttributes());
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 10D);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 1.5);
        builder = builder.add(Attributes.ARMOR, 20);
        builder = builder.add(Attributes.ARMOR_TOUGHNESS, 10);
        builder = builder.add(Attributes.MAX_HEALTH, 500);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 15);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        builder = builder.add(ForgeMod.SWIM_SPEED.get(), 1.1);
        return builder;
    }

    public EntityDamageSource getThunderDmg() {
        return new EntityDamageSource(DamageSource.LIGHTNING_BOLT.getMsgId(), this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE2, false);
        this.entityData.define(PHASE3, false);
    }

    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((10));
        Objects.requireNonNull(attributes.getInstance(Attributes.MAX_HEALTH)).setBaseValue((500));
        Objects.requireNonNull(attributes.getInstance(Attributes.FOLLOW_RANGE)).setBaseValue(64.0);
        Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(1.5);
        Objects.requireNonNull(attributes.getInstance(ForgeMod.SWIM_SPEED.get())).setBaseValue((1.1));
        Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(15);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR)).setBaseValue(20);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR_TOUGHNESS)).setBaseValue(10);
        Objects.requireNonNull(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE)).setBaseValue(1);
        Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_KNOCKBACK)).setBaseValue(1);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        // 什么都不做 = 不被击退
    }

    public boolean getAllowedUndeath() { return setUndying; }

    public void setAllowedUndeath(boolean value) { setUndying = value; }

    public boolean isAble2Healing() { return healingChance > 0; }

    public int getHealingChance() { return healingChance; }

    public void increaseHealingChance() { if (healingChance < 3) healingChance++; }

    public void decreaseHealingChance() { if (healingChance > 0) healingChance--; }

    @Override
    public boolean startRiding(@NotNull Entity EntityIn, boolean force) {
        if (EntityIn instanceof Boat || EntityIn instanceof Minecart) {
            return false;
        }
        return super.startRiding(EntityIn, force);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity target) {
        if (target.getEyeY() > this.getEyeY() + 1) {
            return super.getMeleeAttackRangeSqr(target) * 1.5D;
        }
        return super.getMeleeAttackRangeSqr(target);
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    public static <T extends ChangedEntity> boolean checkEntitySpawnRules(EntityType<T> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
        return false;
    }

    @Override
    public LatexType getLatexType() {
        return LatexType.WHITE_LATEX;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return super.getAddEntityPacket();
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        if (this.getUnderlyingPlayer() != null) {
            Player playerInControl = this.getUnderlyingPlayer();
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(playerInControl);
            if (transfurVariantInstance != null) {
                DodgeAbilityInstance dodgeAbilityInstance = transfurVariantInstance.getAbilityInstance(ChangedAbilities.DODGE.get());
                if (dodgeAbilityInstance != null && dodgeAbilityInstance.getMaxDodgeAmount() < 10) {
                    dodgeAbilityInstance.setMaxDodgeAmount(10);
                    dodgeAbilityInstance.setDodgeAmount(10);
                }
            }
        }
    }

    public LivingEntity getSelf() {
        return this;
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public boolean isDamageSourceBlocked(@NotNull DamageSource pDamageSource) {
        if (pDamageSource == ChangedDamageSources.ELECTROCUTION) {
            return true;
        }
        return super.isDamageSourceBlocked(pDamageSource);
    }

    public boolean isPhase3() {
        return this.entityData.get(PHASE3);
    }

    public void setPhase3(boolean set) {
        this.entityData.set(PHASE3, set);
    }

    public boolean isPhase2() {
        return this.entityData.get(PHASE2);
    }

    public void setPhase2(boolean set) {
        this.entityData.set(PHASE2, set);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Phase2"))
            setPhase2(tag.getBoolean("Phase2"));
        if (tag.contains("Phase3"))
            setPhase3(tag.getBoolean("Phase3"));
        if (tag.contains("Bleeding"))
            shouldBleed = tag.getBoolean("Bleeding");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Phase2", isPhase2());
        tag.putBoolean("Phase3", isPhase3());
        tag.putBoolean("Bleeding", shouldBleed);
    }

    public boolean isBleeding() {
        return shouldBleed;
    }

    @Override
    protected void onEffectAdded(@NotNull MobEffectInstance mobEffectInstance, @Nullable Entity entity) {
        super.onEffectAdded(mobEffectInstance, entity);
        if (this.getUnderlyingPlayer() == null && mobEffectInstance.getEffect() == MobEffects.HEAL && this.isBleeding()) {
            this.shouldBleed = false;
        }
    }

    public void removeStatModifiers() {
        removeModifierUUID(this, Attributes.ATTACK_DAMAGE, "a06083b0-291d-4a72-85de-73bd93ffb736");
        removeModifierUUID(this, Attributes.ARMOR, "a06083b0-291d-4a72-85de-73bd93ffb737");
        removeModifierUUID(this, Attributes.ARMOR_TOUGHNESS, "a06083b0-291d-4a72-85de-73bd93ffb738");
        removeModifierUUID(this, Attributes.KNOCKBACK_RESISTANCE, "a06083b0-291d-4a72-85de-73bd93ffb739");
        //removeModifierUUID(this, Attributes.MOVEMENT_SPEED, "a06083b0-291d-4a72-85de-73bd93ffb710");
    }

    public void removeStatModifiers(LivingEntity entity) {
        removeModifierUUID(entity, Attributes.ATTACK_DAMAGE, "AttackMultiplier");
        removeModifierUUID(entity, Attributes.ARMOR, "ArmorMultiplier");
        removeModifierUUID(entity, Attributes.ARMOR_TOUGHNESS, "ArmorToughnessMultiplier");
        removeModifierUUID(entity, Attributes.KNOCKBACK_RESISTANCE, "KnockbackResistanceMultiplier");
        //removeModifierUUID(entity, Attributes.MOVEMENT_SPEED, "SpeedMultiplier");
    }

    private void removeModifier(LivingEntity entity, Attribute attribute, String modifierName) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            for (AttributeModifier modifier : instance.getModifiers()) {
                if (modifier.getName().equals(modifierName)) {
                    instance.removeModifier(modifier);
                    break; // Remove apenas um, caso haja múltiplos com o mesmo nome
                }
            }
        }
    }

    private void removeModifierUUID(LivingEntity entity, Attribute attribute, String uuid) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            for (AttributeModifier modifier : instance.getModifiers()) {
                if (modifier.getId().equals(UUID.fromString(uuid))) {
                    instance.removeModifier(modifier);
                    break; // Remove apenas um, caso haja múltiplos com o mesmo nome
                }
            }
        }
    }

    public void applyStatModifier(LivingEntity entity, double multiplier) {
        applyModifierIfAbsent(entity, Attributes.ATTACK_DAMAGE, "a06083b0-291d-4a72-85de-73bd93ffb736", "AttackMultiplier", multiplier - 1);
        applyModifierIfAbsent(entity, Attributes.ARMOR, "a06083b0-291d-4a72-85de-73bd93ffb737", "ArmorMultiplier", multiplier - 1);
        applyModifierIfAbsent(entity, Attributes.ARMOR_TOUGHNESS, "a06083b0-291d-4a72-85de-73bd93ffb738", "ArmorToughnessMultiplier", multiplier - 1);
        applyModifierIfAbsent(entity, Attributes.KNOCKBACK_RESISTANCE, "a06083b0-291d-4a72-85de-73bd93ffb739", "KnockbackResistanceMultiplier", multiplier - 1);
        //applyModifierIfAbsent(entity, Attributes.MOVEMENT_SPEED, "a06083b0-291d-4a72-85de-73bd93ffb710", "SpeedMultiplier", (multiplier - 1) * 0.5);
    }

    public void applyStatModifierAllOutPhase() {
        applyModifierIfAbsent(this, Attributes.ATTACK_DAMAGE, "a06083b0-291d-4a72-85de-73bd93ffb736", "AttackMultiplier", 0.25f);
        applyModifierIfAbsent(this, Attributes.ARMOR, "a06083b0-291d-4a72-85de-73bd93ffb737", "ArmorMultiplier", 1.25f);
        applyModifierIfAbsent(this, Attributes.ARMOR_TOUGHNESS, "a06083b0-291d-4a72-85de-73bd93ffb738", "ArmorToughnessMultiplier", 1.25f);
        applyModifierIfAbsent(this, Attributes.KNOCKBACK_RESISTANCE, "a06083b0-291d-4a72-85de-73bd93ffb739", "KnockbackResistanceMultiplier", 0.5f);
        //applyModifierIfAbsent(entity, Attributes.MOVEMENT_SPEED, "a06083b0-291d-4a72-85de-73bd93ffb710", "SpeedMultiplier", (multiplier - 1) * 0.5);
    }

    private void applyModifierIfAbsent(LivingEntity entity, Attribute attribute, String uuid, String name, double value) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance == null) return;

        UUID modifierUUID = UUID.fromString(uuid);
        if (attributeInstance.getModifier(modifierUUID) == null) { // Verifica se o modificador já existe
            attributeInstance.addTransientModifier(new AttributeModifier(modifierUUID, name, value, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    public void setSpeed(AzurebyssEntity entity) {
        AttributeModifier speedModifier = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Speed", -0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.getPose() == Pose.SWIMMING) {
            if (!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(speedModifier)) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(speedModifier);
            }
        } else {
            if (entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(speedModifier)) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(speedModifier);
            }
        }
    }

    public void crawlingSystem(LivingEntity target) {
        if (target != null) {
            setCrawlingPoseIfNeeded(target);
            crawlToTarget(target);
        } else {
            BlockPos pPos = new BlockPos(this.getX(), this.getEyeY(), this.getZ());
            BlockState blockState = this.level.getBlockState(pPos.above());

            Pose currentPose = this.getPose();
            Pose safePose = currentPose;

            if (!this.canEnterPose(currentPose)) {
                if (this.canEnterPose(Pose.STANDING)) {
                    safePose = Pose.STANDING;
                } else if (this.canEnterPose(Pose.CROUCHING)) {
                    safePose = Pose.CROUCHING;
                } else if (this.canEnterPose(Pose.SWIMMING)) {
                    safePose = Pose.SWIMMING;
                }
            }

            if (safePose != currentPose) {
                this.setPose(safePose);
                //this.refreshDimensions();
            }
        }
    }

    public void setCrawlingPoseIfNeeded(LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && this.getPose() != Pose.SWIMMING) {
            if (target.getY() < this.getEyeY() && !target.level.getBlockState(new BlockPos(target.getX(), target.getEyeY(), target.getZ()).above()).isAir()) {
                this.setPose(Pose.SWIMMING);
            }
        } else {
            if (!this.isSwimming() && this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir()) {
                this.setPose(Pose.STANDING);
            }
        }
    }

    public void crawlToTarget(LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && this.getPose() == Pose.SWIMMING) {
            Vec3 direction = target.position().subtract(this.position()).normalize();
            this.setDeltaMovement(this.getDeltaMovement().add(direction.scale(0.05)));
        }
    }

    public void updateSwimmingMovement() {
        if (this.isInWater()) {
            if (this.getTarget() != null) {
                Vec3 direction = this.getTarget().position().subtract(this.position()).normalize();
                this.setDeltaMovement(this.getDeltaMovement().add(direction.scale(0.07)));
            }
            if (this.isEyeInFluid(FluidTags.WATER)) {
                this.setPose(Pose.SWIMMING);
                this.setSwimming(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSwimming(false);
            }
        } else if (this.getPose() == Pose.SWIMMING && !this.isInWater() && (this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir() || this.canEnterPose(Pose.STANDING))) {
            this.setPose(Pose.STANDING);
        }
    }

    private void applyRampage() {
        MobEffectInstance thisEffect = this.getEffect(MobEffects.DAMAGE_BOOST);
        MobEffectInstance mobEffectInstance;
        if (thisEffect != null) {
            int pDuration = thisEffect.getDuration() + 10;
            int pAmplifier = Mth.clamp(thisEffect.getAmplifier() + 1, 0, 5);
            mobEffectInstance = new MobEffectInstance(MobEffects.DAMAGE_BOOST, pDuration, pAmplifier, thisEffect.isAmbient(), thisEffect.isVisible(), thisEffect.showIcon());
        } else {
            mobEffectInstance = new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 0, true, true, true);
        }
        this.addEffect(mobEffectInstance);
    }
}