package net.ltxprogrammer.changed.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.world.inventory.TamedDarkLatexInventoryMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TamedDarkLatexInventoryScreen extends AbstractContainerScreen<TamedDarkLatexInventoryMenu> {
    private final TamedDarkLatexInventoryMenu menu;
    private float xMouse;
    private float yMouse;

    public TamedDarkLatexInventoryScreen(TamedDarkLatexInventoryMenu menu, Inventory inventory, Component text) {
        super(menu, inventory, text);
        this.menu = menu;
        this.imageWidth = 212;
        this.imageHeight = 166;
    }

    private static final ResourceLocation texture = Changed.modResource("textures/gui/tamed_dl_inventory.png");

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);

        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;

        this.renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack ms, int gx, int gy) {
        //super.renderLabels(graphics, gx, gy);
    }

    @Override
    protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, texture);
        blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        RenderSystem.disableBlend();

        int i = this.leftPos;
        int j = this.topPos;
        InventoryScreen.renderEntityInInventory(i + 51, j + 75, 30,
                (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.menu.tamedDarkLatex);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }

        return super.keyPressed(key, b, c);
    }

    @Override
    public void containerTick() {
        super.containerTick();
    }

}