package net.ltxprogrammer.changed.entity.ai;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 1.20+ {@link Level 的 broadcastDamageEvent} 在 1.18.2 中不存在。
 * 与 {@link LivingEntity#hurt} 中向跟踪者同步受击表现一致：{@link Level#broadcastEntityEvent}
 * 以及（除溺水外）{@link net.minecraft.world.entity.Entity#hurtMarked}。
 */
final class AssimilationDamageSync {
    private AssimilationDamageSync() {
    }

    private static byte entityEventForDamageSource(DamageSource source) {
        if (source == DamageSource.DROWN) {
            return 36;
        }
        if (source.isFire()) {
            return 37;
        }
        if (source == DamageSource.SWEET_BERRY_BUSH) {
            return 44;
        }
        if (source == DamageSource.FREEZE) {
            return 57;
        }
        return 2;
    }

    static void broadcastDamageEvent(Player player, DamageSource source) {
        if (player.level.isClientSide) {
            return;
        }
        byte status;
        if (source instanceof EntityDamageSource) {
            EntityDamageSource entitySource = (EntityDamageSource) source;
            if (entitySource.isThorns()) {
                status = 33;
            } else {
                status = entityEventForDamageSource(source);
            }
        } else {
            status = entityEventForDamageSource(source);
        }
        player.level.broadcastEntityEvent(player, status);
        if (source != DamageSource.DROWN) {
            player.hurtMarked = true;
        }
    }
}
