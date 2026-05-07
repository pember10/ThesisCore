package com.bleachlizard.grimarcana.item;

import com.bleachlizard.grimarcana.Grimarcana;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Data component types for Grimarcana fragment items.
 *
 * <p>{@link #FRAGMENT_ID} stores the registry ID of the {@link com.bleachlizard.grimarcana.fragment.GrimarcanaFragment}
 * that a particular item stack represents. All display and gameplay behavior is
 * derived from the fragment definition looked up via this ID at runtime.
 */
public class FragmentComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.DATA_COMPONENT_TYPE, "grimarcana");

    /**
     * Stores the {@link ResourceLocation} key of the fragment this item represents.
     * Looked up in the {@link com.bleachlizard.grimarcana.fragment.GrimarcanaFragments} registry.
     */
    public static final Supplier<DataComponentType<ResourceLocation>> FRAGMENT_ID =
            COMPONENTS.register("fragment_id",
                    () -> DataComponentType.<ResourceLocation>builder()
                            .persistent(ResourceLocation.CODEC)
                            .build());

    private FragmentComponents() {}
}
