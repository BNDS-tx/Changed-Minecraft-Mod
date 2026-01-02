package net.ltxprogrammer.changed.entity.beast;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.handler.DodgeAbilityInstance;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.robot.Exoskeleton;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.*;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class AzurebyssEntity extends ChangedEntity implements GenderedEntity, PowderSnowWalkable, AzurebyssCreate{
    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PHASE3 =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean shouldBleed;
    private boolean setUndying = true;
    private static final EntityDataAccessor<Boolean> setUndyingSynced =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean isDead = false;
    private static final EntityDataAccessor<Boolean> isDeadSynced =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private int healingChance = 3;
    private static final EntityDataAccessor<Integer> healingChanceSynced =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.INT);

    public AzurebyssEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedEntities.AZUREBYSS_ENTITY.get(), world);
    }

    public AzurebyssEntity(EntityType<? extends AzurebyssEntity> type, Level level) {
        super(type, level);
        this.setAttributes(getAttributes());
        xpReward = 3000;
        this.setNoAi(true);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 10D);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
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

    public DamageSource getThunderDmg() {
        DamageSource damageSource = this.getCommandSenderWorld().damageSources().lightningBolt();
        Holder<DamageType> pType = damageSource.typeHolder();
        return new DamageSource(pType, this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE2, false);
        this.entityData.define(PHASE3, false);
        this.entityData.define(setUndyingSynced, true);
        this.entityData.define(isDeadSynced, false);
        this.entityData.define(healingChanceSynced, 3);
    }

    private void syncFromServer() {
        setAllowedUndeath(this.entityData.get(setUndyingSynced));
        this.isDead = this.entityData.get(isDeadSynced);
        this.healingChance = this.entityData.get(healingChanceSynced);
    }

    private void syncToClient() {
        this.entityData.set(setUndyingSynced, getAllowedUndeath());
        this.entityData.set(isDeadSynced, this.isDead);
        this.entityData.set(healingChanceSynced, this.healingChance);
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

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        return BALD.get();
    }

    @Override
    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.MALE.getStyles();
    }

    public boolean getAllowedUndeath() { return this.setUndying; }

    public void setAllowedUndeath(boolean value) { this.setUndying = value; }

    public boolean isAble2Healing() { return this.healingChance > 0; }

    private boolean shouldDisable() { return this.healingChance < 0; }

    public int getHealingChance() { return Math.max(this.healingChance, 0); }

    public void increaseHealingChance() {
        if (this.healingChance <= 0) this.healingChance = 1;
        else if (this.healingChance < 3) this.healingChance++;
    }

    public void decreaseHealingChance() { if (this.healingChance >= 0) this.healingChance--; }

    private void setDisable(boolean isDisabled, boolean isForced) {
        LivingEntity liveEntity = this.maybeGetUnderlying();
        if (!(liveEntity instanceof Player)) return;

        if (this.isDead != isDisabled || isForced) {
            var attributes = this.getAttributes();

            if (!isDisabled) {
                setAttributes(attributes);
            } else {
                Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(0);
                Objects.requireNonNull(attributes.getInstance(ForgeMod.SWIM_SPEED.get())).setBaseValue(0);
                Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(1);
            }

            this.isDead = isDisabled;

            var instance = IAbstractChangedEntity.forEitherSafe(this.maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).orElse(null);
            if (instance != null) {
                instance.itemUseMode = !isDisabled ? UseItemMode.NORMAL : UseItemMode.NONE;
                instance.miningStrength = !isDisabled ? MiningStrength.NORMAL : MiningStrength.WEAK;

                instance.refreshAttributes();
            }
        }
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        if (this.getUnderlyingPlayer() == null) return Color3.WHITE;

        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(this.getUnderlyingPlayer());
        if (transfurVariantInstance == null) return Color3.WHITE;

        return Color3.WHITE.lerp(transfurVariantInstance.getTransfurProgression(1), Color3.getColor("#ffe6e6"));
    }

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

    public static <T extends ChangedEntity> boolean checkEntitySpawnRules(EntityType<T> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return false;
    }

    @Override
    public LatexType getLatexType() {
        return ChangedLatexTypes.WHITE_LATEX.get();
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
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
        } else {
            if (this.getTarget() != null) this.setTarget(null);
            this.setDeltaMovement(0, 0, 0);
            this.setNoAi(true);
            if (this.tickCount % 20 == 0) this.hurt(this.damageSources().generic(), 50);
        }

        if (this.getCommandSenderWorld().isClientSide()) syncFromServer();
        else syncToClient();

        if (this.tickCount <=1 ) setDisable(false, true);

        boolean hasExo = Exoskeleton.getEntityExoskeleton(this.getUnderlyingPlayer()).isPresent();
        if (hasExo) { setDisable(false, false); return; }
        if (this.getHealth() <= 4F) { if (this.isDead != shouldDisable()) setDisable(shouldDisable(), false); }
        else { if (this.isDead) setDisable(false, false); }

        if (this.isDead) {
            this.maybeGetUnderlying().addEffect(new MobEffectInstance(MobEffects.JUMP, 5 * 20, -50));
        } else {
            if (this.maybeGetUnderlying().hasEffect(MobEffects.JUMP)
                    && Objects.requireNonNull(this.maybeGetUnderlying().getEffect(MobEffects.JUMP)).getAmplifier() == -50)
                this.maybeGetUnderlying().removeEffect(MobEffects.JUMP);
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
        if (pDamageSource == ChangedDamageSources.ELECTROCUTION.source(this.getCommandSenderWorld().registryAccess())) {
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

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Phase2"))
            setPhase2(tag.getBoolean("Phase2"));
        if (tag.contains("Phase3"))
            setPhase3(tag.getBoolean("Phase3"));
        if (tag.contains("Bleeding"))
            shouldBleed = tag.getBoolean("Bleeding");
        if (tag.contains("Undying"))
            setAllowedUndeath(tag.getBoolean("Undying"));
        if (tag.contains("HealingChance"))
            healingChance = tag.getInt("HealingChance");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Phase2", isPhase2());
        tag.putBoolean("Phase3", isPhase3());
        tag.putBoolean("Bleeding", this.shouldBleed);
        tag.putBoolean("Undying", getAllowedUndeath());
        tag.putInt("HealingChance", this.healingChance);
    }

    public boolean isBleeding() {
        return this.shouldBleed;
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
        AttributeInstance speedAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute == null) return;
        if (entity.getPose() == Pose.SWIMMING) {
            if (!speedAttribute.hasModifier(speedModifier)) {
                speedAttribute.addTransientModifier(speedModifier);
            }
        } else {
            if (speedAttribute.hasModifier(speedModifier)) {
                speedAttribute.removeModifier(speedModifier);
            }
        }
    }

    public void crawlingSystem(LivingEntity target) {
        if (target != null) {
            setCrawlingPoseIfNeeded(target);
            crawlToTarget(target);
        } else {
            BlockPos pPos = new BlockPos((int) this.getX(), (int) this.getEyeY(), (int) this.getZ());
            BlockState blockState = this.getCommandSenderWorld().getBlockState(pPos.above());

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
            if (target.getY() < this.getEyeY() && !target.getCommandSenderWorld().getBlockState(new BlockPos((int) target.getX(), (int) target.getEyeY(), (int) target.getZ()).above()).isAir()) {
                this.setPose(Pose.SWIMMING);
            }
        } else {
            if (!this.isSwimming() && this.getCommandSenderWorld().getBlockState(new BlockPos((int) this.getX(), (int) this.getEyeY(), (int) this.getZ()).above()).isAir()) {
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
        } else if (this.getPose() == Pose.SWIMMING && !this.isInWater() && (this.getCommandSenderWorld().getBlockState(new BlockPos((int) this.getX(), (int) this.getEyeY(), (int) this.getZ()).above()).isAir() || this.canEnterPose(Pose.STANDING))) {
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