package net.ltxprogrammer.changed.entity;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber
public class EntityDamageImmuneNTransfurEvent implements AzurebyssCreate {
    @SubscribeEvent
    public static void onEntityDamage(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
//        if (entity.getCommandSenderWorld().isClientSide) return;
        if (!(entity instanceof Player player)) return;

        if (inFireImmune.isEmpty() && onFireImmune.isEmpty() && thunderImmune.isEmpty())
            initialImmuneEntity();

        if (event.getSource().is(DamageTypes.LIGHTNING_BOLT)) {
            if (isThunderImmune(player)) event.setCanceled(true);
        } else if (event.getSource().is(DamageTypes.ON_FIRE)) {
            if (isOnFireImmune(player)) event.setCanceled(true); player.clearFire();
        } else if (event.getSource().is(DamageTypes.IN_FIRE)) {
            if (isInFireImmune(player)) event.setCanceled(true);
        } else if (event.getSource().is(ChangedDamageSources.ELECTROCUTION.key())) {
            if (IAbstractChangedEntity.forEitherSafe(player).isPresent()
                    && IAbstractChangedEntity.forEitherSafe(player).get().getSelfVariant() == ChangedTransfurVariants.AZUREBYSS_ENTITY.get()
                    && ((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(player).get().getChangedEntity()).activateElectrocutionAura())
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void SendTransfurTexts(ProcessTransfur.EntityVariantAssigned.ChangedVariant changedVariantEvent) {
        if (changedVariantEvent.newVariant == null) {
            return;
        }
        if (changedVariantEvent.newVariant == ChangedTransfurVariants.AZUREBYSS_ENTITY.get()) {
            if (changedVariantEvent.livingEntity instanceof Player player) {
                if (player.getUUID() == UUID.fromString("71afaef1-7ece-4718-bccb-1b12fa1e37cc") ||
                        Objects.equals(player.getGameProfile().getName(), "Azurebyss")) {
                    if (player.getCommandSenderWorld().isClientSide()) {
                        player.displayClientMessage(Component.translatable("entity_dialogues.azurebyss_create.transfur.text.wb"), false);
                    }
                } else if (azurebyssPlayers.contains(player.getUUID())) {
                    if (player.getCommandSenderWorld().isClientSide()) {
                        player.displayClientMessage(Component.translatable("entity_dialogues.azurebyss_create.transfur.text"), false);
                    }
                }
            }
        }
    }

    private static final List<TransfurVariant<?>> onFireImmune = new ArrayList<>();

    private static final List<TransfurVariant<?>> inFireImmune = new ArrayList<>();

    private static final List<TransfurVariant<?>> thunderImmune = new ArrayList<>();

    private static final List<UUID> azurebyssPlayers = List.of(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            UUID.fromString("380df991-f603-344c-a090-369bad2a924a")
    );

    private static void initialImmuneEntity() {
        onFireImmune.add(ChangedTransfurVariants.AZUREBYSS_ENTITY.get());
        thunderImmune.add(ChangedTransfurVariants.AZUREBYSS_ENTITY.get());
    }

    public static boolean isOnFireImmune(Player player) {
        if (IAbstractChangedEntity.forEitherSafe(player).isPresent()) {
            var Variant = IAbstractChangedEntity.forEitherSafe(player).get().getSelfVariant();
            return onFireImmune.contains(Variant);
        }
        return false;
    }

    public static boolean isInFireImmune(Player player) {
        if (IAbstractChangedEntity.forEitherSafe(player).isPresent()) {
            var Variant = IAbstractChangedEntity.forEitherSafe(player).get().getSelfVariant();
            return inFireImmune.contains(Variant);
        }
        return false;
    }

    public static boolean isThunderImmune(Player player) {
        if (IAbstractChangedEntity.forEitherSafe(player).isPresent()) {
            var Variant = IAbstractChangedEntity.forEitherSafe(player).get().getSelfVariant();
            return thunderImmune.contains(Variant);
        }
        return false;
    }

    @SubscribeEvent
    public static void onAzurebyssDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
//        if (entity.getCommandSenderWorld().isClientSide) return;
        if (!(entity instanceof Player player)) return;
        if (IAbstractChangedEntity.forEitherSafe(player).isEmpty() ||
                IAbstractChangedEntity.forEitherSafe(player).get().getSelfVariant() != ChangedTransfurVariants.AZUREBYSS_ENTITY.get())
            return;

        if (((AzurebyssEntity)IAbstractChangedEntity.forEitherSafe(player).get().getChangedEntity()).activateElectrocutionAura())
            if (event.getSource().is(ChangedDamageSources.ELECTROCUTION.key())) {
                event.setAmount(0F);
                event.setCanceled(true);
            }
            else event.setAmount(event.getAmount() * 0.5F);
    }
}
