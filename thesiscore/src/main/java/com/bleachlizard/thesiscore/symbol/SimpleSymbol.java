package com.bleachlizard.thesiscore.symbol;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A basic implementation of {@link Symbol} with an explicit id and a fixed set of tags.
 * Symbols can be registered via {@code DeferredRegister} or loaded from JSON data packs.
 */
public class SimpleSymbol implements Symbol {

    public static final Codec<SimpleSymbol> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC
                            .fieldOf("id")
                            .forGetter(SimpleSymbol::getId),
                    Codec.STRING.listOf()
                            .<Set<String>>xmap(list -> new HashSet<>(list), set -> new ArrayList<>(set))
                            .fieldOf("tags")
                            .forGetter(SimpleSymbol::getTags)
            ).apply(instance, SimpleSymbol::new));

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
