package net.ltxprogrammer.changed.entity.beast;

import net.ltxprogrammer.changed.ability.handler.DodgeAbilityInstance;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.*;
import net.ltxprogrammer.changed.item.TscWeapon;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class AzurebyssEntity extends ChangedEntity implements GenderedEntity, PowderSnowWalkable, UndeadEntity, AzurebyssCreate{
    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PHASE3 =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean shouldBleed;
    private static final EntityDataAccessor<Boolean> EA =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> setUndyingSynced =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> isDeadSynced =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> healingChanceSynced =
            SynchedEntityData.defineId(AzurebyssEntity.class, EntityDataSerializers.INT);
    
    @Override
    public EntityDataAccessor<Boolean> setUndyingSynced() { return setUndyingSynced; };
    @Override
    public EntityDataAccessor<Boolean> isDeadSynced() { return isDeadSynced; }
    @Override
    public EntityDataAccessor<Integer> healingChanceSynced() { return healingChanceSynced; }
    @Override
    public SynchedEntityData getEntityUndeathData() { return this.entityData; }

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

        builder = builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 10D)
                .add(ChangedAttributes.SPRINT_SPEED.get(), 2.0D)
                .add(ChangedAttributes.SNEAK_SPEED.get(), 3.0D)
                .add(ChangedAttributes.AIR_CAPACITY.get(), 60.0)
                .add(ChangedAttributes.JUMP_STRENGTH.get(), 1.5D)
                .add(ChangedAttributes.FALL_RESISTANCE.get(), 2.5D);

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
        this.entityData.define(EA, false);
        defineUndeathData();
    }

    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.SPRINT_SPEED.get())).setBaseValue(2.0D);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.SNEAK_SPEED.get())).setBaseValue(2.0D);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.AIR_CAPACITY.get())).setBaseValue(60.0);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get())).setBaseValue(1.5D);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get())).setBaseValue(2.5D);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue(10D);

        Objects.requireNonNull(attributes.getInstance(Attributes.MAX_HEALTH)).setBaseValue(500);
        Objects.requireNonNull(attributes.getInstance(Attributes.FOLLOW_RANGE)).setBaseValue(64.0);
        Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(1.5);
        Objects.requireNonNull(attributes.getInstance(ForgeMod.SWIM_SPEED.get())).setBaseValue(1.1);
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
    public void push(@NotNull Entity entity) {
        // 什么都不做 = 不被挤开
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
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
            if (firstTick) {
                this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
            }
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

        if (activateElectrocutionAura()) {
            if (this.tickCount % 10 == 0) doElectrocutionAura();
            if (this.tickCount % 3 == 0) {
                if (!this.getCommandSenderWorld().isClientSide) {
                    final var sl = (ServerLevel) this.level();

                    Vec3 p = this.position().add(0, this.getBbHeight() * 0.6, 0);

                    // 每 2~4 tick 一次就够，别每 tick 疯狂喷
                    if (this.tickCount % 3 == 0) {
                        sl.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                                p.x, p.y, p.z,
                                6,              // count
                                0.55, 0.7, 0.55,// spread
                                0.2            // speed
                        );
                    }
                }
            }
        }

        tickCheck(this);
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

    public boolean activateElectrocutionAura() {
        return this.getUnderlyingPlayer() != null && this.entityData.get(EA);
    }

    public void setElectrocutionAura(boolean set) {
        this.entityData.set(EA, set);
    }

    private static final int WATER_RADIUS = 15; // blocks ~= meters
    private static final int RAIN_RADIUS  = 20;
    private static final float DMG = 3.0F; // 每秒伤害（自己调）

    private void doElectrocutionAura() {
        if (this.getCommandSenderWorld().isClientSide) return;
        final var level = (ServerLevel) this.level();

        (this.getUnderlyingPlayer() != null ? this.getUnderlyingPlayer() : this).addEffect(new MobEffectInstance(MobEffects.GLOWING, 15, 0, true, false, false));

        // 先做一个大 AABB，避免全世界遍历
        AABB box = this.getBoundingBox().inflate(Math.max(WATER_RADIUS, RAIN_RADIUS));

        // 本体的 AABB
        AABB self = this.getBoundingBox().inflate(0.3);

        // 目标集合（你可以换成 LivingEntity 或者按需过滤）
        List<LivingEntity> ts = level.getEntitiesOfClass(LivingEntity.class, box, e ->
                e.isAlive() &&
                        !e.isSpectator() &&
                        !e.isInvulnerable()
        );

        // 扫描和本体接触到的个体
        List<LivingEntity> touched = level.getEntitiesOfClass(LivingEntity.class, self, e ->
                e.isAlive() &&
                        !e.isSpectator() &&
                        !e.isInvulnerable() &&
                        e != this.getSelf() &&
                        e != this.maybeGetUnderlying()
        );

        List<LivingEntity> targets = new ArrayList<>();
        for (LivingEntity t : ts) {
            if (t == this) continue;
            if (t == this.getUnderlyingPlayer()) continue;
            targets.add(t);
        }
        if (targets.isEmpty() && touched.isEmpty()) return;

        // 1) “同一片水域”判定：只在你自己处于水中时才做 BFS
        //    BFS 成本可控：每秒一次、半径 15，访问上限你可以卡死避免炸服
        WaterConnectivity waterConn = null;
        if (this.isInWaterOrBubble()) {
            waterConn = WaterConnectivity.build(level, this.blockPosition(), WATER_RADIUS, 4096);
        }

        List<LivingEntity> validTargets = new ArrayList<>();
        for (LivingEntity t : targets) {
            double d2 = this.distanceToSqr(t);

            boolean inSameWater =
                    d2 <= (double)WATER_RADIUS * WATER_RADIUS &&
                            waterConn != null &&
                            t.isInWaterOrBubble() &&
                            waterConn.isConnected(t.blockPosition());

            boolean inRain =
                    d2 <= (double)RAIN_RADIUS * RAIN_RADIUS &&
                            level.isRainingAt(t.blockPosition().above()); // above() 更符合“淋到雨”的直觉

            if (inSameWater || inRain) {
                validTargets.add(t);
            }
        }
        for (LivingEntity t : touched) {
            if (validTargets.contains(t)) continue;
            validTargets.add(t);
        }

        for (LivingEntity t : validTargets) {
            // 可选：防止同一 tick 被多个 Azure 重复电击（看你需不需要）
            // if (alreadyZappedThisSecond(t, level.getGameTime())) continue;

            t.hurt(ChangedDamageSources.ELECTROCUTION.source(level.registryAccess()), DMG);
            TscWeapon.applyShock(t, 3);
            // markZapped(t, level.getGameTime());
            spawnArc(level,
                    (this.getUnderlyingPlayer() == null)
                            ? new Vec3(this.position().x, this.position().y + this.getBbHeight() * 0.5, this.position().z)
                            : new Vec3(
                            this.getUnderlyingPlayer().position().x,
                            this.getUnderlyingPlayer().position().y + this.getUnderlyingPlayer().getBbHeight() * 0.5,
                            this.getUnderlyingPlayer().position().z),
                    new Vec3(t.position().x, t.position().y + t.getBbHeight() * 0.5, t.position().z));
        }
    }

    private static void spawnArc(ServerLevel sl, Vec3 from, Vec3 to) {
        Vec3 diff = to.subtract(from);
        double len = diff.length();
        if (len < 0.01) return;

        Vec3 step = diff.scale(1.0 / (len * 4.0)); // 每 0.25m 一个点（你可调）
        int points = (int) Math.min(80, len * 4.0);

        Vec3 pos = from;
        for (int i = 0; i <= points; i++) {
            // 加一点随机抖动让线“抖成电弧”
            double jx = (sl.random.nextDouble() - 0.5) * 0.12;
            double jy = (sl.random.nextDouble() - 0.5) * 0.12;
            double jz = (sl.random.nextDouble() - 0.5) * 0.12;

            sl.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    pos.x + jx, pos.y + jy, pos.z + jz,
                    1, 0, 0, 0, 0
            );
            pos = pos.add(step);
        }
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
        if (tag.contains("ElectrocutionAura"))
            setElectrocutionAura(tag.getBoolean("ElectrocutionAura"));
        if (tag.contains("IsDead"))
            this.entityData.set(isDeadSynced(), tag.getBoolean("IsDead"));
        if (tag.contains("Undying"))
            this.entityData.set(setUndyingSynced(), tag.getBoolean("Undying"));
        if (tag.contains("HealingChance"))
            this.entityData.set(healingChanceSynced(), tag.getInt("HealingChance"));
        readUndeathSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Phase2", isPhase2());
        tag.putBoolean("Phase3", isPhase3());
        tag.putBoolean("Bleeding", this.shouldBleed);
        tag.putBoolean("ElectrocutionAura", activateElectrocutionAura());
        tag.putBoolean("Undying", this.entityData.get(setUndyingSynced()));
        tag.putBoolean("IsDead", this.entityData.get(isDeadSynced()));
        tag.putInt("HealingChance", this.entityData.get(healingChanceSynced()));
        addUndeathSaveData(tag);
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

    private static final class WaterConnectivity {
        private final Set<BlockPos> connected;

        private WaterConnectivity(Set<BlockPos> connected) {
            this.connected = connected;
        }

        boolean isConnected(BlockPos pos) {
            return connected.contains(pos);
        }

        static WaterConnectivity build(ServerLevel level, BlockPos start, int radius, int visitLimit) {
            // 起点必须在水里，否则直接返回空集合
            if (!isWater(level, start)) return new WaterConnectivity(Set.of());

            int r2 = radius * radius;

            ArrayDeque<BlockPos> q = new ArrayDeque<>();
            HashSet<BlockPos> visited = new HashSet<>();
            q.add(start);
            visited.add(start);

            while (!q.isEmpty() && visited.size() < visitLimit) {
                BlockPos p = q.poll();

                // 半径裁剪（球形）
                if (p.distSqr(start) > r2) continue;

                // 6 邻接（你也可以加对角 26 邻接，但成本更高）
                for (Direction dir : Direction.values()) {
                    BlockPos n = p.relative(dir);
                    if (visited.contains(n)) continue;
                    if (n.distSqr(start) > r2) continue;

                    if (isWater(level, n)) {
                        visited.add(n);
                        q.add(n);
                    }
                }
            }

            return new WaterConnectivity(Collections.unmodifiableSet(visited));
        }

        private static boolean isWater(ServerLevel level, BlockPos pos) {
            // 这里用 FluidState 更稳：水/流动水都算
            return level.getFluidState(pos).is(Fluids.WATER);
            // 如果你还想把“泡泡柱/水logged”算进去，可按需扩展
        }
    }
}