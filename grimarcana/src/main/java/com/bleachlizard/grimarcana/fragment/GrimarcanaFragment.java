package com.bleachlizard.grimarcana.fragment;

import com.bleachlizard.thesiscore.knowledge.FragmentRarity;
import com.bleachlizard.thesiscore.knowledge.KnowledgeFragment;
import com.bleachlizard.thesiscore.registry.ThesisCoreRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * A Grimarcana-specific fragment of knowledge, wrapping ThesisCore's {@link KnowledgeFragment}
 * with a rarity tier that controls its signal strength range and display properties.
 *
 * <p>The rarity is a {@link ResourceKey} pointing into ThesisCore's
 * {@link com.bleachlizard.thesiscore.registry.ThesisCoreRegistries#FRAGMENT_RARITIES} registry,
 * so any mod or data pack can define additional tiers.
 *
 * <p>Example JSON (at {@code data/grimarcana/grimarcana_fragments/<name>.json}):
 * <pre>
 * {
 *   "fragment": {
 *     "id": "grimarcana:ember_trace",
 *     "description_key": "fragment.grimarcana.ember_trace",
 *     "related_symbols": ["grimarcana:heat"],
 *     "signal_strength": 0.45,
 *     "misleading": false
 *   },
 *   "rarity": "grimarcana:uncommon"
 * }
 * </pre>
 */
public class GrimarcanaFragment {

    public static final Codec<GrimarcanaFragment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    KnowledgeFragment.CODEC
                            .fieldOf("fragment")
                            .forGetter(GrimarcanaFragment::getFragment),
                    ResourceLocation.CODEC
                            .xmap(
                                    rl -> ResourceKey.create(ThesisCoreRegistries.FRAGMENT_RARITIES, rl),
                                    ResourceKey::location
                            )
                            .fieldOf("rarity")
                            .forGetter(GrimarcanaFragment::getRarityKey)
            ).apply(instance, GrimarcanaFragment::new));

    private final KnowledgeFragment fragment;
    private final ResourceKey<FragmentRarity> rarityKey;

    public GrimarcanaFragment(KnowledgeFragment fragment, ResourceKey<FragmentRarity> rarityKey) {
        this.fragment = fragment;
        this.rarityKey = rarityKey;
    }

    public KnowledgeFragment getFragment() {
        return fragment;
    }

    public ResourceKey<FragmentRarity> getRarityKey() {
        return rarityKey;
    }

    /** Convenience: the unique ID of the underlying fragment. */
    public ResourceLocation getId() {
        return fragment.getId();
    }

    /** Convenience: whether this fragment is intentionally misleading. */
    public boolean isMisleading() {
        return fragment.isMisleading();
    }

    @Override
    public String toString() {
        return "GrimarcanaFragment[" + fragment.getId() + ", rarity=" + rarityKey.location() + "]";
    }
}
