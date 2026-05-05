package com.bleachlizard.thesiscore.knowledge;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.bleachlizard.thesiscore.ResourceLocationTestUtil.of;
import static org.junit.jupiter.api.Assertions.*;

class SymbolBeliefTest {

    @Test
    void unknownBeliefStartsEmptyAndUnentrenched() {
        var belief = SymbolBelief.unknown(of("thesiscore", "heat"));

        assertEquals(0f, belief.getConfidence(), 0.001f);
        assertTrue(belief.getBelievedTags().isEmpty());
        assertFalse(belief.isEntrenched());
    }

    @Test
    void applyEvidenceAddsTagsAndIncreasesConfidence() {
        var belief = SymbolBelief.unknown(of("thesiscore", "heat"));

        belief.applyEvidence(Set.of("energy", "destruction"), 0.4f);

        assertEquals(0.4f, belief.getConfidence(), 0.001f);
        assertEquals(Set.of("energy", "destruction"), belief.getBelievedTags());
    }

    @Test
    void applyEvidenceIsDampenedWhenEntrenched() {
        var belief = new SymbolBelief(of("thesiscore", "heat"), Set.of("fire"), 0.8f, true);

        belief.applyEvidence(Set.of("energy"), 0.4f);

        assertEquals(1.0f, belief.getConfidence(), 0.001f);
        assertEquals(Set.of("fire", "energy"), belief.getBelievedTags());
    }

    @Test
    void entrenchPreventsWeakCorrection() {
        var belief = new SymbolBelief(of("thesiscore", "heat"), Set.of("fire"), 0.8f, false);
        belief.entrench();

        assertTrue(belief.isEntrenched());

        belief.correct(Set.of("energy"), 0.5f);

        assertEquals(Set.of("fire"), belief.getBelievedTags());
        assertEquals(0.8f, belief.getConfidence(), 0.001f);
    }

    @Test
    void weakCorrectionDoesNotChangeEntrenchedBelief() {
        var belief = new SymbolBelief(of("thesiscore", "heat"), Set.of("fire"), 0.8f, true);

        belief.correct(Set.of("energy"), 0.5f);

        assertEquals(Set.of("fire"), belief.getBelievedTags());
        assertEquals(0.8f, belief.getConfidence(), 0.001f);
        assertTrue(belief.isEntrenched());
    }

    @Test
    void correctResetsBeliefAndUnentrances() {
        var belief = new SymbolBelief(of("thesiscore", "heat"), Set.of("fire"), 0.8f, true);
        belief.correct(Set.of("energy", "destruction"), 0.9f);

        assertEquals(Set.of("energy", "destruction"), belief.getBelievedTags());
        assertEquals(0.9f, belief.getConfidence(), 0.001f);
        assertFalse(belief.isEntrenched());
    }

    @Test
    void entrenchRequiresMinimumConfidence() {
        var belief = new SymbolBelief(of("thesiscore", "heat"), Set.of("fire"), 0.5f, false);

        belief.entrench();

        assertFalse(belief.isEntrenched());
    }
}
