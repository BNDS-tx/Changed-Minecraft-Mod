package net.ltxprogrammer.changed.ability;

import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.Collections;

public class ToggleElectricalSurgeAbility extends SimpleAbility {
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
}
