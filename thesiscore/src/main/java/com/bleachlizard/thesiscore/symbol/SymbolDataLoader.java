package com.bleachlizard.thesiscore.symbol;

import com.bleachlizard.thesiscore.ThesisCore;
import com.bleachlizard.thesiscore.registry.ThesisCoreRegistries;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

/**
 * Loads symbols from JSON data pack files.
 *
 * <p>Symbols can be defined in data packs under {@code data/<namespace>/symbols/}.
 * Each JSON file represents one symbol definition.
 *
 * <p>Example: {@code data/grimarcana/symbols/heat.json}
 * <pre>
 * {
 *   "id": "grimarcana:heat",
 *   "tags": ["energy", "destruction", "transformation", "motion"]
 * }
 * </pre>
 */
public class SymbolDataLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Loads all symbols from data packs.
     * This method is called during data reload and should register symbols
     * into the symbol registry.
     */
    public static void loadSymbols(ResourceManager resourceManager, Registry<Symbol> registry) {
        int loaded = 0;

        try {
            var resources = resourceManager.listResources("symbols", path -> path.getPath().endsWith(".json"));

            for (var entry : resources.entrySet()) {
                try {
                    var resource = entry.getValue();
                    var json = com.google.gson.JsonParser.parseString(
                            new String(resource.open().readAllBytes())
                    ).getAsJsonObject();

                    var result = SimpleSymbol.CODEC.decode(JsonOps.INSTANCE, json);

                    if (result.error().isPresent()) {
                        LOGGER.error("Failed to decode symbol: {}", result.error().get());
                        continue;
                    }

                    SimpleSymbol symbol = result.result().get().getFirst();

                    // Register using the proper Minecraft registry method
                    Registry.register(registry, symbol.getId(), symbol);
                    loaded++;

                } catch (Exception e) {
                    LOGGER.error("Failed to load symbol from {}", entry.getKey(), e);
                }
            }

            LOGGER.info("Loaded {} symbols from data packs", loaded);

        } catch (Exception e) {
            LOGGER.error("Error loading symbols from data packs", e);
        }
    }

    private SymbolDataLoader() {}
}
