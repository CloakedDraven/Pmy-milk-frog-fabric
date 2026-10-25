package com.pmytrio.milkfrog.blocks;

import com.pmytrio.milkfrog.PmySMilkfrog;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {

    /** ----------Blocks---------- **/
    public static final Block YELLOW_PEACH_BLOCK = register(
            "yellow_peach_block",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.GRASS)
                    .strength(1.5f),
            true
    );

    public static final Block MILK_NOTE_BLOCK = register(
            "milk_note_block",
            MilkNoteBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOD)
                    .strength(2.5f),
            true
    );



    /** ----------Initialize---------- **/
    public static void initialize() {
    }



    /** ----------Register---------- **/
    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties, boolean shouldRegisterItem) {

        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(properties.setId(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(PmySMilkfrog.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PmySMilkfrog.MOD_ID, name));
    }
}