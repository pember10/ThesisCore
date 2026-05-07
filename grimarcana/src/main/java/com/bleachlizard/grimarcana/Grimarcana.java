package com.bleachlizard.grimarcana;

import com.bleachlizard.grimarcana.fragment.GrimarcanaFragments;
import com.bleachlizard.grimarcana.fragment.GrimarcanaRarities;
import com.bleachlizard.grimarcana.item.FragmentComponents;
import com.bleachlizard.grimarcana.item.GrimarcanaItems;
import com.bleachlizard.grimarcana.symbol.GrimarcanaSymbols;
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
        GrimarcanaSymbols.SYMBOLS.register(modEventBus);
        GrimarcanaRarities.RARITIES.register(modEventBus);
        GrimarcanaFragments.FRAGMENTS.register(modEventBus);
        FragmentComponents.COMPONENTS.register(modEventBus);
        GrimarcanaItems.ITEMS.register(modEventBus);
    }
}
