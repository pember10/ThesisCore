package com.bleachlizard.grimarcana.item;

import com.bleachlizard.grimarcana.fragment.GrimarcanaFragment;
import com.bleachlizard.grimarcana.fragment.GrimarcanaFragments;
import com.bleachlizard.thesiscore.knowledge.FragmentRarity;
import com.bleachlizard.thesiscore.knowledge.PlayerKnowledgeState;
import com.bleachlizard.thesiscore.registry.ThesisCoreAttachments;
import com.bleachlizard.thesiscore.registry.ThesisCoreRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * The Minecraft item representation of a {@link GrimarcanaFragment}.
 *
 * <p>One item class is used for all fragments of a given rarity tier. The specific
 * fragment definition is stored in the {@link FragmentComponents#FRAGMENT_ID} data
 * component on the stack, which is looked up at runtime from the fragment registry.
 *
 * <p>Using the item applies evidence from the fragment to the player's
 * {@link PlayerKnowledgeState}. The signal strength is taken from the fragment definition.
 * The player cannot tell whether the fragment is misleading until they research it.
 */
public class FragmentItem extends Item {

    public FragmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        Optional<GrimarcanaFragment> fragment = resolveFragment(stack);
        if (fragment.isEmpty()) return super.getName(stack);

        FragmentRarity rarity = resolveRarity(fragment.get(), stack);
        if (rarity == null) return super.getName(stack);

        return Component.translatable(rarity.getDisplayNameKey())
                .append(" ")
                .append(Component.translatable(getDescriptionId(stack)))
                .withStyle(Style.EMPTY.withColor(rarity.getTextColor()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        Optional<GrimarcanaFragment> fragment = resolveFragment(stack);
        if (fragment.isEmpty()) return;

        // Vague description — always shown
        lines.add(Component.translatable(fragment.get().getFragment().getDescriptionKey())
                .withStyle(ChatFormatting.GRAY));

        // Signal strength hint — phrased as completeness, not raw number
        float signal = fragment.get().getFragment().getSignalStrength();
        lines.add(Component.translatable(signalHintKey(signal))
                .withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResultHolder.pass(stack);

        Optional<GrimarcanaFragment> fragment = resolveFragment(stack);
        if (fragment.isEmpty()) return InteractionResultHolder.fail(stack);

        var knowledgeFragment = fragment.get().getFragment();
        PlayerKnowledgeState knowledge = player.getData(ThesisCoreAttachments.PLAYER_KNOWLEDGE);

        // Apply evidence for each related symbol
        for (var symbolId : knowledgeFragment.getRelatedSymbols()) {
            knowledge.applyEvidence(
                    symbolId,
                    // We only pass signal-strength here; the belief system handles the rest.
                    // The tags come from the symbol registry, not the fragment — the player
                    // doesn't get tag information directly, just confidence.
                    java.util.Set.of(),
                    knowledgeFragment.getSignalStrength()
            );
        }

        player.displayClientMessage(
                Component.translatable("fragment.grimarcana.studied")
                        .withStyle(ChatFormatting.AQUA),
                true);

        return InteractionResultHolder.success(stack);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static Optional<GrimarcanaFragment> resolveFragment(ItemStack stack) {
        ResourceLocation id = stack.get(FragmentComponents.FRAGMENT_ID.get());
        if (id == null) return Optional.empty();
        var registry = BuiltInRegistries.REGISTRY.get(GrimarcanaFragments.REGISTRY_KEY.location());
        if (registry == null) return Optional.empty();
        return ((net.minecraft.core.Registry<GrimarcanaFragment>) registry).getOptional(id);
    }

    @SuppressWarnings("unchecked")
    private static FragmentRarity resolveRarity(GrimarcanaFragment fragment, ItemStack stack) {
        var rarityKey = fragment.getRarityKey();
        var registry = BuiltInRegistries.REGISTRY.get(ThesisCoreRegistries.FRAGMENT_RARITIES.location());
        if (registry == null) return null;
        return ((net.minecraft.core.Registry<FragmentRarity>) registry).getOptional(rarityKey.location()).orElse(null);
    }

    private static String signalHintKey(float signal) {
        if (signal < 0.3f) return "fragment.grimarcana.signal.faint";
        if (signal < 0.5f) return "fragment.grimarcana.signal.partial";
        if (signal < 0.7f) return "fragment.grimarcana.signal.clear";
        if (signal < 0.85f) return "fragment.grimarcana.signal.strong";
        return "fragment.grimarcana.signal.complete";
    }
}
