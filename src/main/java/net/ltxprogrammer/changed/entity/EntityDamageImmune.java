package net.ltxprogrammer.changed.entity;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityDamageImmune {
    @SubscribeEvent
    public static void onEntityDamage(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
//        if (entity.getCommandSenderWorld().isClientSide) return;
        if (!(entity instanceof Player player)) return;

        if (event.getSource().is(DamageTypes.LIGHTNING_BOLT)) {
            if (isThunderImmune(player)) event.setCanceled(true);
        } else if (event.getSource().is(DamageTypes.ON_FIRE)) {
            if (isOnFireImmune(player)) event.setCanceled(true); player.clearFire();
        } else if (event.getSource().is(DamageTypes.IN_FIRE)) {
            if (isInFireImmune(player)) event.setCanceled(true);
        }
    }

    public static boolean isOnFireImmune(Player player) {
        if (IAbstractChangedEntity.forEitherSafe(player).isPresent()) {
            var Variant = IAbstractChangedEntity.forEitherSafe(player).get().getSelfVariant();
            if (Variant == ChangedTransfurVariants.AZUREBYSS_ENTITY.get()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInFireImmune(Player player) {
        return false;
    }

    public static boolean isThunderImmune(Player player) {
        if (IAbstractChangedEntity.forEitherSafe(player).isPresent()) {
            var Variant = IAbstractChangedEntity.forEitherSafe(player).get().getSelfVariant();
            if (Variant == ChangedTransfurVariants.AZUREBYSS_ENTITY.get()) {
                return true;
            }
        }
        return false;
    }
}
