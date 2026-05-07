package com.bleachlizard.grimarcana.menu;

import com.bleachlizard.grimarcana.block.ResearchTableBlockEntity;
import com.bleachlizard.grimarcana.fragment.GrimarcanaFragment;
import com.bleachlizard.grimarcana.fragment.GrimarcanaFragments;
import com.bleachlizard.grimarcana.item.FragmentComponents;
import com.bleachlizard.thesiscore.registry.ThesisCoreAttachments;
import com.bleachlizard.grimarcana.network.SyncResearchedFragmentsPayload;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Set;

/**
 * Container menu for the Research Table.
 *
 * <p>Two slots are presented to the player:
 * <ul>
 *   <li>Fragment slot — must contain a Grimarcana fragment item with a FRAGMENT_ID component</li>
 *   <li>Reagent slot  — must contain an item matching the fragment's required reagent</li>
 * </ul>
 *
 * <p>The player clicks the Research button (button ID {@code 0}), which is handled
 * server-side in {@link #clickMenuButton}. On success, the fragment is permanently
 * marked as researched on the player and both items are consumed.
 */
public class ResearchTableMenu extends AbstractContainerMenu {

    private final IItemHandler inventory;
    private final ResearchTableBlockEntity blockEntity;

    /** Server-side constructor, called from {@link ResearchTableBlockEntity#createMenu}. */
    public ResearchTableMenu(int containerId, Inventory playerInventory, ResearchTableBlockEntity blockEntity) {
        super(GrimarcanaMenus.RESEARCH_TABLE.get(), containerId);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.inventory;
        setupSlots(playerInventory);
    }

    /**
     * Client-side constructor, called by the {@link net.minecraft.world.inventory.MenuType} factory.
     * Uses a stub {@link ItemStackHandler} whose contents are synced from the server.
     */
    public ResearchTableMenu(int containerId, Inventory playerInventory) {
        super(GrimarcanaMenus.RESEARCH_TABLE.get(), containerId);
        this.blockEntity = null;
        this.inventory = new ItemStackHandler(ResearchTableBlockEntity.SLOT_COUNT);
        setupSlots(playerInventory);
    }

    private void setupSlots(Inventory playerInventory) {
        // Block entity slots
        addSlot(new SlotItemHandler(inventory, ResearchTableBlockEntity.SLOT_FRAGMENT, 35, 35));
        addSlot(new SlotItemHandler(inventory, ResearchTableBlockEntity.SLOT_REAGENT,  77, 35));

        // Player main inventory (rows 0–2)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    // -------------------------------------------------------------------------
    // Research logic
    // -------------------------------------------------------------------------

    /**
     * Handles the Research button click (button ID {@code 0}) on the server.
     *
     * <p>Requirements for a successful research:
     * <ol>
     *   <li>Fragment slot contains an item with a valid {@link FragmentComponents#FRAGMENT_ID}</li>
     *   <li>Reagent slot contains an item matching the fragment's {@link GrimarcanaFragment#getReagent()}</li>
     *   <li>The player has not already researched this fragment</li>
     * </ol>
     *
     * <p>On success: fragment ID is added to the player's RESEARCHED_FRAGMENTS attachment,
     * both items are consumed, and a system message is sent to the player.
     */
    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != 0 || blockEntity == null || player.level().isClientSide()) return false;

        ItemStack fragmentStack = inventory.getStackInSlot(ResearchTableBlockEntity.SLOT_FRAGMENT);
        ItemStack reagentStack  = inventory.getStackInSlot(ResearchTableBlockEntity.SLOT_REAGENT);

        if (fragmentStack.isEmpty() || reagentStack.isEmpty()) return false;

        ResourceLocation fragmentId = fragmentStack.get(FragmentComponents.FRAGMENT_ID.get());
        if (fragmentId == null) return false;

        GrimarcanaFragment fragment = resolveFragment(fragmentId);
        if (fragment == null) return false;

        if (!fragment.getReagent().test(reagentStack)) {
            player.sendSystemMessage(Component.translatable("message.grimarcana.wrong_reagent"));
            return false;
        }

        Set<ResourceLocation> researched = player.getData(ThesisCoreAttachments.RESEARCHED_FRAGMENTS);
        if (researched.contains(fragmentId)) {
            player.sendSystemMessage(Component.translatable("message.grimarcana.already_researched"));
            return false;
        }

        researched.add(fragmentId);
        player.setData(ThesisCoreAttachments.RESEARCHED_FRAGMENTS, researched);

        inventory.extractItem(ResearchTableBlockEntity.SLOT_FRAGMENT, 1, false);
        inventory.extractItem(ResearchTableBlockEntity.SLOT_REAGENT,  1, false);

        player.sendSystemMessage(Component.translatable("message.grimarcana.research_complete"));

        // Sync the updated researched set to the client so the tooltip can reflect it
        if (player instanceof ServerPlayer sp) {
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                    sp, new SyncResearchedFragmentsPayload(researched));
        }

        blockEntity.setChanged();
        return true;
    }

    // -------------------------------------------------------------------------
    // Standard menu overrides
    // -------------------------------------------------------------------------

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 2) {
                if (!moveItemStackTo(stack, 2, slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(stack, 0, 2, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) return true; // client-side stub is always valid
        return blockEntity.getLevel() != null
                && blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity
                && player.distanceToSqr(
                        blockEntity.getBlockPos().getX() + 0.5,
                        blockEntity.getBlockPos().getY() + 0.5,
                        blockEntity.getBlockPos().getZ() + 0.5) < 64.0;
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static GrimarcanaFragment resolveFragment(ResourceLocation id) {
        var reg = BuiltInRegistries.REGISTRY.get(GrimarcanaFragments.REGISTRY_KEY.location());
        if (reg == null) return null;
        return ((Registry<GrimarcanaFragment>) reg).get(id);
    }
}
