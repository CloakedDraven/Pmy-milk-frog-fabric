package com.pmytrio.milkfrog;

import com.pmytrio.milkfrog.blocks.ModBlocks;
import com.pmytrio.milkfrog.item.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class ModCreativeTabs {

    public static void registerCreativeTabEntries() {

    /** ----------Items---------- **/
    /* ----------Food and Drinks---------- **/
    //lollipop
    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
            .register((creativeTab) -> creativeTab.accept(ModItems.LOLLIPOP));

    //yellow_peach
    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
            .register((creativeTab) -> creativeTab.accept(ModItems.YELLOW_PEACH));

    //yellow_peach_jar
    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
            .register((creativeTab) -> creativeTab.accept(ModItems.YELLOW_PEACH_JAR));

    /* ----------Ingredients---------- **/
    //yellow_peach_jar
    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
            .register((creativeTab) -> creativeTab.accept(ModItems.GLASS_JAR));

    /* ----------Spawn Eggs---------- **/
    //milksnail_spawn_egg
    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
            .register((creativeTab) -> creativeTab.accept(ModItems.MILKSNAIL_SPAWN_EGG));

    /* ----------Blocks---------- **/
    //yellow_peach_block
    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> {
        creativeTab.accept(ModBlocks.YELLOW_PEACH_BLOCK.asItem());
    });

    //milk_note_block
    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((creativeTab) -> {
        creativeTab.accept(ModBlocks.MILK_NOTE_BLOCK.asItem());
    });
}
}
