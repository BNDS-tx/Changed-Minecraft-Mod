package net.ltxprogrammer.changed.ability;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.AzurebyssCreate;
import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.item.TscWeapon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = Changed.MODID)
public class ToggleElectricalSurgeAbility extends SimpleAbility implements AzurebyssCreate {
    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getChangedEntity() instanceof AzurebyssEntity;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        if (entity.getChangedEntity() instanceof AzurebyssEntity azurebyss) {
            azurebyss.setElectrocutionAura(!azurebyss.activateElectrocutionAura());
        }

        setDirty(entity);
        entity.displayClientMessage(displayESCondition(entity), true);
    }

    private Component displayESCondition(IAbstractChangedEntity entity) {
        if (canUse(entity)) {
            if (getESEnable(entity)) return Component.translatable("ability.changed.toggle_electrical_surge.activated");
            else return Component.translatable("ability.changed.toggle_electrical_surge.deactivated");
        } else return Component.translatable("ability.changed.toggle_electrical_surge.deactivated");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return super.getUseType(entity);
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 20;
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("changed.ability.toggle_electrical_surge");
    }

    private static final Collection<Component> DESCRIPTION = Collections.singleton(Component.translatable("ability.changed.toggle_electrical_surge.desc"));

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        return DESCRIPTION;
    }

    public static boolean getESEnable(IAbstractChangedEntity entity) {
        return entity.getChangedEntity() instanceof AzurebyssEntity azurebyss
                && azurebyss.activateElectrocutionAura();
    }

    public static boolean getESEnable(Player player) {
        return IAbstractChangedEntity.forEitherSafe(player).isPresent() && getESEnable(IAbstractChangedEntity.forEitherSafe(player).get());
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getDirectEntity();
//        if (entity.getCommandSenderWorld().isClientSide) return;
        if (target instanceof Player player && getESEnable(player)) {
            if (event.getSource().is(ChangedDamageSources.ELECTROCUTION.key())) {
                event.setAmount(0F);
                event.setCanceled(true);
            } else event.setAmount(event.getAmount() * 0.5F);
            if (source instanceof LivingEntity livingSource) {
                livingSource.hurt(ChangedDamageSources.ELECTROCUTION.source(livingSource.level().registryAccess()), 1);
                TscWeapon.applyShock(livingSource, 3);
            }
        }

        if (source instanceof Player player && getESEnable(player)) {
            target.hurt(ChangedDamageSources.ELECTROCUTION.source(target.level().registryAccess()), 3);
            TscWeapon.applyShock(target, 3);
        }
    }
}
