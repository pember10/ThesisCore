package com.bleachlizard.grimarcana;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Grimarcana.MODID)
public class Grimarcana {
    public static final String MODID = "grimarcana";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Grimarcana(IEventBus modEventBus, ModContainer modContainer) {
        // Grimarcana-specific registries and systems will be registered here.
    }
}
