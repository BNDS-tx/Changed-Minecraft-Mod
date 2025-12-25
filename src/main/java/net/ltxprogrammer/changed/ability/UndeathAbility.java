package net.ltxprogrammer.changed.ability;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.MiningStrength;
import net.ltxprogrammer.changed.entity.UseItemMode;
import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = Changed.MODID)
public class UndeathAbility extends SimpleAbility {

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed.ability.undeath");
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getChangedEntity() instanceof AzurebyssEntity;
    }

    @Override
    public boolean canKeepUsing(IAbstractChangedEntity entity) { return false; }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (entity.getLevel().isClientSide) return;
        if (!(entity.getChangedEntity() instanceof AzurebyssEntity)) return;

        ((AzurebyssEntity) entity.getChangedEntity()).setAllowedUndeath(!((AzurebyssEntity) entity.getChangedEntity()).getAllowedUndeath());

        setDirty(entity);
        entity.displayClientMessage(new TranslatableComponent("ability.changed.undeath.select", displayUndeathCondition(entity.getChangedEntity())), true);
    }

    private TranslatableComponent displayUndeathCondition(Entity entity) {
        if (entity instanceof AzurebyssEntity) {
            if (((AzurebyssEntity) entity).getAllowedUndeath()) return new TranslatableComponent("ability.mode.changed.allow_undeath", ((AzurebyssEntity) entity).getHealingChance());
            else return new TranslatableComponent("ability.mode.changed.not_allow_undeath");
        } else return new TranslatableComponent("ability.mode.changed.not_allow_undeath");
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.saveData(tag, entity);
        tag.putString("UndeathMode", saveBoolean2String(((AzurebyssEntity) entity.getChangedEntity()).getAllowedUndeath()));
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.readData(tag, entity);
        ((AzurebyssEntity) entity.getChangedEntity()).setAllowedUndeath(setBooleanFromString(tag.getString("UndeathMode")));
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
        TranslatableComponent baseDesc = new TranslatableComponent("ability.changed.undeath.desc");

        map.put(false, List.of(baseDesc, new TranslatableComponent("ability.changed.undeath_mode.not_allowed.desc")));
        map.put(true, List.of(baseDesc, new TranslatableComponent("ability.changed.undeath_mode.allowed.desc")));
    });

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        return DESCRIPTION.get(((AzurebyssEntity) entity.getChangedEntity()).getAllowedUndeath());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (allowUndying(entity) && !allowUndying(source)) {
            if (entity.level.isClientSide) return;
            if (!allowUndying(entity)) return;
            if (((Player)entity).getHealth() - event.getAmount() > 0) return;

            triggerUndying(event, entity);
        } else if (!allowUndying(entity) && allowUndying(source)) {
            if (source.level.isClientSide) return;
            if (!(entity instanceof LivingEntity)
                    || ((LivingEntity)entity).getHealth() - event.getAmount() > 0) return;

            triggerUndyingChance(source);
        }
    }

    private static boolean allowUndying(Entity entity) {
        if (entity == null) return false;
        if (entity instanceof Player && TransfurVariant.getEntityVariant((Player)entity) != null
                && TransfurVariant.getEntityVariant((Player)entity).getEntityType() == ChangedEntities.AZUREBYSS_ENTITY.get()) {
            Optional<IAbstractChangedEntity> opt = IAbstractChangedEntity.forEitherSafe(entity);
            boolean live = false;
            if (opt.isPresent()) { live = ((AzurebyssEntity) opt.get().getChangedEntity()).getAllowedUndeath(); }
            return live;
        } else return false;
    }

    private static void triggerUndying(LivingHurtEvent event, Entity entity) {
        if (IAbstractChangedEntity.forEitherSafe(entity).isEmpty()) return;

        event.setCanceled(true);
        ((Player)entity).setHealth(1F);

        if (((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(entity).get().getChangedEntity()).isAble2Healing()) {
            ((Player)entity).removeAllEffects();
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.REGENERATION, // 回血效果
                            20 * 20,                 // 持续时间：20 秒（20 tick = 1 秒）
                            ((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(entity).get().getChangedEntity()).getHealingChance() * 2                        // 等级
                    )
            );
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.WEAKNESS,
                            5 * 20,
                            1
                    )
            );
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN,
                            5 * 20,
                            5
                    )
            );
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.JUMP,
                            5 * 20,
                            -50
                    )
            );
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.ABSORPTION,
                            5 * 20,
                            2
                    )
            );
            ((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(entity).get().getChangedEntity()).decreaseHealingChance();
        } else {
            ((Player)entity).removeAllEffects();
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.WEAKNESS,
                            Integer.MAX_VALUE,
                            10
                    )
            );
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN,
                            Integer.MAX_VALUE,
                            10
                    )
            );
            ((Player)entity).addEffect(
                    new MobEffectInstance(
                            MobEffects.JUMP,
                            Integer.MAX_VALUE,
                            -50
                    )
            );
        }
        ((ServerPlayer)entity).connection.send(new ClientboundSetTitleTextPacket(new TranslatableComponent("changed.ability.undeath.activated")));
        IAbstractChangedEntity.forEitherSafe(entity).get().displayClientMessage(
                new TranslatableComponent("changed.ability.undeath.point_remain",
                        ((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(entity).get().getChangedEntity()).getHealingChance()), true);
    }

    private static void triggerUndyingChance(Entity source) {
        if (IAbstractChangedEntity.forEitherSafe(source).isEmpty()) return;

        if (IAbstractChangedEntity.forEitherSafe(source).isPresent()
                && !((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(source).get().getChangedEntity()).isAble2Healing()) {
            ((Player)source).removeEffect(MobEffects.WEAKNESS);
            ((Player)source).removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            ((Player)source).removeEffect(MobEffects.JUMP);
        }

        ((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(source).get().getChangedEntity()).increaseHealingChance();
    }
}
