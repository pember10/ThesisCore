package com.bleachlizard.thesiscore.registry;

import com.bleachlizard.thesiscore.ThesisCore;
import com.bleachlizard.thesiscore.symbol.Symbol;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Defines and creates the custom registries used by ThesisCore.
 *
 * {@code @EventBusSubscriber} causes NeoForge to auto-discover the
 * {@code @SubscribeEvent} methods here on the MOD bus at load time.
 */
@EventBusSubscriber(modid = ThesisCore.MODID)
public class ThesisCoreRegistries {

    /**
     * Registry key for the Symbol registry.
     * Other mods can register their own symbols under any namespace using their own
     * {@code DeferredRegister<Symbol>} pointed at this key.
     */
    public static final ResourceKey<Registry<Symbol>> SYMBOLS =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ThesisCore.MODID, "symbol"));

    @SubscribeEvent
    static void onNewRegistry(NewRegistryEvent event) {
        event.create(new RegistryBuilder<>(SYMBOLS));
    }

    private ThesisCoreRegistries() {}
}
