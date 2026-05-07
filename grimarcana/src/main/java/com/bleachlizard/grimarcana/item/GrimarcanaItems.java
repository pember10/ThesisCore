package com.bleachlizard.grimarcana.item;

import com.bleachlizard.grimarcana.block.GrimarcanaBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers all Grimarcana items.
 *
 * <p>Fragment items are registered one per rarity tier — all fragments of the same
 * rarity share the same item class, display model, and texture. The specific fragment
 * definition carried by a stack is stored in the {@link FragmentComponents#FRAGMENT_ID}
 * data component.
 */
public class GrimarcanaItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("grimarcana");

    // -------------------------------------------------------------------------
    // Fragment item — single item for all rarities
    // -------------------------------------------------------------------------

    /**
     * The single item used for every fragment, regardless of rarity.
     * Rarity-specific display (name colour, display name prefix) is resolved at runtime
     * from the {@link FragmentComponents#FRAGMENT_ID} component via the open
     * {@link com.bleachlizard.thesiscore.registry.ThesisCoreRegistries#FRAGMENT_RARITIES}
     * registry — so data-pack-defined rarities work automatically with no additional items.
     */
    public static final Supplier<FragmentItem> FRAGMENT = ITEMS.register("fragment",
            () -> new FragmentItem(new Item.Properties().stacksTo(16)));

    // -------------------------------------------------------------------------
    // Research reagent items
    // -------------------------------------------------------------------------

    /**
     * A plain stone tablet — used as the research reagent for the Crumbling Tablet fragment.
     * Thematically fitting: the researcher sacrifices a blank tablet to decode an ancient one.
     */
    public static final Supplier<Item> TABLET = ITEMS.register("tablet",
            () -> new Item(new Item.Properties().stacksTo(16)));

    /** Placeable Research Table item — gives the player the Research Table block. */
    public static final Supplier<BlockItem> RESEARCH_TABLE = ITEMS.register("research_table",
            () -> new BlockItem(GrimarcanaBlocks.RESEARCH_TABLE.get(), new Item.Properties()));

    private GrimarcanaItems() {}
}
