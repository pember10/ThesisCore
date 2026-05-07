package com.bleachlizard.grimarcana;

import com.bleachlizard.grimarcana.block.GrimarcanaBlocks;
import com.bleachlizard.grimarcana.fragment.GrimarcanaFragments;
import com.bleachlizard.grimarcana.fragment.GrimarcanaRarities;
import com.bleachlizard.grimarcana.item.FragmentComponents;
import com.bleachlizard.grimarcana.item.GrimarcanaItems;
import com.bleachlizard.grimarcana.menu.GrimarcanaMenus;
import com.bleachlizard.grimarcana.network.SyncResearchedFragmentsPayload;
import com.bleachlizard.grimarcana.symbol.GrimarcanaSymbols;
import com.bleachlizard.thesiscore.registry.ThesisCoreAttachments;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

@Mod(Grimarcana.MODID)
public class Grimarcana {
    public static final String MODID = "grimarcana";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Grimarcana(IEventBus modEventBus, ModContainer modContainer) {
        GrimarcanaSymbols.SYMBOLS.register(modEventBus);
        GrimarcanaRarities.RARITIES.register(modEventBus);
        GrimarcanaFragments.FRAGMENTS.register(modEventBus);
        FragmentComponents.COMPONENTS.register(modEventBus);
        GrimarcanaItems.ITEMS.register(modEventBus);
        GrimarcanaBlocks.BLOCKS.register(modEventBus);
        GrimarcanaBlocks.BLOCK_ENTITIES.register(modEventBus);
        GrimarcanaMenus.MENUS.register(modEventBus);

        // Send researched-fragments data to the client when the player joins.
        // This ensures the tooltip research hint is available from the very first session.
        NeoForge.EVENT_BUS.addListener(Grimarcana::onPlayerLogin);
    }

    private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            var researched = sp.getData(ThesisCoreAttachments.RESEARCHED_FRAGMENTS);
            PacketDistributor.sendToPlayer(sp, new SyncResearchedFragmentsPayload(researched));
        }
    }
}
