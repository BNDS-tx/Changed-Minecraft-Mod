package net.ltxprogrammer.changed.aaBackport;

import net.ltxprogrammer.changed.init.ChangedTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmithingTemplateItem extends Item {
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    private final Component appliesTo;
    private final Component ingredients;
    private final Component upgradeDescription;
    private final Component baseSlotDescription;
    private final Component additionsSlotDescription;

    // 构造函数：去掉了 ResourceLocation 列表，只保留文本和 Properties
    public SmithingTemplateItem(Component appliesTo, Component ingredients, Component upgradeDescription, Component baseSlotDescription, Component additionsSlotDescription) {
        super(new Properties().tab(ChangedTabs.TAB_CHANGED_COMBAT));
        this.appliesTo = appliesTo;
        this.ingredients = ingredients;
        this.upgradeDescription = upgradeDescription;
        this.baseSlotDescription = baseSlotDescription;
        this.additionsSlotDescription = additionsSlotDescription;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // 1.18.2 的 Tooltip 渲染逻辑
        tooltip.add(this.upgradeDescription);
        tooltip.add(new TextComponent(""));

        tooltip.add((new TranslatableComponent("item.minecraft.smithing_template.applies_to")).withStyle(TITLE_FORMAT));
        tooltip.add((new TextComponent(" ")).append(this.appliesTo).withStyle(DESCRIPTION_FORMAT));

        tooltip.add((new TranslatableComponent("item.minecraft.smithing_template.ingredients")).withStyle(TITLE_FORMAT));
        tooltip.add((new TextComponent(" ")).append(this.ingredients).withStyle(DESCRIPTION_FORMAT));
    }
}