package com.bleachlizard.grimarcana.fragment;

import com.bleachlizard.thesiscore.knowledge.KnowledgeFragment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Defines the custom registry for Grimarcana's fragment definitions and
 * registers all built-in fragments.
 *
 * <p>Fragments are organised by rarity tier. Several are intentionally misleading —
 * the player will not know this until they research them or discover inconsistencies
 * through experimentation.
 *
 * <p>Symbol coverage:
 * <ul>
 *   <li>Heat       — 2 true, 1 misleading</li>
 *   <li>Stability  — 2 true</li>
 *   <li>Chaos      — 2 true, 1 misleading</li>
 *   <li>Life       — 2 true</li>
 *   <li>Decay      — 2 true</li>
 *   <li>Void       — 1 true, 1 misleading</li>
 *   <li>Spirit     — 2 true</li>
 * </ul>
 */
@EventBusSubscriber(modid = "grimarcana")
public class GrimarcanaFragments {

    // -------------------------------------------------------------------------
    // Registry
    // -------------------------------------------------------------------------

    public static final ResourceKey<Registry<GrimarcanaFragment>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("grimarcana", "grimarcana_fragment"));

    public static final DeferredRegister<GrimarcanaFragment> FRAGMENTS =
            DeferredRegister.create(REGISTRY_KEY, "grimarcana");

    @SubscribeEvent
    static void onNewRegistry(NewRegistryEvent event) {
        event.create(new RegistryBuilder<>(REGISTRY_KEY));
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private static ResourceKey<com.bleachlizard.thesiscore.knowledge.FragmentRarity> rarity(String name) {
        return ResourceKey.create(
                com.bleachlizard.thesiscore.registry.ThesisCoreRegistries.FRAGMENT_RARITIES,
                ResourceLocation.fromNamespaceAndPath("grimarcana", name)
        );
    }

    private static ResourceLocation sym(String name) {
        return ResourceLocation.fromNamespaceAndPath("grimarcana", name);
    }

    private static GrimarcanaFragment fragment(
            String id, String descKey, Set<ResourceLocation> symbols,
            float signal, boolean misleading, String rarityName) {
        return new GrimarcanaFragment(
                new KnowledgeFragment(
                        ResourceLocation.fromNamespaceAndPath("grimarcana", id),
                        descKey, symbols, signal, misleading),
                rarity(rarityName));
    }

    // -------------------------------------------------------------------------
    // Heat (grimarcana:heat)
    // -------------------------------------------------------------------------

    /** Common — a smear of ash that seems to pulse faintly. True. */
    public static final Supplier<GrimarcanaFragment> EMBER_SMEAR = FRAGMENTS.register("ember_smear",
            () -> fragment("ember_smear", "fragment.grimarcana.ember_smear",
                    Set.of(sym("heat")), 0.20f, false, "common"));

    /** Uncommon — singed rune etched into stone. True. */
    public static final Supplier<GrimarcanaFragment> SCORCHED_GLYPH = FRAGMENTS.register("scorched_glyph",
            () -> fragment("scorched_glyph", "fragment.grimarcana.scorched_glyph",
                    Set.of(sym("heat")), 0.45f, false, "uncommon"));

    /**
     * Rare — a carved inscription that looks like heat but is subtly wrong — it is
     * actually a Chaos symbol. Misleading.
     */
    public static final Supplier<GrimarcanaFragment> FALSE_FLAME = FRAGMENTS.register("false_flame",
            () -> fragment("false_flame", "fragment.grimarcana.false_flame",
                    Set.of(sym("chaos")), 0.60f, true, "rare"));

    // -------------------------------------------------------------------------
    // Stability (grimarcana:stability)
    // -------------------------------------------------------------------------

    /** Common — a chip of stone that feels unusually still. True. */
    public static final Supplier<GrimarcanaFragment> STILL_SHARD = FRAGMENTS.register("still_shard",
            () -> fragment("still_shard", "fragment.grimarcana.still_shard",
                    Set.of(sym("stability")), 0.25f, false, "common"));

    /** Epic — a nearly-complete binding rune inscribed on obsidian. True. */
    public static final Supplier<GrimarcanaFragment> BINDING_RUNE = FRAGMENTS.register("binding_rune",
            () -> fragment("binding_rune", "fragment.grimarcana.binding_rune",
                    Set.of(sym("stability")), 0.80f, false, "epic"));

    // -------------------------------------------------------------------------
    // Chaos (grimarcana:chaos)
    // -------------------------------------------------------------------------

    /** Uncommon — a scrap of paper covered in shifting ink. True. */
    public static final Supplier<GrimarcanaFragment> SHIFTING_SCRAP = FRAGMENTS.register("shifting_scrap",
            () -> fragment("shifting_scrap", "fragment.grimarcana.shifting_scrap",
                    Set.of(sym("chaos")), 0.40f, false, "uncommon"));

    /** Rare — a cracked lens that refracts light strangely. True. */
    public static final Supplier<GrimarcanaFragment> FRACTURED_LENS = FRAGMENTS.register("fractured_lens",
            () -> fragment("fractured_lens", "fragment.grimarcana.fractured_lens",
                    Set.of(sym("chaos")), 0.65f, false, "rare"));

    /**
     * Uncommon — looks like Void energy but is actually Chaos.
     * Misleading — the visual signature of both is easily confused at low signal strength.
     */
    public static final Supplier<GrimarcanaFragment> HOLLOW_ECHO = FRAGMENTS.register("hollow_echo",
            () -> fragment("hollow_echo", "fragment.grimarcana.hollow_echo",
                    Set.of(sym("chaos")), 0.35f, true, "uncommon"));

    // -------------------------------------------------------------------------
    // Life (grimarcana:life)
    // -------------------------------------------------------------------------

    /** Common — a dried seed that still hums with potential. True. */
    public static final Supplier<GrimarcanaFragment> DORMANT_SEED = FRAGMENTS.register("dormant_seed",
            () -> fragment("dormant_seed", "fragment.grimarcana.dormant_seed",
                    Set.of(sym("life")), 0.20f, false, "common"));

    /** Legendary — an ancient pressed flower perfectly preserved with full inscription. True. */
    public static final Supplier<GrimarcanaFragment> PRESERVED_BLOSSOM = FRAGMENTS.register("preserved_blossom",
            () -> fragment("preserved_blossom", "fragment.grimarcana.preserved_blossom",
                    Set.of(sym("life")), 0.92f, false, "legendary"));

    // -------------------------------------------------------------------------
    // Decay (grimarcana:decay)
    // -------------------------------------------------------------------------

    /** Rare — a fragment of bone with worn runes. True. */
    public static final Supplier<GrimarcanaFragment> WORN_BONE = FRAGMENTS.register("worn_bone",
            () -> fragment("worn_bone", "fragment.grimarcana.worn_bone",
                    Set.of(sym("decay")), 0.55f, false, "rare"));

    /** Epic — a crumbling tablet with a near-complete inscription. True. */
    public static final Supplier<GrimarcanaFragment> CRUMBLING_TABLET = FRAGMENTS.register("crumbling_tablet",
            () -> fragment("crumbling_tablet", "fragment.grimarcana.crumbling_tablet",
                    Set.of(sym("decay")), 0.75f, false, "epic"));

    // -------------------------------------------------------------------------
    // Void (grimarcana:void)
    // -------------------------------------------------------------------------

    /** Legendary — a crystallised absence — impossible to describe precisely. True. */
    public static final Supplier<GrimarcanaFragment> VOID_CRYSTAL = FRAGMENTS.register("void_crystal",
            () -> fragment("void_crystal", "fragment.grimarcana.void_crystal",
                    Set.of(sym("void")), 0.90f, false, "legendary"));

    /**
     * Rare — looks like a Void fragment but points to Spirit.
     * Misleading — experienced players will notice the warmth is wrong for Void.
     */
    public static final Supplier<GrimarcanaFragment> HOLLOW_IMPRESSION = FRAGMENTS.register("hollow_impression",
            () -> fragment("hollow_impression", "fragment.grimarcana.hollow_impression",
                    Set.of(sym("spirit")), 0.60f, true, "rare"));

    // -------------------------------------------------------------------------
    // Spirit (grimarcana:spirit)
    // -------------------------------------------------------------------------

    /** Uncommon — a feather that seems to carry intent. True. */
    public static final Supplier<GrimarcanaFragment> INTENTIONED_FEATHER = FRAGMENTS.register("intentioned_feather",
            () -> fragment("intentioned_feather", "fragment.grimarcana.intentioned_feather",
                    Set.of(sym("spirit")), 0.40f, false, "uncommon"));

    /** Epic — a carved medallion with nearly-complete spirit inscription. True. */
    public static final Supplier<GrimarcanaFragment> SPIRIT_MEDALLION = FRAGMENTS.register("spirit_medallion",
            () -> fragment("spirit_medallion", "fragment.grimarcana.spirit_medallion",
                    Set.of(sym("spirit")), 0.80f, false, "epic"));

    private GrimarcanaFragments() {}
}
