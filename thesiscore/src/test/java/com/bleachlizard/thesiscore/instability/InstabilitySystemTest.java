package com.bleachlizard.thesiscore.instability;

import com.bleachlizard.thesiscore.knowledge.InterpretationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstabilitySystemTest {

    @Test
    void calculateGainMatchesResultSeverity() {
        assertEquals(0f, InstabilitySystem.calculateGain(InterpretationResult.CORRECT, 1f), 0.001f);
        assertEquals(0.5f, InstabilitySystem.calculateGain(InterpretationResult.PARTIAL, 1f), 0.001f);
        assertEquals(1.5f, InstabilitySystem.calculateGain(InterpretationResult.INCORRECT, 1f), 0.001f);
        assertEquals(0.25f, InstabilitySystem.calculateGain(InterpretationResult.FAILURE, 1f), 0.001f);
        assertEquals(3f, InstabilitySystem.calculateGain(InterpretationResult.CATASTROPHIC_FAILURE, 1f), 0.001f);
    }

    @Test
    void calculateGainClampsNegativeActionPower() {
        assertEquals(0f, InstabilitySystem.calculateGain(InterpretationResult.CATASTROPHIC_FAILURE, -10f), 0.001f);
    }

    @Test
    void resolveOutcomeReturnsCorrectThresholds() {
        var instability = new PlayerInstability();

        instability.add(5f);
        assertEquals(InstabilityOutcome.PERFECT, InstabilitySystem.resolveOutcome(instability, InterpretationResult.CORRECT));

        instability = new PlayerInstability();
        instability.add(25f);
        assertEquals(InstabilityOutcome.IMPERFECT, InstabilitySystem.resolveOutcome(instability, InterpretationResult.CORRECT));

        instability = new PlayerInstability();
        instability.add(55f);
        assertEquals(InstabilityOutcome.FAILURE, InstabilitySystem.resolveOutcome(instability, InterpretationResult.PARTIAL));
    }

    @Test
    void resolveOutcomeCapsPressureAtOne() {
        var instability = new PlayerInstability();
        instability.add(PlayerInstability.MAX_INSTABILITY);

        assertEquals(InstabilityOutcome.CATASTROPHIC, InstabilitySystem.resolveOutcome(instability, InterpretationResult.CATASTROPHIC_FAILURE));
    }
}
