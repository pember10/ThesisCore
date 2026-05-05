package com.bleachlizard.thesiscore;

import com.bleachlizard.thesiscore.events.InstabilityDecayHandler;
import com.bleachlizard.thesiscore.registry.SymbolRegistry;
import com.bleachlizard.thesiscore.registry.ThesisCoreAttachments;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(ThesisCore.MODID)
public class ThesisCore {
    public static final String MODID = "thesiscore";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ThesisCore(IEventBus modEventBus, ModContainer modContainer) {
        SymbolRegistry.SYMBOLS.register(modEventBus);
        ThesisCoreAttachments.ATTACHMENT_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(new InstabilityDecayHandler());
    }
}
