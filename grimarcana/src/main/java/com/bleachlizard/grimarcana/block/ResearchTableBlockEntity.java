package com.bleachlizard.grimarcana.block;

import com.bleachlizard.grimarcana.menu.ResearchTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Block entity for the Research Table.
 *
 * <p>Holds two slots:
 * <ul>
 *   <li>Slot 0 ({@link #SLOT_FRAGMENT}) — the fragment item to research</li>
 *   <li>Slot 1 ({@link #SLOT_REAGENT})  — the required reagent</li>
 * </ul>
 *
 * <p>Research is triggered from {@link ResearchTableMenu#clickMenuButton} on the server.
 * When successful, the fragment ID is added to the player's
 * {@link com.bleachlizard.thesiscore.registry.ThesisCoreAttachments#RESEARCHED_FRAGMENTS}
 * attachment and both items are consumed.
 */
public class ResearchTableBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_FRAGMENT = 0;
    public static final int SLOT_REAGENT  = 1;
    public static final int SLOT_COUNT    = 2;

    public final ItemStackHandler inventory = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(GrimarcanaBlocks.RESEARCH_TABLE_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.grimarcana.research_table");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ResearchTableMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
    }
}
