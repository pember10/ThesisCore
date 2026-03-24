package com.bleachlizard.thesiscore.symbol;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/**
 * Represents a core symbolic concept (e.g., Heat, Stability, Chaos).
 *
 * Symbols are abstract — they are not tied to any item or block.
 * They interact with other symbols through rules defined by higher-level systems,
 * and form the foundation of all interpretation logic in ThesisCore.
 */
public interface Symbol {

    /** The unique registry identifier for this symbol. */
    ResourceLocation getId();

    /** Tags describing this symbol's conceptual properties. */
    Set<String> getTags();
}
