package com.bleachlizard.thesiscore.knowledge;

/**
 * The outcome of evaluating a player's belief about a symbol against system truth.
 *
 * <p>Severity increases from {@code CORRECT} to {@code CATASTROPHIC_FAILURE}.
 * A result of {@code INCORRECT} means the player has formed a false belief —
 * they believe they understand something, but they are wrong. This is the most
 * dangerous non-catastrophic state, because the player does not know they are wrong.
 */
public enum InterpretationResult {

    /** The player's understood tags fully match the symbol's actual tags. */
    CORRECT(0),

    /** The player understands some tags correctly but not all — incomplete, not wrong. */
    PARTIAL(1),

    /**
     * The player's believed tags are substantially wrong or contradictory.
     * The system knows; the player does not. Effects will quietly misbehave.
     */
    INCORRECT(2),

    /** The player had insufficient knowledge to produce a valid interpretation. */
    FAILURE(3),

    /**
     * The player held a confident, deeply incorrect belief about a critical symbol.
     * Triggers instability consequences. The worst outcome.
     */
    CATASTROPHIC_FAILURE(4);

    private final int severity;

    InterpretationResult(int severity) {
        this.severity = severity;
    }

    /** 0 = best, 4 = worst. */
    public int getSeverity() {
        return severity;
    }

    /** Returns {@code true} for outcomes that produce usable (if imperfect) results. */
    public boolean isSuccessful() {
        return this == CORRECT || this == PARTIAL;
    }

    /** Returns {@code true} if the player is operating on false information. */
    public boolean isMisinformed() {
        return this == INCORRECT || this == CATASTROPHIC_FAILURE;
    }
}
