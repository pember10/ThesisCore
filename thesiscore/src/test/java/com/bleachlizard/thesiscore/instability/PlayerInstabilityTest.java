package com.bleachlizard.thesiscore.instability;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInstabilityTest {

    @Test
    void addClampsAtMax() {
        var instability = new PlayerInstability();

        instability.add(120f);

        assertEquals(PlayerInstability.MAX_INSTABILITY, instability.getRaw(), 0.001f);
        assertTrue(instability.isAtMax());
    }

    @Test
    void reduceFloorsAtZero() {
        var instability = new PlayerInstability();

        instability.add(10f);
        instability.reduce(20f);

        assertEquals(0f, instability.getRaw(), 0.001f);
    }

    @Test
    void normalizedIsRawDividedByMax() {
        var instability = new PlayerInstability();

        instability.add(50f);

        assertEquals(0.5f, instability.getNormalized(), 0.001f);
    }

    @Test
    void addNegativeAmountReducesInstabilityWhenPossible() {
        var instability = new PlayerInstability();

        instability.add(10f);
        instability.add(-5f);

        assertEquals(5f, instability.getRaw(), 0.001f);
    }

    @Test
    void tickDecayDoesNotDropBelowZero() {
        var instability = new PlayerInstability();

        instability.tickDecay();

        assertEquals(0f, instability.getRaw(), 0.001f);
    }

    @Test
    void toStringIncludesCurrentValue() {
        var instability = new PlayerInstability();
        instability.add(7.5f);

        assertTrue(instability.toString().contains("7.5"));
    }

    @Test
    void tickDecayReducesValue() {
        var instability = new PlayerInstability();

        instability.add(1f);
        instability.tickDecay();

        assertEquals(1f - PlayerInstability.PASSIVE_DECAY_PER_SECOND, instability.getRaw(), 0.001f);
    }
}
