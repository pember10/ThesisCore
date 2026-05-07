package com.bleachlizard.grimarcana.fragment;

import com.bleachlizard.thesiscore.knowledge.FragmentRarity;
import com.bleachlizard.thesiscore.knowledge.KnowledgeFragment;
import com.bleachlizard.thesiscore.registry.ThesisCoreRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * A Grimarcana-specific fragment of knowledge, wrapping ThesisCore's {@link KnowledgeFragment}
 * with a rarity tier and a research reagent requirement.
 *
 * <p>The {@code reagent} field is a NeoForge {@link Ingredient} — it can match a specific item,
 * an item tag, or a list of alternatives, and is defined per-fragment in JSON. This allows
 * thematic reagents without requiring mod-specific items for every fragment.
 *
 * <p>Example JSON (at {@code data/grimarcana/grimarcana_fragments/<name>.json}):
 * <pre>
 * {
 *   "fragment": { ... },
 *   "rarity": "grimarcana:uncommon",
 *   "reagent": { "item": "minecraft:bone" }
 * }
 * </pre>
 * Or with a tag:
 * <pre>
 *   "reagent": { "tag": "minecraft:seeds" }
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
                            .forGetter(GrimarcanaFragment::getRarityKey),
                    Ingredient.CODEC
                            .fieldOf("reagent")
                            .forGetter(GrimarcanaFragment::getReagent)
            ).apply(instance, GrimarcanaFragment::new));

    private final KnowledgeFragment fragment;
    private final ResourceKey<FragmentRarity> rarityKey;
    private final Ingredient reagent;

    public GrimarcanaFragment(KnowledgeFragment fragment, ResourceKey<FragmentRarity> rarityKey, Ingredient reagent) {
        this.fragment = fragment;
        this.rarityKey = rarityKey;
        this.reagent = reagent;
    }

    public KnowledgeFragment getFragment() {
        return fragment;
    }

    public ResourceKey<FragmentRarity> getRarityKey() {
        return rarityKey;
    }

    public Ingredient getReagent() {
        return reagent;
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
