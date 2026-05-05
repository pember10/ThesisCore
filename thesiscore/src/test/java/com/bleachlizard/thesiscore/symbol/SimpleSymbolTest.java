package com.bleachlizard.thesiscore.symbol;

import com.bleachlizard.thesiscore.ResourceLocationTestUtil;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSymbolTest {

    @Test
    void constructorCopiesAndFreezesTags() {
        var tags = Set.of("energy", "heat");
        var symbol = new SimpleSymbol(ResourceLocationTestUtil.of("thesiscore", "heat"), tags);

        assertEquals(tags, symbol.getTags());
        assertThrows(UnsupportedOperationException.class, () -> symbol.getTags().add("extra"));
    }

    @Test
    void getIdAndTagsReturnExpectedValues() {
        var id = ResourceLocationTestUtil.of("thesiscore", "heat");
        var symbol = new SimpleSymbol(id, Set.of("energy"));

        assertSame(id, symbol.getId());
        assertEquals(Set.of("energy"), symbol.getTags());
    }

    @Test
    void toStringContainsId() {
        var symbol = new SimpleSymbol(ResourceLocationTestUtil.of("thesiscore", "heat"), Set.of("energy"));

        assertTrue(symbol.toString().contains("thesiscore:heat"));
    }
}
