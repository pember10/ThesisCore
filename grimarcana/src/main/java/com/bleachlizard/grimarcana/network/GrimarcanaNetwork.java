package com.bleachlizard.grimarcana.network;

import com.bleachlizard.grimarcana.Grimarcana;
import com.bleachlizard.thesiscore.registry.ThesisCoreAttachments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashSet;

/**
 * Registers Grimarcana's network payloads on the mod event bus.
 *
 * <p>The single payload {@link SyncResearchedFragmentsPayload} is sent server → client
 * to keep the client's RESEARCHED_FRAGMENTS attachment in sync so that
 * {@link com.bleachlizard.grimarcana.item.FragmentItem} can show the research hint tooltip.
 */
@EventBusSubscriber(modid = Grimarcana.MODID)
public class GrimarcanaNetwork {

    @SubscribeEvent
    static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncResearchedFragmentsPayload.TYPE,
                SyncResearchedFragmentsPayload.STREAM_CODEC,
                // Handler runs only on the client; Minecraft class is not loaded server-side
                // because playToClient handlers are never invoked on a dedicated server.
                (payload, ctx) -> ctx.enqueueWork(() -> {
                    var player = net.minecraft.client.Minecraft.getInstance().player;
                    if (player != null) {
                        player.setData(ThesisCoreAttachments.RESEARCHED_FRAGMENTS,
                                new HashSet<>(payload.researched()));
                    }
                })
        );
    }

    private GrimarcanaNetwork() {}
}
