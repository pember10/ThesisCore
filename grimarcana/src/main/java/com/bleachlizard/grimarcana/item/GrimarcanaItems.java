package com.bleachlizard.grimarcana.item;

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
    // Fragment items — one per rarity tier
    // -------------------------------------------------------------------------

    public static final Supplier<FragmentItem> FRAGMENT_COMMON = ITEMS.register("fragment_common",
            () -> new FragmentItem(new Item.Properties().stacksTo(16)));

    public static final Supplier<FragmentItem> FRAGMENT_UNCOMMON = ITEMS.register("fragment_uncommon",
            () -> new FragmentItem(new Item.Properties().stacksTo(16)));

    public static final Supplier<FragmentItem> FRAGMENT_RARE = ITEMS.register("fragment_rare",
            () -> new FragmentItem(new Item.Properties().stacksTo(8)));

    public static final Supplier<FragmentItem> FRAGMENT_EPIC = ITEMS.register("fragment_epic",
            () -> new FragmentItem(new Item.Properties().stacksTo(4)));

    public static final Supplier<FragmentItem> FRAGMENT_LEGENDARY = ITEMS.register("fragment_legendary",
            () -> new FragmentItem(new Item.Properties().stacksTo(1)));

    private GrimarcanaItems() {}
}
