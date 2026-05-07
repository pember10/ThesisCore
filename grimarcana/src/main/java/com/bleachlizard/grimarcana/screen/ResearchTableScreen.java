package com.bleachlizard.grimarcana.screen;

import com.bleachlizard.grimarcana.menu.ResearchTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * Client-side screen for the Research Table.
 *
 * <p>Shows:
 * <ul>
 *   <li>A fragment slot (left) and a reagent slot (right) in the working area</li>
 *   <li>A "Research" button that sends button click 0 to the server</li>
 *   <li>The standard player inventory grid below</li>
 * </ul>
 *
 * <p>No custom texture is used — slots and background are drawn with solid fills.
 * A proper texture can be added later by overriding {@link #renderBg} with a
 * {@link GuiGraphics#blit} call.
 */
public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {

    public ResearchTableScreen(ResearchTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth  = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(
                Button.builder(
                        Component.translatable("button.grimarcana.research"),
                        button -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0)
                ).bounds(leftPos + 105, topPos + 31, 60, 20).build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // Panel background
        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFFC6C6C6);
        // Slot insets (18×18 each)
        guiGraphics.fill(leftPos + 34, topPos + 34, leftPos + 52, topPos + 52, 0xFF8B8B8B);
        guiGraphics.fill(leftPos + 76, topPos + 34, leftPos + 94, topPos + 52, 0xFF8B8B8B);
        // Player inventory separator line
        guiGraphics.fill(leftPos + 7, topPos + 77, leftPos + 169, topPos + 78, 0xFF888888);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
