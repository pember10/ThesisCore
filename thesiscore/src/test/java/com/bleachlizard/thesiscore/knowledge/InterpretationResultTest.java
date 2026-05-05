package com.bleachlizard.thesiscore.knowledge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpretationResultTest {

    @Test
    void severityValuesMatchExpectedOrdering() {
        assertEquals(0, InterpretationResult.CORRECT.getSeverity());
        assertEquals(1, InterpretationResult.PARTIAL.getSeverity());
        assertEquals(2, InterpretationResult.INCORRECT.getSeverity());
        assertEquals(3, InterpretationResult.FAILURE.getSeverity());
        assertEquals(4, InterpretationResult.CATASTROPHIC_FAILURE.getSeverity());
    }

    @Test
    void successfulResultsAreCorrectOrPartialOnly() {
        assertTrue(InterpretationResult.CORRECT.isSuccessful());
        assertTrue(InterpretationResult.PARTIAL.isSuccessful());
        assertFalse(InterpretationResult.INCORRECT.isSuccessful());
        assertFalse(InterpretationResult.FAILURE.isSuccessful());
        assertFalse(InterpretationResult.CATASTROPHIC_FAILURE.isSuccessful());
    }

    @Test
    void misinformedResultsAreIncorrectOrCatastrophicOnly() {
        assertFalse(InterpretationResult.CORRECT.isMisinformed());
        assertFalse(InterpretationResult.PARTIAL.isMisinformed());
        assertTrue(InterpretationResult.INCORRECT.isMisinformed());
        assertFalse(InterpretationResult.FAILURE.isMisinformed());
        assertTrue(InterpretationResult.CATASTROPHIC_FAILURE.isMisinformed());
    }
}
