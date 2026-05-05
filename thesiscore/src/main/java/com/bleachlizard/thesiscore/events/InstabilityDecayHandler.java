package com.bleachlizard.thesiscore.events;

import com.bleachlizard.thesiscore.registry.ThesisCoreAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Handles server-side tick events for ThesisCore's passive systems.
 *
 * <p>Registered on {@code NeoForge.EVENT_BUS} (the game event bus) in
 * {@link com.bleachlizard.thesiscore.ThesisCore}. Not a mod-bus subscriber.
 */
public class InstabilityDecayHandler {

    /**
     * Every 20 ticks (once per second), apply passive instability decay to
     * all online players. This allows instability to drain naturally when
     * players are not performing magical actions.
     */
    @SubscribeEvent
    public void onServerTickPost(ServerTickEvent.Post event) {
        if (event.getServer().getTickCount() % 20 != 0) return;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            player.getData(ThesisCoreAttachments.PLAYER_INSTABILITY).tickDecay();
        }
    }
}
