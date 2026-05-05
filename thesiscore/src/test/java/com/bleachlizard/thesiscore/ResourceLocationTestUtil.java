package com.bleachlizard.thesiscore;

import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Constructor;

public final class ResourceLocationTestUtil {

    private ResourceLocationTestUtil() {}

    public static ResourceLocation of(String namespace, String path) {
        try {
            Constructor<ResourceLocation> ctor = ResourceLocation.class.getDeclaredConstructor(String.class, String.class);
            ctor.setAccessible(true);
            return ctor.newInstance(namespace, path);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
