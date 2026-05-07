package com.bleachlizard.grimarcana.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers all Grimarcana blocks and block entity types.
 */
public class GrimarcanaBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks("grimarcana");

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "grimarcana");

    // -------------------------------------------------------------------------
    // Blocks
    // -------------------------------------------------------------------------

    /**
     * The Research Table — right-click to open the research interface.
     * Placing a fragment and the correct reagent, then clicking Research,
     * permanently marks that fragment as researched for the player.
     */
    public static final Supplier<ResearchTableBlock> RESEARCH_TABLE = BLOCKS.register("research_table",
            () -> new ResearchTableBlock(Block.Properties.of()
                    .strength(2.5f)
                    .requiresCorrectToolForDrops()));

    // -------------------------------------------------------------------------
    // Block entity types
    // -------------------------------------------------------------------------

    public static final Supplier<BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE_ENTITY =
            BLOCK_ENTITIES.register("research_table", () ->
                    BlockEntityType.Builder.of(ResearchTableBlockEntity::new, RESEARCH_TABLE.get())
                            .build(null));

    private GrimarcanaBlocks() {}
}
