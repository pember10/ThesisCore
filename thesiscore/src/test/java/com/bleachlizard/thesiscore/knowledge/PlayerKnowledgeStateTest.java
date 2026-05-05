package com.bleachlizard.thesiscore.knowledge;

import com.bleachlizard.thesiscore.ResourceLocationTestUtil;
import com.bleachlizard.thesiscore.symbol.SimpleSymbol;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.bleachlizard.thesiscore.ResourceLocationTestUtil.of;
import static org.junit.jupiter.api.Assertions.*;

class PlayerKnowledgeStateTest {

    @Test
    void getOrCreateBeliefCreatesUnknownBelief() {
        var state = new PlayerKnowledgeState();
        var symbolId = of("thesiscore", "heat");

        var belief = state.getOrCreateBelief(symbolId);

        assertNotNull(belief);
        assertEquals(symbolId, belief.getSymbolId());
        assertEquals(0f, belief.getConfidence(), 0.001f);
    }

    @Test
    void evaluateReturnsCorrectForExactMatch() {
        var symbol = new SimpleSymbol(of("thesiscore", "heat"), Set.of("energy", "destruction"));
        var state = new PlayerKnowledgeState();

        state.applyEvidence(symbol.getId(), Set.of("energy", "destruction"), 1.0f);

        assertEquals(InterpretationResult.CORRECT, state.evaluate(symbol));
    }

    @Test
    void evaluateReturnsPartialWhenSomeTagsMatch() {
        var symbol = new SimpleSymbol(of("thesiscore", "heat"), Set.of("energy", "destruction"));
        var state = new PlayerKnowledgeState();

        state.applyEvidence(symbol.getId(), Set.of("energy"), 0.7f);

        assertEquals(InterpretationResult.PARTIAL, state.evaluate(symbol));
    }

    @Test
    void evaluateReturnsFailureWhenConfidenceIsLow() {
        var symbol = new SimpleSymbol(of("thesiscore", "heat"), Set.of("energy"));
        var state = new PlayerKnowledgeState();

        state.applyEvidence(symbol.getId(), Set.of("energy"), 0.05f);

        assertEquals(InterpretationResult.FAILURE, state.evaluate(symbol));
    }

    @Test
    void getBeliefReturnsEmptyWhenAbsent() {
        var state = new PlayerKnowledgeState();

        assertTrue(state.getBelief(of("thesiscore", "heat")).isEmpty());
        assertFalse(state.hasKnowledge(of("thesiscore", "heat")));
    }

    @Test
    void applyEvidenceCreatesBeliefAndStoresIt() {
        var symbolId = of("thesiscore", "heat");
        var state = new PlayerKnowledgeState();

        state.applyEvidence(symbolId, Set.of("energy", "destruction"), 0.4f);

        assertTrue(state.hasKnowledge(symbolId));
        assertTrue(state.getBelief(symbolId).isPresent());
        assertEquals(0.4f, state.getBelief(symbolId).get().getConfidence(), 0.001f);
    }

    @Test
    void evaluateReturnsCorrectForEmptySymbolWithEmptyBelief() {
        var symbol = new SimpleSymbol(of("thesiscore", "unknown"), Set.of());
        var state = new PlayerKnowledgeState();

        state.applyEvidence(symbol.getId(), Set.of(), 0.2f);

        assertEquals(InterpretationResult.CORRECT, state.evaluate(symbol));
    }

    @Test
    void evaluateReturnsCatastrophicFailureForVeryConfidentWrongBelief() {
        var symbol = new SimpleSymbol(of("thesiscore", "heat"), Set.of("energy", "destruction"));
        var state = new PlayerKnowledgeState();

        state.applyEvidence(symbol.getId(), Set.of("cold", "darkness"), 0.9f);

        assertEquals(InterpretationResult.CATASTROPHIC_FAILURE, state.evaluate(symbol));
    }
}
