package com.bleachlizard.grimarcana;

import com.bleachlizard.grimarcana.menu.GrimarcanaMenus;
import com.bleachlizard.grimarcana.screen.ResearchTableScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-only event subscribers for Grimarcana.
 *
 * <p>Runs only on the physical client (Dist.CLIENT), ensuring no dedicated-server
 * crashes from client-only classes.
 */
@EventBusSubscriber(modid = Grimarcana.MODID, value = Dist.CLIENT)
public class GrimarcanaClientSetup {

    @SubscribeEvent
    static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(GrimarcanaMenus.RESEARCH_TABLE.get(), ResearchTableScreen::new);
    }

    private GrimarcanaClientSetup() {}
}
