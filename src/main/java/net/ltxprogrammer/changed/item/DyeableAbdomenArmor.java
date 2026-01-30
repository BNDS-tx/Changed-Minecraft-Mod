package net.ltxprogrammer.changed.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;

public class DyeableAbdomenArmor extends AbdomenArmor implements DyeableLeatherItem {
    public DyeableAbdomenArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot);
    }
}
