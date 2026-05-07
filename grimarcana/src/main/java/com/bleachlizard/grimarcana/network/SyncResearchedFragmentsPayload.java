package com.bleachlizard.grimarcana.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

/**
 * Server → Client payload that carries the full set of fragment IDs the player has researched.
 *
 * <p>Sent when the player logs in and whenever a new research completes. The client stores
 * the set on its local player entity via the RESEARCHED_FRAGMENTS attachment so that
 * {@link com.bleachlizard.grimarcana.item.FragmentItem} can show the research hint in
 * the item tooltip without needing a server round-trip.
 */
public record SyncResearchedFragmentsPayload(Set<ResourceLocation> researched)
        implements CustomPacketPayload {

    public static final Type<SyncResearchedFragmentsPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("grimarcana", "sync_researched"));

    public static final StreamCodec<FriendlyByteBuf, SyncResearchedFragmentsPayload> STREAM_CODEC =
            StreamCodec.of(SyncResearchedFragmentsPayload::encode, SyncResearchedFragmentsPayload::decode);

    private static void encode(FriendlyByteBuf buf, SyncResearchedFragmentsPayload payload) {
        buf.writeVarInt(payload.researched().size());
        for (ResourceLocation id : payload.researched()) {
            buf.writeUtf(id.toString());
        }
    }

    private static SyncResearchedFragmentsPayload decode(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        Set<ResourceLocation> set = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            set.add(ResourceLocation.parse(buf.readUtf()));
        }
        return new SyncResearchedFragmentsPayload(set);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
