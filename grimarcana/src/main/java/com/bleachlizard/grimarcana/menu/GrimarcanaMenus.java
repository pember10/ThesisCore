package com.bleachlizard.grimarcana.menu;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers Grimarcana's container menu types.
 */
public class GrimarcanaMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.MENU, "grimarcana");

    public static final Supplier<MenuType<ResearchTableMenu>> RESEARCH_TABLE =
            MENUS.register("research_table",
                    () -> new MenuType<>(ResearchTableMenu::new, FeatureFlags.DEFAULT_FLAGS));

    private GrimarcanaMenus() {}
}
