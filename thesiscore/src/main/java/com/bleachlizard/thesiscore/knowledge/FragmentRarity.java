package com.bleachlizard.thesiscore.knowledge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Defines a named rarity tier for {@link KnowledgeFragment}s.
 *
 * <p>Rarities control how much signal strength a fragment conveys and how it
 * is displayed to the player. ThesisCore defines no default rarities — consuming
 * mods define their own tiers via {@code DeferredRegister<FragmentRarity>}.
 *
 * <p>Rarities live in the {@link com.bleachlizard.thesiscore.registry.ThesisCoreRegistries#FRAGMENT_RARITIES}
 * registry so any mod or data pack can add entirely new tiers.
 *
 * <p>Example JSON (at {@code data/<namespace>/fragment_rarities/<name>.json}):
 * <pre>
 * {
 *   "signal_strength_min": 0.1,
 *   "signal_strength_max": 0.3,
 *   "display_name_key": "fragment_rarity.grimarcana.common",
 *   "text_color": 16777215
 * }
 * </pre>
 */
public class FragmentRarity {

    public static final Codec<FragmentRarity> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT
                            .fieldOf("signal_strength_min")
                            .forGetter(FragmentRarity::getSignalStrengthMin),
                    Codec.FLOAT
                            .fieldOf("signal_strength_max")
                            .forGetter(FragmentRarity::getSignalStrengthMax),
                    Codec.STRING
                            .fieldOf("display_name_key")
                            .forGetter(FragmentRarity::getDisplayNameKey),
                    Codec.INT
                            .fieldOf("text_color")
                            .forGetter(FragmentRarity::getTextColor)
            ).apply(instance, FragmentRarity::new));

    /** The minimum signal strength a fragment of this rarity can have. */
    private final float signalStrengthMin;
    /** The maximum signal strength a fragment of this rarity can have. */
    private final float signalStrengthMax;
    /** Translation key for the rarity's display name (e.g. "Common", "Legendary"). */
    private final String displayNameKey;
    /** ARGB text color used when displaying the rarity name in tooltips and UI. */
    private final int textColor;

    public FragmentRarity(float signalStrengthMin, float signalStrengthMax, String displayNameKey, int textColor) {
        this.signalStrengthMin = Math.clamp(signalStrengthMin, 0f, 1f);
        this.signalStrengthMax = Math.clamp(signalStrengthMax, 0f, 1f);
        this.displayNameKey = displayNameKey;
        this.textColor = textColor;
    }

    public float getSignalStrengthMin() {
        return signalStrengthMin;
    }

    public float getSignalStrengthMax() {
        return signalStrengthMax;
    }

    public String getDisplayNameKey() {
        return displayNameKey;
    }

    public int getTextColor() {
        return textColor;
    }

    /**
     * Returns the midpoint signal strength for this rarity tier.
     * Used as a default when a fragment does not specify an explicit signal strength.
     */
    public float getMidpointSignalStrength() {
        return (signalStrengthMin + signalStrengthMax) / 2f;
    }

    @Override
    public String toString() {
        return "FragmentRarity[" + displayNameKey + ", " + signalStrengthMin + "-" + signalStrengthMax + "]";
    }
}
