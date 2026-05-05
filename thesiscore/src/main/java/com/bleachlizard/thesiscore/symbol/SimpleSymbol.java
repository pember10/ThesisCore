package com.bleachlizard.thesiscore.symbol;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A basic implementation of {@link Symbol} with an explicit id and a fixed set of tags.
 * This is the standard concrete type used when registering symbols via {@code DeferredRegister}.
 */
public class SimpleSymbol implements Symbol {

    private final ResourceLocation id;
    private final Set<String> tags;

    public SimpleSymbol(ResourceLocation id, Set<String> tags) {
        this.id = id;
        this.tags = Collections.unmodifiableSet(new HashSet<>(tags));
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "SimpleSymbol[" + id + "]";
    }
}
