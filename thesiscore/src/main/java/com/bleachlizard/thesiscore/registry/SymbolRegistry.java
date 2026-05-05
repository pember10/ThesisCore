package com.bleachlizard.thesiscore.registry;

import com.bleachlizard.thesiscore.ThesisCore;
import com.bleachlizard.thesiscore.symbol.Symbol;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * ThesisCore's own {@link DeferredRegister} for the Symbol registry.
 *
 * Built-in symbols (e.g., Heat, Stability, Chaos) will be declared here
 * as the system grows. Other mods (such as Grimarcana) should create their
 * own {@code DeferredRegister<Symbol>} pointed at
 * {@link ThesisCoreRegistries#SYMBOLS} under their own namespace.
 *
 * <p>Example for a dependent mod:
 * <pre>{@code
 *   public static final DeferredRegister<Symbol> SYMBOLS =
 *       DeferredRegister.create(ThesisCoreRegistries.SYMBOLS, MyMod.MODID);
 * }</pre>
 */
public class SymbolRegistry {

    public static final DeferredRegister<Symbol> SYMBOLS =
            DeferredRegister.create(ThesisCoreRegistries.SYMBOLS, ThesisCore.MODID);

    // Built-in symbols will be registered here as development progresses.
    // Example:
    //   public static final DeferredHolder<Symbol, SimpleSymbol> HEAT =
    //       SYMBOLS.register("heat", () -> new SimpleSymbol(
    //           ResourceLocation.fromNamespaceAndPath(ThesisCore.MODID, "heat"),
    //           Set.of("heat", "energy", "destruction")));

    private SymbolRegistry() {}
}
