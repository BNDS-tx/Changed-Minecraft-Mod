package net.ltxprogrammer.changed.ability;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.AzurebyssCreate;
import net.ltxprogrammer.changed.entity.UndeadEntity;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.network.packet.UndeathPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = Changed.MODID)
public class UndeathAbility extends SimpleAbility implements AzurebyssCreate {

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
        boolean isNotDisable = !shouldDisable(entity);
        boolean isDead = isDead(entity);
        entity.removeAllEffects();
        if (isAble2Healing) {
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 5 * 20, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, getHealingChance(entity) * 2));
        }
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 5));
        entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 5 * 20, -50));
        decreaseHealingChance(entity);
        if (isAble2Healing) {
            Changed.PACKET_HANDLER.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer)entity),
                    new UndeathPacket((Player)entity, new ItemStack(ChangedItems.ABILITY_UNDEATH_AVA.get()))
            );
        } else if (isNotDisable) {
            Changed.PACKET_HANDLER.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer)entity),
                    new UndeathPacket((Player)entity, new ItemStack(ChangedItems.ABILITY_UNDEATH_UNAVA.get()))
            );
        }
        IAbstractChangedEntity.forEitherSafe(entity).get().displayClientMessage(
                Component.empty()
                        .append((isAble2Healing ? Component.translatable("changed.ability.undeath.activated") :
                        (isNotDisable ? Component.translatable("changed.ability.undeath.activated_dead")
                                : Component.literal("......"))).withStyle(ChatFormatting.RED))
                        .append(Component.literal(" ").withStyle(ChatFormatting.RESET))
                        .append(Component.translatable(
                                "changed.ability.undeath.point_remain",
                                getHealingChance(entity)).withStyle(ChatFormatting.RESET)),
                true);
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
        return entity.getTransfurVariantInstance() != null && entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity;
    }

    private static void setAllowedUndeath(IAbstractChangedEntity entity, boolean allowedUndeath) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) undeadEntity.setAllowedUndeath(allowedUndeath);
    }

    private static boolean getAllowedUndeath(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent() && (
                getAllowedUndeath(IAbstractChangedEntity.forEitherSafe(entity).get())
        );
    }

    public static boolean getAllowedUndeath(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) return undeadEntity.getAllowedUndeath();
        else return false;
    }

    private static int getHealingChance(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent()
                ? getHealingChance(IAbstractChangedEntity.forEitherSafe(entity).get())
                : 0;
    }

    private static int getHealingChance(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) return undeadEntity.getHealingChance();
        else return 0;
    }

    private static boolean isAble2Healing(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent() && (
                isAble2Healing(IAbstractChangedEntity.forEitherSafe(entity).get())
        );
    }

    private static boolean isAble2Healing(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) return undeadEntity.isAble2Healing();
        else return false;
    }

    private static boolean shouldDisable(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent() && (
                shouldDisable(IAbstractChangedEntity.forEitherSafe(entity).get())
        );
    }

    private static boolean shouldDisable(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) return undeadEntity.shouldDisable();
        else return false;
    }

    private static boolean isDead(Entity entity) {
        return canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent() && (
                isDead(IAbstractChangedEntity.forEitherSafe(entity).get())
        );
    }

    private static boolean isDead(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) return undeadEntity.getIsDead();
        else return false;
    }

    private static void decreaseHealingChance(Entity entity) {
        if (canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent())
            decreaseHealingChance(IAbstractChangedEntity.forEitherSafe(entity).get());
    }

    private static void decreaseHealingChance(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) undeadEntity.decreaseHealingChance();
    }

    private static void increaseHealingChance(Entity entity) {
        if (canUse(entity) && IAbstractChangedEntity.forEitherSafe(entity).isPresent())
            increaseHealingChance(IAbstractChangedEntity.forEitherSafe(entity).get());
    }

    private static void increaseHealingChance(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() != null &&
                entity.getTransfurVariantInstance().getChangedEntity() instanceof UndeadEntity undeadEntity) undeadEntity.increaseHealingChance();
    }
}
