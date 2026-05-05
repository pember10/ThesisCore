package com.bleachlizard.thesiscore.knowledge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a player's belief about a specific {@link com.bleachlizard.thesiscore.symbol.Symbol}.
 *
 * <p>A belief is the player's mental model of what a symbol means — which tags they think it carries.
 * This is entirely separate from what the symbol <em>actually</em> is. The system can compare the
 * two at any time to produce an {@link InterpretationResult}.
 *
 * <p>Key fields:
 * <ul>
 *   <li>{@code believedTags} — the set of tags the player thinks this symbol has</li>
 *   <li>{@code confidence} — how certain the player is (0.0 = no idea, 1.0 = fully sure)</li>
 *   <li>{@code entrenched} — if true, this belief resists correction; represents deeply held
 *       (possibly wrong) understanding that requires deliberate effort to revise</li>
 * </ul>
 */
public class SymbolBelief {

    public static final Codec<SymbolBelief> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC
                            .fieldOf("symbol_id")
                            .forGetter(SymbolBelief::getSymbolId),
                    Codec.STRING.listOf()
                            .<Set<String>>xmap(list -> new HashSet<>(list), set -> new ArrayList<>(set))
                            .fieldOf("believed_tags")
                            .forGetter(b -> b.believedTags),
                    Codec.FLOAT
                            .fieldOf("confidence")
                            .forGetter(SymbolBelief::getConfidence),
                    Codec.BOOL
                            .fieldOf("entrenched")
                            .forGetter(SymbolBelief::isEntrenched)
            ).apply(instance, SymbolBelief::new));

    private final ResourceLocation symbolId;
    private Set<String> believedTags;
    private float confidence;
    private boolean entrenched;

    public SymbolBelief(ResourceLocation symbolId, Set<String> believedTags, float confidence, boolean entrenched) {
        this.symbolId = symbolId;
        this.believedTags = new HashSet<>(believedTags);
        this.confidence = Math.clamp(confidence, 0f, 1f);
        this.entrenched = entrenched;
    }

    /** Creates a fresh, empty belief — the player has just encountered this symbol for the first time. */
    public static SymbolBelief unknown(ResourceLocation symbolId) {
        return new SymbolBelief(symbolId, Set.of(), 0f, false);
    }

    public ResourceLocation getSymbolId() {
        return symbolId;
    }

    public Set<String> getBelievedTags() {
        return Collections.unmodifiableSet(believedTags);
    }

    public float getConfidence() {
        return confidence;
    }

    public boolean isEntrenched() {
        return entrenched;
    }

    /**
     * Updates this belief with new evidence from a fragment or experiment.
     * Entrenched beliefs dampen confidence changes by 50%.
     */
    public void applyEvidence(Set<String> evidenceTags, float evidenceStrength) {
        float dampening = entrenched ? 0.5f : 1.0f;
        believedTags.addAll(evidenceTags);
        confidence = Math.clamp(confidence + evidenceStrength * dampening, 0f, 1f);
    }

    /**
     * Attempts to correct this belief. Entrenched beliefs require a strength above
     * 0.6 to accept the correction at all; weaker corrections are ignored.
     */
    public void correct(Set<String> trueTags, float correctionStrength) {
        if (entrenched && correctionStrength < 0.6f) {
            return;
        }
        believedTags = new HashSet<>(trueTags);
        confidence = Math.clamp(correctionStrength, 0f, 1f);
        if (entrenched) {
            entrenched = false;
        }
    }

    /** Locks this belief in. Future corrections will require higher strength to take effect. */
    public void entrench() {
        if (confidence >= 0.6f) {
            this.entrenched = true;
        }
    }

    @Override
    public String toString() {
        return "SymbolBelief[" + symbolId + ", confidence=" + confidence
                + ", entrenched=" + entrenched + ", tags=" + believedTags + "]";
    }
}
