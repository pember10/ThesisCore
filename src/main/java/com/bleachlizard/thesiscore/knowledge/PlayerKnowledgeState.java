package com.bleachlizard.thesiscore.knowledge;

import com.bleachlizard.thesiscore.symbol.Symbol;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Stores everything a single player believes about the symbolic world.
 *
 * <p>This is the runtime representation of imperfect player knowledge.
 * It is entirely separate from the actual {@link Symbol} objects in the registry —
 * a player may have a fully wrong {@link SymbolBelief} for a symbol and not know it.
 *
 * <p>An instance of this class is attached to each player via the NeoForge
 * attachment system ({@code ThesisCoreAttachments.PLAYER_KNOWLEDGE}) and persists
 * across sessions.
 */
public class PlayerKnowledgeState {

    public static final Codec<PlayerKnowledgeState> CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC, SymbolBelief.CODEC)
                    .xmap(PlayerKnowledgeState::fromMap, state -> Map.copyOf(state.beliefs));

    private final Map<ResourceLocation, SymbolBelief> beliefs = new HashMap<>();

    public PlayerKnowledgeState() {}

    private static PlayerKnowledgeState fromMap(Map<ResourceLocation, SymbolBelief> map) {
        PlayerKnowledgeState state = new PlayerKnowledgeState();
        state.beliefs.putAll(map);
        return state;
    }

    // -------------------------------------------------------------------------
    // Belief access
    // -------------------------------------------------------------------------

    /** Returns the player's current belief about a symbol, if any exists. */
    public Optional<SymbolBelief> getBelief(ResourceLocation symbolId) {
        return Optional.ofNullable(beliefs.get(symbolId));
    }

    /**
     * Returns the player's belief about a symbol, creating a fresh unknown belief
     * if the player has never encountered it. The new belief is stored immediately.
     */
    public SymbolBelief getOrCreateBelief(ResourceLocation symbolId) {
        return beliefs.computeIfAbsent(symbolId, SymbolBelief::unknown);
    }

    /** Returns {@code true} if the player has any recorded knowledge of this symbol. */
    public boolean hasKnowledge(ResourceLocation symbolId) {
        return beliefs.containsKey(symbolId);
    }

    /** Read-only view of all beliefs. */
    public Map<ResourceLocation, SymbolBelief> getAllBeliefs() {
        return Collections.unmodifiableMap(beliefs);
    }

    // -------------------------------------------------------------------------
    // Belief modification
    // -------------------------------------------------------------------------

    /**
     * Applies evidence from a fragment to the belief for the given symbol.
     * Creates a new unknown belief first if none exists.
     */
    public void applyEvidence(ResourceLocation symbolId, Set<String> evidenceTags, float strength) {
        getOrCreateBelief(symbolId).applyEvidence(evidenceTags, strength);
    }

    /**
     * Attempts to correct the player's belief about a symbol to match the true tags.
     * Has no effect if the player has no existing belief for this symbol.
     * Entrenched beliefs may resist the correction depending on {@code strength}.
     */
    public void correctBelief(ResourceLocation symbolId, Set<String> trueTags, float correctionStrength) {
        getBelief(symbolId).ifPresent(belief -> belief.correct(trueTags, correctionStrength));
    }

    // -------------------------------------------------------------------------
    // Interpretation
    // -------------------------------------------------------------------------

    /**
     * Evaluates how well the player's current belief matches a symbol's actual state.
     *
     * <p>This is the core of the system: the gap between what the player believes
     * and what is true determines the {@link InterpretationResult}.
     */
    public InterpretationResult evaluate(Symbol symbol) {
        Optional<SymbolBelief> belief = getBelief(symbol.getId());

        if (belief.isEmpty() || belief.get().getConfidence() < 0.1f) {
            return InterpretationResult.FAILURE;
        }

        SymbolBelief b = belief.get();
        Set<String> actualTags = symbol.getTags();
        Set<String> believedTags = b.getBelievedTags();

        long correct = believedTags.stream().filter(actualTags::contains).count();
        long wrong = believedTags.stream().filter(t -> !actualTags.contains(t)).count();
        int totalActual = actualTags.size();

        // Confident and significantly wrong → worst outcomes
        if (b.getConfidence() >= 0.6f && wrong > correct) {
            return b.getConfidence() >= 0.85f
                    ? InterpretationResult.CATASTROPHIC_FAILURE
                    : InterpretationResult.INCORRECT;
        }

        if (totalActual == 0 && believedTags.isEmpty()) {
            return InterpretationResult.CORRECT;
        }

        if (correct == totalActual && wrong == 0) {
            return InterpretationResult.CORRECT;
        }

        if (correct > 0) {
            return InterpretationResult.PARTIAL;
        }

        return b.getConfidence() >= 0.7f
                ? InterpretationResult.INCORRECT
                : InterpretationResult.FAILURE;
    }
}
