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

    /** ----------Initialize---------- **/
    public static void Initialize(){

    }



    /** ----------Food and Drinks---------- **/
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

    public static final Item YELLOW_PEACH = register(
            "yellow_peach",
            Item::new,
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(1f)
                            .alwaysEdible()
                            .build()
    )
    );

    public static final Item YELLOW_PEACH_JAR = register(
            "yellow_peach_jar",
            YellowPeachJarItem::new,
            new Item.Properties()
                    .stacksTo(16)
                    .food(
                    new FoodProperties.Builder()
                            .nutrition(10)
                            .saturationModifier(3f)
                            .alwaysEdible()
                            .build()
            )
    );

    /** ----------Ingredients---------- **/
    public static final Item GLASS_JAR = register(
            "glass_jar",
            Item::new,
            new Item.Properties().stacksTo(16)
    );

    /** ----------Spawn Egg---------- **/
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
