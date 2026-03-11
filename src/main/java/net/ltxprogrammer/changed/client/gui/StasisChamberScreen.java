package net.ltxprogrammer.changed.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.block.entity.StasisChamberBlockEntity;
import net.ltxprogrammer.changed.entity.ModifiableEntity;
import net.ltxprogrammer.changed.entity.ModificationVector;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.world.inventory.StasisChamberMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.ByteTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StasisChamberScreen extends AbstractContainerScreen<StasisChamberMenu> {
    private final StasisChamberMenu menu;
    private @Nullable Runnable toolTip = null;

    public StasisChamberScreen(StasisChamberMenu menu, Inventory inventory, Component text) {
        super(menu, inventory, text);
        this.menu = menu;
        this.imageWidth = 285;
        this.imageHeight = 166;

        menu.requestUpdate();
    }

    public void setToolTip(Runnable fn) {
        this.toolTip = fn;
    }

    private static final ResourceLocation texture = Changed.modResource("textures/gui/stasis_chamber.png");
    private static final ResourceLocation GENDER_SWITCH_LOCATION = Changed.modResource("textures/gui/gender_switch.png");

    private ButtonScope buttonScope = ButtonScope.DEFAULT;
    private ModifyType modifyType = ModifyType.INTERFACE;

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.menu.hideSlots = buttonScope != ButtonScope.DEFAULT;

        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);

        final int textLeftMargin = this.leftPos + 176;
        final int textRightMargin = this.leftPos + 278;
        final int textTopMargin = this.topPos + 6;
        final int commandsTopMargin = this.topPos + 24;
        final int commandsBottomMargin = this.topPos + 136;

        if (buttonScope == ButtonScope.PROGRAMS) {
            menu.getChamberedEntity().ifPresent(entity -> {
                Gui.drawCenteredString(ms, Minecraft.getInstance().font, entity.getDisplayName(), (textLeftMargin + textRightMargin) / 2, textTopMargin, 0xFFFFFF);
            });

            AtomicInteger yOffset = new AtomicInteger(0);
            menu.getCurrentCommand().ifPresent(command -> {
                Gui.drawString(ms, Minecraft.getInstance().font, command.getActiveDisplayText(), textLeftMargin, commandsTopMargin + yOffset.getAndAdd(12), 0x20FF20);
            });
            menu.getScheduledCommands().forEach(command -> {
                Gui.drawString(ms, Minecraft.getInstance().font, command.getDisplayText(), textLeftMargin, commandsTopMargin + yOffset.getAndAdd(12), 0xFFFFFF);
            });
        }

        else {
            menu.getChamberedEntity().ifPresent(entity -> {
                Gui.drawCenteredString(ms, Minecraft.getInstance().font, entity.getDisplayName(), (textLeftMargin + textRightMargin) / 2, textTopMargin, 0xFFFFFF);

                int entityX = (textLeftMargin + textRightMargin) / 2;
                int entityY = commandsBottomMargin - 5;
                InventoryScreen.renderEntityInInventory(entityX, entityY, 40,
                        (float)(this.leftPos) - mouseX + entityX,
                        (float)(this.topPos) - mouseY + (entityY - 100),
                        entity);
            });
        }

        this.renderTooltip(ms, mouseX, mouseY);

        if (this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            if (menu.getCustomSlot(0) == this.hoveredSlot) {
                setToolTip(() -> this.renderTooltip(ms, new TranslatableComponent("changed.stasis.slot.variant.tooltip"), mouseX, mouseY));
            }

            else if (menu.getCustomSlot(1) == this.hoveredSlot) {
                setToolTip(() -> this.renderTooltip(ms, new TranslatableComponent("changed.stasis.slot.fluid.tooltip"), mouseX, mouseY));
            }
        }


        if (toolTip != null) {
            if (this.menu.getCarried().isEmpty()) {
                toolTip.run();
            }
            toolTip = null;
        }
    }

    @Override
    protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, texture);
        blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth + 18, this.imageHeight);

        this.menu.slots.forEach(slot -> {
            if (slot.isActive()) {
                blit(ms, this.leftPos + slot.x - 1, this.topPos + slot.y - 1, this.imageWidth, 0, 18, 18, this.imageWidth + 18, this.imageHeight);
            }
        });

        RenderSystem.setShaderTexture(0, Changed.modResource("textures/gui/progress_bar_back.png"));
        blit(ms, this.leftPos + 217, this.topPos + 144, 0, 0, 48, 12, 48, 12);

        float progress = menu.getFluidLevel(partialTicks);
        RenderSystem.setShaderTexture(0, Changed.modResource("textures/gui/progress_bar_front.png"));
        blit(ms, this.leftPos + 217, this.topPos + 144, 0, 0, (int)(48 * progress), 12, 48, 12);

        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }

        return super.keyPressed(key, b, c);
    }

    public enum ButtonScope {
        DEFAULT,
        PROGRAMS,
        MODIFICATIONS;

        public ButtonScope toggleScope(ButtonScope intendedScope) {
            if (this == DEFAULT)
                return intendedScope;
            if (this == intendedScope)
                return DEFAULT;

            return intendedScope;
        }
    }

    public enum ModifyType {
        NONE,
        INTERFACE,
        GENDERED
    }

    public record PaginatedWidget(int pageIndex, AbstractWidget widget) {}

    private boolean initialized = false;
    private Button programsButton;
    private Button modifyButton;
    private Button openDoorButton;
    private Button closeDoorButton;

    private List<AbstractWidget> programWidgets = new ArrayList<>();
    private AbstractSliderButton waitDurationSlider;
    private ModifiableEntity lastSeenEntityForModification = null;
    private List<PaginatedWidget> modificationWidgets = new ArrayList<>();
    private int modificationsPageIndex = 0;
    private int modificationsPageCount = 1;
    private Button genderButton;

    @Override
    public void containerTick() {
        super.containerTick();

        checkButtonStates();
    }

    private void checkButtonStates() {
        if (!initialized) return;

        boolean open = menu.isChamberOpen();
        float fillLevel = menu.getChamberFillLevel(Minecraft.getInstance().getFrameTime());
        var fillFluid = menu.getChamberFillFluid();
        var chamberedEntity = menu.getChamberedEntity();

        programsButton.active = true;
        modifyButton.active = true;
        openDoorButton.active = !open && fillLevel <= 0f;
        openDoorButton.visible = buttonScope == ButtonScope.DEFAULT;
        closeDoorButton.active = open;
        closeDoorButton.visible = buttonScope == ButtonScope.DEFAULT;

        modifyType = menu.getChamberedLatex().map(entity -> {
            if (entity.getChangedEntity() instanceof ModifiableEntity modifiable) {
                if (this.lastSeenEntityForModification != modifiable) {
                    modificationWidgets.forEach(entry -> this.removeWidget(entry.widget));
                    modificationWidgets.clear();
                    this.createModificationWidgets(modifiable);
                }
                return ModifyType.INTERFACE;
            }
            else if (ChangedTransfurVariants.Gendered.getOpposite(entity.getSelfVariant()).isPresent())
                return ModifyType.GENDERED;
            return ModifyType.NONE;
        }).orElse(ModifyType.NONE);

        programWidgets.forEach(button -> button.visible = buttonScope == ButtonScope.PROGRAMS);
        genderButton.visible = buttonScope == ButtonScope.MODIFICATIONS && modifyType == ModifyType.GENDERED;
        modificationWidgets.forEach(entry -> {
            entry.widget.visible = (buttonScope == ButtonScope.MODIFICATIONS && modifyType == ModifyType.INTERFACE) &&
                modificationsPageIndex == entry.pageIndex;
        });
    }

    protected void createModificationWidgets(ModifiableEntity entity) {
        this.lastSeenEntityForModification = entity;

        int leftMargin = this.leftPos + 7;
        int topMargin = this.topPos + 7;
        int buttonWidth = 79;
        int buttonHeight = 20;
        int buttonHeightSpacing = buttonHeight + 4;
        int buttonWidthSpacing = buttonWidth + 4;
        int buttonWidthLong = buttonWidth + buttonWidthSpacing;

        Map<String, ModificationVector.EnumVector> enumVectors = new HashMap<>();
        Map<String, ModificationVector.LinearVector> linearVectors = new HashMap<>();
        Map<String, ModificationVector.BooleanVector> booleanVectors = new HashMap<>();

        entity.getModificationVectors().forEach((key, vector) -> {
            if (vector instanceof ModificationVector.EnumVector enumVector)
                enumVectors.put(key, enumVector);
            if (vector instanceof ModificationVector.LinearVector linearVector)
                linearVectors.put(key, linearVector);
            if (vector instanceof ModificationVector.BooleanVector booleanVector)
                booleanVectors.put(key, booleanVector);
        });

        final AtomicInteger widgetIndex = new AtomicInteger(2);
        final AtomicInteger pageIndex = new AtomicInteger(0);

        Runnable createNewPage = () -> {
            widgetIndex.set(2);
            final int nextPage = pageIndex.get() + 1;
            final int prevPage = pageIndex.get();

            modificationWidgets.add(new PaginatedWidget(prevPage, this.addRenderableWidget(
                    new Button(
                            leftMargin + buttonWidthSpacing + buttonWidth - buttonHeight,
                            topMargin + buttonHeightSpacing * 5 + (buttonHeight / 2),
                            buttonHeight,
                            buttonHeight,
                            new TextComponent(">"),
                            b -> modificationsPageIndex = nextPage
                    )
            )));
            modificationWidgets.add(new PaginatedWidget(nextPage, this.addRenderableWidget(
                    new Button(
                            leftMargin,
                            topMargin + buttonHeightSpacing * 5 + (buttonHeight / 2),
                            buttonHeight,
                            buttonHeight,
                            new TextComponent("<"),
                            b -> modificationsPageIndex = prevPage
                    )
            )));

            pageIndex.incrementAndGet();
        };

        for (var entry : enumVectors.entrySet()) {
            if (widgetIndex.get() >= 10)
                createNewPage.run();

            final var key = entry.getKey();
            final var vector = entry.getValue();
            var tooltip = vector.getTooltipText();

            modificationWidgets.add(new PaginatedWidget(pageIndex.get(), this.addRenderableWidget(
                    new Button(
                            leftMargin + (widgetIndex.get() % 2 == 1 ? buttonWidthSpacing : 0),
                            topMargin + buttonHeightSpacing * (widgetIndex.get() / 2),
                            buttonWidth,
                            buttonHeight,
                            vector.getDisplayText(),
                            b -> {
                                vector.cycleForward();
                                menu.inputModification(key, vector.writeAsTag());
                                b.setMessage(vector.getDisplayText());
                            }
                    ) {
                        @Override
                        public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                            super.render(poseStack, mouseX, mouseY, partialTicks);
                            if (tooltip != null && this.isMouseOver(mouseX, mouseY)) {
                                Screen screen = Minecraft.getInstance().screen;
                                if (screen != null) {
                                    screen.renderTooltip(poseStack, new TextComponent(tooltip.getContents()), mouseX, mouseY);
                                }
                            }
                        }
                    }
            )));

            widgetIndex.addAndGet(1);
        }

        for (var entry : linearVectors.entrySet()) {
            widgetIndex.addAndGet(widgetIndex.get() % 2);
            if (widgetIndex.get() >= 10)
                createNewPage.run();

            final var key = entry.getKey();
            final var vector = entry.getValue();
            var tooltip = vector.getTooltipText();

            var slider = new AbstractSliderButton(
                    leftMargin,
                    topMargin + buttonHeightSpacing * (widgetIndex.get() / 2),
                    buttonWidthLong, buttonHeight, vector.getDisplayText(), vector.getValue()) {
                @Override
                protected void updateMessage() {
                    setMessage(vector.getDisplayText());
                }

                @Override
                protected void applyValue() {
                    vector.acceptValue(this.value);
                    menu.inputModification(key, vector.writeAsTag());
                }

                @Override
                public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                    super.render(poseStack, mouseX, mouseY, partialTicks);
                    if (tooltip != null && this.isMouseOver(mouseX, mouseY)) {
                        Screen screen = Minecraft.getInstance().screen;
                        if (screen != null) {
                            screen.renderTooltip(poseStack, new TextComponent(tooltip.getContents()), mouseX, mouseY);
                        }
                    }
                }
            };

            modificationWidgets.add(new PaginatedWidget(pageIndex.get(), this.addRenderableWidget(slider)));

            widgetIndex.addAndGet(2);
        }

        for (var entry : booleanVectors.entrySet()) {
            widgetIndex.addAndGet(widgetIndex.get() % 2);
            if (widgetIndex.get() >= 10)
                createNewPage.run();

            final var key = entry.getKey();
            final var vector = entry.getValue();
            var tooltip = vector.getTooltipText();

            var checkbox = new Checkbox(
                    leftMargin,
                    topMargin + buttonHeightSpacing * (widgetIndex.get() / 2),
                    buttonWidthLong, buttonHeight, vector.getDisplayText(), vector.getState()) {
                @Override
                public void onPress() {
                    super.onPress();
                    vector.acceptState(this.selected());
                    menu.inputModification(key, vector.writeAsTag());
                }

                @Override
                public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                    super.render(poseStack, mouseX, mouseY, partialTicks);
                    if (tooltip != null && this.isMouseOver(mouseX, mouseY)) {
                        Screen screen = Minecraft.getInstance().screen;
                        if (screen != null) {
                            screen.renderTooltip(poseStack, new TextComponent(tooltip.getContents()), mouseX, mouseY);
                        }
                    }
                }
            };

            modificationWidgets.add(new PaginatedWidget(pageIndex.get(), this.addRenderableWidget(checkbox)));

            widgetIndex.addAndGet(2);
        }
    }

    @Override
    public void init() {
        super.init();

        int leftMargin = this.leftPos + 7;
        int topMargin = this.topPos + 7;
        int buttonWidth = 79;
        int buttonHeight = 20;
        int buttonHeightSpacing = buttonHeight + 4;
        int buttonWidthSpacing = buttonWidth + 4;
        int buttonWidthLong = buttonWidth + buttonWidthSpacing;

        programsButton = this.addRenderableWidget(new Button(leftMargin, topMargin, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.programs"), button -> {
            buttonScope = buttonScope.toggleScope(ButtonScope.PROGRAMS);
            this.checkButtonStates();
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.programs.tooltip"), x, y));
        }));

        modifyButton = this.addRenderableWidget(new Button(leftMargin + buttonWidthSpacing, topMargin, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.modify"), button -> {
            buttonScope = buttonScope.toggleScope(ButtonScope.MODIFICATIONS);
            this.checkButtonStates();
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.modify.tooltip"), x, y));
        }));

        openDoorButton = this.addRenderableWidget(new Button(leftMargin, topMargin + buttonHeightSpacing, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.open"), button -> {
            menu.sendSimpleCommand(StasisChamberMenu.Command.OPEN_DOOR);
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.open.tooltip"), x, y));
        }));

        closeDoorButton = this.addRenderableWidget(new Button(leftMargin + buttonWidthSpacing, topMargin + buttonHeightSpacing, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.close"), button -> {
            menu.sendSimpleCommand(StasisChamberMenu.Command.CLOSE_DOOR);
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.close.tooltip"), x, y));
        }));

        programWidgets.add(this.addRenderableWidget(new Button(leftMargin, topMargin + buttonHeightSpacing, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.program.transfur"), button -> {
            menu.inputProgram("transfur");
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.program.transfur.tooltip"), x, y));
        })));

        programWidgets.add(this.addRenderableWidget(new Button(leftMargin + buttonWidthSpacing, topMargin + buttonHeightSpacing, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.program.modify"), button -> {
            menu.inputProgram("modify");
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.program.modify.tooltip"), x, y));
        })));

        programWidgets.add(this.addRenderableWidget(new Button(leftMargin, topMargin + buttonHeightSpacing * 2, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.program.capture_next"), button -> {
            menu.inputProgram("captureNextEntity");
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.program.capture_next.tooltip"), x, y));
        })));

        programWidgets.add(this.addRenderableWidget(new Button(leftMargin + buttonWidthSpacing, topMargin + buttonHeightSpacing * 2, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.program.toggle_stasis"), button -> {
            menu.inputProgram("toggleStasis");
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.program.toggle_stasis.tooltip"), x, y));
        })));

        programWidgets.add(this.addRenderableWidget(new Button(leftMargin, topMargin + buttonHeightSpacing * 3, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.program.create_entity"), button -> {
            menu.inputProgram("createEntity");
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.program.create_entity.tooltip"), x, y));
        })));

        programWidgets.add(this.addRenderableWidget(new Button(leftMargin + buttonWidthSpacing, topMargin + buttonHeightSpacing * 3, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.program.discard_entity"), button -> {
            menu.inputProgram("discardEntity");
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.program.discard_entity.tooltip"), x, y));
        })));

        programWidgets.add(this.addRenderableWidget(new Button(leftMargin, topMargin + buttonHeightSpacing * 4, buttonWidth, buttonHeight, new TranslatableComponent("changed.stasis.program.abort"), button -> {
            menu.inputProgram("abort");
        }, (button, stack, x, y) -> {
            setToolTip(() -> this.renderTooltip(stack, new TranslatableComponent("changed.stasis.program.abort.tooltip"), x, y));
        })));

        waitDurationSlider = this.addRenderableWidget(new AbstractSliderButton(leftMargin, topMargin + buttonHeightSpacing * 5, buttonWidthLong, buttonHeight, new TranslatableComponent("changed.stasis.program.wait_duration", menu.getWaitDurationSeconds(menu.getWaitDurationPercent())), menu.getWaitDurationPercent()) {
            @Override
            protected void updateMessage() {
                setMessage(new TranslatableComponent("changed.stasis.program.wait_duration", menu.getWaitDurationSeconds(this.value)));
            }

            @Override
            protected void applyValue() {
                menu.inputWaitDuration(this.value);
            }

            @Override
            public void renderButton(PoseStack poseStack, int p_93677_, int p_93678_, float p_93679_) {
                if (!(this.isHoveredOrFocused() && minecraft.mouseHandler.isLeftPressed())) {
                    double last = this.value;
                    this.value = menu.getWaitDurationPercent();
                    if (last != this.value)
                        updateMessage();
                }
                super.renderButton(poseStack, p_93677_, p_93678_, p_93679_);
            }
        });
        programWidgets.add(waitDurationSlider);

        genderButton = this.addRenderableWidget(
                new Button(
                        leftMargin,
                        topMargin + buttonHeightSpacing,
                        buttonWidthLong,
                        buttonHeight,
                        new TranslatableComponent("changed.stasis.modify.gender"),
                        b -> {menu.inputModification("gender", ByteTag.valueOf(true));}
                ) {
                    @Override
                    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                        super.render(poseStack, mouseX, mouseY, partialTicks);

                        if (this.isMouseOver(mouseX, mouseY)) {
                            Screen screen = Minecraft.getInstance().screen;
                            if (screen != null) {
                                screen.renderTooltip(poseStack, new TranslatableComponent("changed.stasis.modify.gender.tooltip"), mouseX, mouseY);
                            }
                        }
                    }
                }
        );

        initialized = true;

        if (menu.startOnModificationsScreen)
            buttonScope = ButtonScope.MODIFICATIONS;

        checkButtonStates();
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double p_97755_, double p_97756_) {
        if (buttonScope == ButtonScope.DEFAULT)
            return super.mouseDragged(x, y, button, p_97755_, p_97756_);
        else
            return this.getFocused() != null && this.isDragging() && button == 0 ? this.getFocused().mouseDragged(x, y, button, p_97755_, p_97756_) : false;
    }

    @Override
    protected void renderLabels(PoseStack p_281635_, int p_282681_, int p_283686_) {

    }
}