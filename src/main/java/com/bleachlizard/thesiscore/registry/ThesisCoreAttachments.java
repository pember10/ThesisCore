package com.bleachlizard.thesiscore.registry;

import com.bleachlizard.thesiscore.ThesisCore;
import com.bleachlizard.thesiscore.instability.PlayerInstability;
import com.bleachlizard.thesiscore.knowledge.PlayerKnowledgeState;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Registers NeoForge data attachments used by ThesisCore.
 *
 * <p>Attachments are the NeoForge mechanism for associating arbitrary data with
 * game objects (players, chunks, etc.) in a serializable, lifecycle-aware way.
 *
 * <p>Usage — accessing a player's knowledge state:
 * <pre>{@code
 *   PlayerKnowledgeState knowledge = player.getData(ThesisCoreAttachments.PLAYER_KNOWLEDGE);
 *   PlayerInstability instability  = player.getData(ThesisCoreAttachments.PLAYER_INSTABILITY);
 * }</pre>
 */
public class ThesisCoreAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ThesisCore.MODID);

    /**
     * Stores a player's complete symbolic knowledge state.
     * Serialized to NBT via {@link PlayerKnowledgeState#CODEC} and persists across sessions.
     */
    public static final Supplier<AttachmentType<PlayerKnowledgeState>> PLAYER_KNOWLEDGE =
            ATTACHMENT_TYPES.register("player_knowledge", () ->
                    AttachmentType.builder(PlayerKnowledgeState::new)
                            .serialize(PlayerKnowledgeState.CODEC)
                            .build());

    /**
     * Stores a player's accumulated magical instability.
     * Serialized to NBT via {@link PlayerInstability#CODEC} and persists across sessions.
     */
    public static final Supplier<AttachmentType<PlayerInstability>> PLAYER_INSTABILITY =
            ATTACHMENT_TYPES.register("player_instability", () ->
                    AttachmentType.builder(PlayerInstability::new)
                            .serialize(PlayerInstability.CODEC)
                            .build());

    private ThesisCoreAttachments() {}
}
