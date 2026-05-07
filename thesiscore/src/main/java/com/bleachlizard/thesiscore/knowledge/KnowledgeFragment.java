package com.bleachlizard.thesiscore.knowledge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a discoverable fragment of knowledge about the symbolic world.
 *
 * <p>Fragments are the primary unit of player discovery. They are intentionally
 * vague — each fragment hints at one or more symbols with a given signal strength,
 * but the player must interpret what that means. Some fragments are deliberately
 * misleading; this is hidden from the player and only known to the system.
 *
 * <p>Fragments are not Minecraft items — the item representation (that a player
 * can hold and trade) is a separate layer built on top of this data object.
 */
public class KnowledgeFragment {

    public static final Codec<KnowledgeFragment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC
                            .fieldOf("id")
                            .forGetter(KnowledgeFragment::getId),
                    Codec.STRING
                            .fieldOf("description_key")
                            .forGetter(KnowledgeFragment::getDescriptionKey),
                    ResourceLocation.CODEC.listOf()
                            .<Set<ResourceLocation>>xmap(list -> new HashSet<>(list), set -> new ArrayList<>(set))
                            .fieldOf("related_symbols")
                            .forGetter(KnowledgeFragment::getRelatedSymbols),
                    Codec.FLOAT
                            .fieldOf("signal_strength")
                            .forGetter(KnowledgeFragment::getSignalStrength),
                    Codec.BOOL
                            .fieldOf("misleading")
                            .forGetter(KnowledgeFragment::isMisleading)
            ).apply(instance, KnowledgeFragment::new));

    private final ResourceLocation id;
    /** Translation key for the vague, player-facing description of this fragment. */
    private final String descriptionKey;
    /** The symbols this fragment hints at. May be empty or intentionally wrong. */
    private final Set<ResourceLocation> relatedSymbols;
    /**
     * How confidently this fragment points toward its related symbols (0.0–1.0).
     * Strong fragments give clearer signals; weak fragments are ambiguous.
     */
    private final float signalStrength;
    /**
     * Whether this fragment is intentionally misleading.
     * Hidden from the player — only the system knows. Misleading fragments point
     * to the wrong symbols or inflate false confidence.
     */
    private final boolean misleading;

    public KnowledgeFragment(
            ResourceLocation id,
            String descriptionKey,
            Set<ResourceLocation> relatedSymbols,
            float signalStrength,
            boolean misleading) {
        this.id = id;
        this.descriptionKey = descriptionKey;
        this.relatedSymbols = Collections.unmodifiableSet(new HashSet<>(relatedSymbols));
        this.signalStrength = Math.clamp(signalStrength, 0f, 1f);
        this.misleading = misleading;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public Set<ResourceLocation> getRelatedSymbols() {
        return relatedSymbols;
    }

    public float getSignalStrength() {
        return signalStrength;
    }

    public boolean isMisleading() {
        return misleading;
    }

    @Override
    public String toString() {
        return "KnowledgeFragment[" + id + ", strength=" + signalStrength + ", misleading=" + misleading + "]";
    }
}
