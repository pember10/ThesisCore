package com.bleachlizard.grimarcana.symbol;

import com.bleachlizard.thesiscore.registry.ThesisCoreRegistries;
import com.bleachlizard.thesiscore.symbol.SimpleSymbol;
import com.bleachlizard.thesiscore.symbol.Symbol;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Core symbols in Grimarcana.
 *
 * <p>Symbols represent fundamental magical concepts that players must discover
 * and interpret through research, fragments, and experimentation.
 *
 * <p>Each symbol is data-driven and defined via JSON in {@code data/grimarcana/symbols/}.
 * This class manages their registration via NeoForge's DeferredRegister.
 */
public class GrimarcanaSymbols {

    public static final DeferredRegister<Symbol> SYMBOLS = DeferredRegister.create(
            ThesisCoreRegistries.SYMBOLS,
            "grimarcana"
    );

    // -------------------------------------------------------------------------
    // Core Symbols (7 fundamental concepts)
    // -------------------------------------------------------------------------

    /**
     * Heat — Energy, destruction, transformation, and warmth.
     */
    public static final Supplier<Symbol> HEAT = SYMBOLS.register("heat",
            () -> new SimpleSymbol(
                    ResourceLocation.fromNamespaceAndPath("grimarcana", "heat"),
                    Set.of("energy", "destruction", "transformation", "motion")
            ));

    /**
     * Stability — Order, resistance, persistence, and control.
     */
    public static final Supplier<Symbol> STABILITY = SYMBOLS.register("stability",
            () -> new SimpleSymbol(
                    ResourceLocation.fromNamespaceAndPath("grimarcana", "stability"),
                    Set.of("order", "resistance", "persistence", "control")
            ));

    /**
     * Chaos — Disorder, unpredictability, change, and randomness.
     */
    public static final Supplier<Symbol> CHAOS = SYMBOLS.register("chaos",
            () -> new SimpleSymbol(
                    ResourceLocation.fromNamespaceAndPath("grimarcana", "chaos"),
                    Set.of("disorder", "unpredictability", "change", "randomness")
            ));

    /**
     * Life — Growth, vitality, regeneration, and fertility.
     */
    public static final Supplier<Symbol> LIFE = SYMBOLS.register("life",
            () -> new SimpleSymbol(
                    ResourceLocation.fromNamespaceAndPath("grimarcana", "life"),
                    Set.of("growth", "vitality", "regeneration", "fertility")
            ));

    /**
     * Decay — Entropy, death, dissolution, and degradation.
     */
    public static final Supplier<Symbol> DECAY = SYMBOLS.register("decay",
            () -> new SimpleSymbol(
                    ResourceLocation.fromNamespaceAndPath("grimarcana", "decay"),
                    Set.of("entropy", "death", "dissolution", "degradation")
            ));

    /**
     * Void — Absence, emptiness, negation, and the unknown.
     */
    public static final Supplier<Symbol> VOID = SYMBOLS.register("void",
            () -> new SimpleSymbol(
                    ResourceLocation.fromNamespaceAndPath("grimarcana", "void"),
                    Set.of("absence", "emptiness", "negation", "unknown")
            ));

    /**
     * Spirit — Consciousness, will, essence, and intention.
     */
    public static final Supplier<Symbol> SPIRIT = SYMBOLS.register("spirit",
            () -> new SimpleSymbol(
                    ResourceLocation.fromNamespaceAndPath("grimarcana", "spirit"),
                    Set.of("consciousness", "will", "essence", "intention")
            ));

    private GrimarcanaSymbols() {}
}
