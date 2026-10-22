package com.pmytrio.milkfrog.item;

import com.pmytrio.milkfrog.PmySMilkfrog;
import com.pmytrio.milkfrog.blocks.ModBlocks;
import com.pmytrio.milkfrog.entity.ModEntityTypes;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;


import java.util.function.Function;


public class ModItems {

    public static void initialize() {
        //lollipop
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((creativeTab) -> creativeTab.accept(ModItems.LOLLIPOP));

        //milk_dust
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> creativeTab.accept(ModItems.MILK_DUST));

        //milksnail_spawn_egg
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
                .register((creativeTab) -> creativeTab.accept(ModItems.MILKSNAIL_SPAWN_EGG));

        //milk_block.json
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> {
            creativeTab.accept(ModBlocks.MILK_BLOCK.asItem());
        });
    }



    public static final Item LOLLIPOP = register(
            "lollipop",
            Item::new,
            new Item.Properties().food(
                    new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.2f)
                        .alwaysEdible()
                        .build()
            )
    );

    public static final Item MILK_DUST = register(
            "milk_dust",
            Item::new,
            new Item.Properties()
    );

    public static final Item MILKSNAIL_SPAWN_EGG = register(
            "milksnail_spawn_egg",
            SpawnEggItem::new,
            new Item.Properties().spawnEgg(
                    ModEntityTypes.MILKSNAIL
            )
            );



    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PmySMilkfrog.MOD_ID, name));

        T item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}
