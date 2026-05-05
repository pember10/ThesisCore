package com.bleachlizard.thesiscore.knowledge;

import com.bleachlizard.thesiscore.ResourceLocationTestUtil;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeFragmentTest {

    @Test
    void gettersReturnValuesAndRelatedSymbolsAreUnmodifiable() {
        var fragment = new KnowledgeFragment(
                ResourceLocationTestUtil.of("thesiscore", "fragment"),
                "fragment.description",
                Set.of(ResourceLocationTestUtil.of("thesiscore", "heat")),
                0.75f,
                true
        );

        assertEquals("fragment.description", fragment.getDescriptionKey());
        assertEquals(0.75f, fragment.getSignalStrength(), 0.001f);
        assertTrue(fragment.isMisleading());
        assertEquals(1, fragment.getRelatedSymbols().size());
        assertThrows(UnsupportedOperationException.class, () -> fragment.getRelatedSymbols().add(ResourceLocationTestUtil.of("thesiscore", "cold")));
    }

    @Test
    void signalStrengthIsClampedBetweenZeroAndOne() {
        var fragment = new KnowledgeFragment(
                ResourceLocationTestUtil.of("thesiscore", "fragment"),
                "fragment.description",
                Set.of(),
                1.5f,
                false
        );

        assertEquals(1f, fragment.getSignalStrength(), 0.001f);

        var fragmentLow = new KnowledgeFragment(
                ResourceLocationTestUtil.of("thesiscore", "fragment"),
                "fragment.description",
                Set.of(),
                -0.5f,
                false
        );

        assertEquals(0f, fragmentLow.getSignalStrength(), 0.001f);
    }

    @Test
    void toStringIncludesIdAndMisleadingFlag() {
        var fragment = new KnowledgeFragment(
                ResourceLocationTestUtil.of("thesiscore", "fragment"),
                "fragment.description",
                Set.of(),
                0.25f,
                true
        );

        assertTrue(fragment.toString().contains("thesiscore:fragment"));
        assertTrue(fragment.toString().contains("misleading=true"));
    }
}
