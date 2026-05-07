package com.bleachlizard.grimarcana.fragment;

import com.bleachlizard.thesiscore.knowledge.FragmentRarity;
import com.bleachlizard.thesiscore.registry.ThesisCoreRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Defines the five default fragment rarity tiers for Grimarcana.
 *
 * <p>Rarities are registered in ThesisCore's open {@code FRAGMENT_RARITIES} registry,
 * so other mods and data packs can extend or override them freely.
 *
 * <p>Signal strength ranges:
 * <ul>
 *   <li>Common     (0.10–0.30) — scattered rune fragments, barely legible</li>
 *   <li>Uncommon   (0.30–0.50) — partial glyphs, hints of meaning</li>
 *   <li>Rare       (0.50–0.70) — clear symbols, most of the translation visible</li>
 *   <li>Epic       (0.70–0.85) — nearly complete inscription, strong signal</li>
 *   <li>Legendary  (0.85–1.00) — almost perfect — but may still be misleading</li>
 * </ul>
 */
public class GrimarcanaRarities {

    public static final DeferredRegister<FragmentRarity> RARITIES =
            DeferredRegister.create(ThesisCoreRegistries.FRAGMENT_RARITIES, "grimarcana");

    /** Gray — 0xAAAAAA */
    public static final Supplier<FragmentRarity> COMMON = RARITIES.register("common",
            () -> new FragmentRarity(0.10f, 0.30f, "fragment_rarity.grimarcana.common", 0xAAAAAA));

    /** Green — 0x55FF55 */
    public static final Supplier<FragmentRarity> UNCOMMON = RARITIES.register("uncommon",
            () -> new FragmentRarity(0.30f, 0.50f, "fragment_rarity.grimarcana.uncommon", 0x55FF55));

    /** Blue — 0x5555FF */
    public static final Supplier<FragmentRarity> RARE = RARITIES.register("rare",
            () -> new FragmentRarity(0.50f, 0.70f, "fragment_rarity.grimarcana.rare", 0x5555FF));

    /** Purple — 0xAA00AA */
    public static final Supplier<FragmentRarity> EPIC = RARITIES.register("epic",
            () -> new FragmentRarity(0.70f, 0.85f, "fragment_rarity.grimarcana.epic", 0xAA00AA));

    /** Gold — 0xFFAA00 */
    public static final Supplier<FragmentRarity> LEGENDARY = RARITIES.register("legendary",
            () -> new FragmentRarity(0.85f, 1.00f, "fragment_rarity.grimarcana.legendary", 0xFFAA00));

    private GrimarcanaRarities() {}
}
