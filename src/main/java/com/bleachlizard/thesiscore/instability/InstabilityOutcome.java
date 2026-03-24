package com.bleachlizard.thesiscore.instability;

/**
 * The experienced outcome of a magical action, combining the player's knowledge quality
 * with their accumulated instability level.
 *
 * <p>This is what the player sees and feels — not the internal judgment of how correct
 * their belief was, but the actual consequence of performing a magical act in their
 * current state. A player with high instability worsens every outcome category.
 *
 * <p>Outcome thresholds are based on a normalized instability score (0.0–1.0),
 * then shifted upward by the severity of the underlying {@link com.bleachlizard.thesiscore.knowledge.InterpretationResult}.
 */
public enum InstabilityOutcome {

    /**
     * The action succeeded cleanly. No negative side effects.
     * Only achievable at low instability with correct interpretation.
     */
    PERFECT,

    /**
     * The action worked but not optimally — slightly reduced effect, minor waste.
     * The player may notice something feels slightly off.
     */
    IMPERFECT,

    /**
     * The action produced unpredictable behaviour. Effects may be wrong, delayed,
     * or interact poorly with other systems. Visible instability feedback begins here.
     */
    UNSTABLE,

    /**
     * The action failed outright. No useful effect was produced.
     * Instability was still consumed but nothing positive happened.
     */
    FAILURE,

    /**
     * Something went seriously wrong. Triggers instability consequences,
     * world reactivity, or direct harm. The worst outcome.
     */
    CATASTROPHIC;

    /** Returns {@code true} if the outcome produced a usable result. */
    public boolean isUsable() {
        return this == PERFECT || this == IMPERFECT;
    }

    /** Returns {@code true} if the outcome may trigger world reactivity or consequence systems. */
    public boolean triggersConsequences() {
        return this == CATASTROPHIC;
    }
}
