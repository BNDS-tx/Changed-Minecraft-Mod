package net.ltxprogrammer.changed.ability;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = Changed.MODID)
public class UndeathAbility extends SimpleAbility {

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("changed.ability.undeath");
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return canUse(entity, true);
    }

    @Override
    public boolean canKeepUsing(IAbstractChangedEntity entity) { return false; }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (entity.getLevel().isClientSide) return;
        if (!canUse(entity)) return;

        setAllowedUndeath(entity, !getAllowedUndeath(entity));

        setDirty(entity);
        entity.displayClientMessage(Component.translatable("ability.changed.undeath.select", displayUndeathCondition(entity)), true);
    }

    private Component displayUndeathCondition(IAbstractChangedEntity entity) {
        if (canUse(entity)) {
            if (getAllowedUndeath(entity)) return Component.translatable("ability.mode.changed.allow_undeath", getHealingChance(entity));
            else return Component.translatable("ability.mode.changed.not_allow_undeath");
        } else return Component.translatable("ability.mode.changed.not_allow_undeath");
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.saveData(tag, entity);
        tag.putString("UndeathMode", saveBoolean2String(getAllowedUndeath(entity)));
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.readData(tag, entity);
        setAllowedUndeath(entity, setBooleanFromString(tag.getString("UndeathMode")));
    }

    private String saveBoolean2String(Boolean b) { if (b) return "1"; else return "0"; }

    private Boolean setBooleanFromString(String s) { return s.equals("1"); }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        super.tick(entity);
    }

    @Override
    public void stopUsing(IAbstractChangedEntity entity) {
        super.stopUsing(entity);
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 3;
    }

    private static final Map<Boolean, Collection<Component>> DESCRIPTION = Util.make(new HashMap<>(), map -> {
        Component baseDesc = Component.translatable("ability.changed.undeath.desc");

        map.put(false, List.of(baseDesc, Component.translatable("ability.changed.undeath_mode.not_allowed.desc")));
        map.put(true, List.of(baseDesc, Component.translatable("ability.changed.undeath_mode.allowed.desc")));
    });

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        return DESCRIPTION.get(getAllowedUndeath(entity));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onLivingHurt(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        Entity source = event.getSource().getDirectEntity();

        if (allowUndying(entity)) {
            if (entity.getCommandSenderWorld().isClientSide) return;

            UUID uuid = entity.getUUID();
            float currentDamage = event.getAmount();

            float alreadyAccumulated = TICK_DAMAGE_MAP.getOrDefault(uuid, 0f);
            float totalPotentialDamage = alreadyAccumulated + currentDamage;

            if (totalPotentialDamage >= entity.getHealth() - 1) {
                triggerUndying(event, entity);
                TICK_DAMAGE_MAP.put(uuid, 0f);
            } else {
                TICK_DAMAGE_MAP.put(uuid, totalPotentialDamage);
            }
        } else if (source instanceof LivingEntity && allowUndying((LivingEntity) source)) {
            if (source.getCommandSenderWorld().isClientSide) return;
            if (!(entity instanceof LivingEntity)
                    || entity.getHealth() - event.getAmount() > 0) return;

            increaseUndyingChance(source);
        }
    }

    private static final Map<UUID, Float> TICK_DAMAGE_MAP = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TICK_DAMAGE_MAP.clear();
        }
    }

    private static boolean allowUndying(LivingEntity entity) {
        if (entity == null) return false;
        if (canUse(entity)) {
            boolean live;
            live = getAllowedUndeath(entity);
            return live;
        } else return false;
    }

    private static void triggerUndying(LivingDamageEvent event, LivingEntity entity) {
        if (IAbstractChangedEntity.forEitherSafe(entity).isEmpty()) return;

        event.setAmount(0);
        event.setCanceled(true);
        entity.setHealth(1F);

        entity.invulnerableTime = 20;
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 4, false, false));
        entity.hurtDuration = 10;
        entity.hurtTime = 10;
        entity.deathTime = 0;
        entity.setDiscardFriction(false);

        entity.setLastHurtByMob(null);

        if (!canUse(entity)) return;
        boolean isAble2Healing = isAble2Healing(entity);
        entity.removeAllEffects();
        if (isAble2Healing) {
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 5 * 20, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, getHealingChance(entity) * 2));
        }
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 5));
        entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 5 * 20, -50));
        decreaseHealingChance(entity);
        ((ServerPlayer)entity).connection.send(new ClientboundSetTitleTextPacket(Component.translatable("changed.ability.undeath.activated")));
        IAbstractChangedEntity.forEitherSafe(entity).get().displayClientMessage(
                Component.translatable("changed.ability.undeath.point_remain", getHealingChance(entity)), true);
    }

    private static void increaseUndyingChance(Entity source) {
        if (IAbstractChangedEntity.forEitherSafe(source).isEmpty()) return;

        if (!canUse(source)) return;
        if (IAbstractChangedEntity.forEitherSafe(source).isPresent() && !isAble2Healing(source)) {
            ((Player)source).removeEffect(MobEffects.WEAKNESS);
            ((Player)source).removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            ((Player)source).removeEffect(MobEffects.JUMP);
        }

        increaseHealingChance(source);
    }

    private static boolean canUse(Entity entity) {
        return entity instanceof Player && IAbstractChangedEntity.forEitherSafe(entity).isPresent() && (
                canUse(IAbstractChangedEntity.forEitherSafe(entity).get(), false)
        );
    }

    private static boolean canUse(IAbstractChangedEntity entity, boolean uselessIdentifier) {
        return entity.getChangedEntity() instanceof AzurebyssEntity
                ;
    }

    private static void setAllowedUndeath(IAbstractChangedEntity entity, boolean allowedUndeath) {
        if (entity.getChangedEntity() instanceof AzurebyssEntity azurebyss) azurebyss.setAllowedUndeath(allowedUndeath);
        else return;
    }

    private static boolean getAllowedUndeath(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent() && (
                getAllowedUndeath(IAbstractChangedEntity.forEitherSafe(entity).get())
        );
    }

    private static boolean getAllowedUndeath(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AzurebyssEntity azurebyss) return azurebyss.getAllowedUndeath();
        else return false;
    }

    private static int getHealingChance(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent()
                ? getHealingChance(IAbstractChangedEntity.forEitherSafe(entity).get())
                : 0;
    }

    private static int getHealingChance(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AzurebyssEntity azurebyss) return azurebyss.getHealingChance();
        else return 0;
    }

    private static boolean isAble2Healing(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent() && (
                isAble2Healing(IAbstractChangedEntity.forEitherSafe(entity).get())
        );
    }

    private static boolean isAble2Healing(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AzurebyssEntity azurebyss) return azurebyss.isAble2Healing();
        else return false;
    }

    private static void decreaseHealingChance(Entity entity) {
        if (canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent())
            decreaseHealingChance(IAbstractChangedEntity.forEitherSafe(entity).get());
    }

    private static void decreaseHealingChance(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AzurebyssEntity azurebyss) azurebyss.decreaseHealingChance();
        else return;
    }

    private static void increaseHealingChance(Entity entity) {
        if (canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent())
            increaseHealingChance(IAbstractChangedEntity.forEitherSafe(entity).get());
    }

    private static void increaseHealingChance(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AzurebyssEntity azurebyss) azurebyss.increaseHealingChance();
        else return;
    }
}
