package net.ltxprogrammer.changed.init;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class ChangedDamageSources {
    public record DamageTypeHolder(String id) {

        /* ===========================
         *  核心构造
         * =========================== */

        public DamageSource source() {
            return new DamageSource(id);
        }

        public DamageSource source(@Nullable Entity source) {
            return source == null
                    ? new DamageSource(id)
                    : new EntityDamageSource(id, source);
        }

        public String getId() {
            return id;
        }
    }

    private static DamageTypeHolder holder(String name) {
        return new DamageTypeHolder("changed:" + name);
    }

    public static final DamageTypeHolder TRANSFUR = holder("transfur");
    public static final DamageTypeHolder ABSORB = holder("absorb");
    public static final DamageTypeHolder BLOODLOSS = holder("bloodloss");
    public static final DamageTypeHolder ELECTROCUTION = holder("electrocution");
    public static final DamageTypeHolder WHITE_LATEX = holder("white_latex");
    public static final DamageTypeHolder LATEX_FLUID = holder("latex_fluid");
    public static final DamageTypeHolder PALE = holder("pale");
    public static final DamageTypeHolder FAN = holder("fan");
    public static final DamageTypeHolder HEART_ATTACK = holder("heart_attack");
    public static final DamageTypeHolder GRAB_ESCAPE = holder("grab_escape");

    public static DamageSource entityTransfur(RegistryAccess access, LivingEntity source) {
        return TRANSFUR.source(source);
    }

    public static DamageSource entityTransfur(LivingEntity source) {
        return TRANSFUR.source(source);
    }

    public static DamageSource entityTransfur(@Nullable IAbstractChangedEntity source) {
        return TRANSFUR.source(source == null ? null : source.getEntity());
    }

    public static DamageSource entityAbsorb(RegistryAccess access, LivingEntity source) {
        return ABSORB.source(source);
    }

    public static DamageSource entityAbsorb(LivingEntity source) {
        return ABSORB.source(source);
    }

    public static DamageSource entityAbsorb(@Nullable IAbstractChangedEntity source) {
        return ABSORB.source(source == null ? null : source.getEntity());
    }

    public static DamageSource bloodLoss() {
        return BLOODLOSS.source();
    }

    public static DamageSource electric(@Nullable Entity source) {
        return ELECTROCUTION.source(source);
    }
}
