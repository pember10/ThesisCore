package com.bleachlizard.thesiscore.instability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Tracks accumulated magical instability for a single player.
 *
 * <p>Instability is a raw float value that grows with each imperfect or incorrect
 * magical action. It decays slowly over time (or rapidly via XP expenditure).
 * {@link #getNormalized()} maps the raw value to 0.0–1.0 for use in outcome resolution.
 *
 * <p>The maximum instability cap is intentionally a hard limit — once reached,
 * even minor magical actions will produce {@link InstabilityOutcome#CATASTROPHIC} results.
 */
public class PlayerInstability {

    /** The instability value beyond which all actions become catastrophic. */
    public static final float MAX_INSTABILITY = 100f;

    /** Passive decay applied per second when the player is not performing magical actions. */
    public static final float PASSIVE_DECAY_PER_SECOND = 0.05f;

    public static final Codec<PlayerInstability> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("instability").forGetter(PlayerInstability::getRaw)
            ).apply(instance, PlayerInstability::new));

    private float instability;

    public PlayerInstability() {
        this.instability = 0f;
    }

    private PlayerInstability(float instability) {
        this.instability = Math.clamp(instability, 0f, MAX_INSTABILITY);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /** Raw instability value in range [0, {@value #MAX_INSTABILITY}]. */
    public float getRaw() {
        return instability;
    }

    /** Instability normalized to [0.0, 1.0]. Used by {@link InstabilitySystem} for outcome resolution. */
    public float getNormalized() {
        return instability / MAX_INSTABILITY;
    }

    public boolean isAtMax() {
        return instability >= MAX_INSTABILITY;
    }

    // -------------------------------------------------------------------------
    // Modification
    // -------------------------------------------------------------------------

    /** Adds instability from a magical action. Clamped to {@value #MAX_INSTABILITY}. */
    public void add(float amount) {
        instability = Math.clamp(instability + amount, 0f, MAX_INSTABILITY);
    }

    /**
     * Reduces instability directly. Used by XP stabilization and passive decay.
     *
     * @param amount amount to remove; clamped so instability never goes below 0
     */
    public void reduce(float amount) {
        instability = Math.max(0f, instability - amount);
    }

    /**
     * Applies one tick of passive decay. Call this on a server tick when the player
     * is not performing magical actions (e.g., every 20 ticks = once per second).
     */
    public void tickDecay() {
        reduce(PASSIVE_DECAY_PER_SECOND);
    }

    @Override
    public String toString() {
        return "PlayerInstability[" + instability + "/" + MAX_INSTABILITY + "]";
    }
}
