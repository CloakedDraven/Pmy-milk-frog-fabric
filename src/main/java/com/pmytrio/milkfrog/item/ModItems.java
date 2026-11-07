package com.pmytrio.milkfrog.item;

import com.pmytrio.milkfrog.PmySMilkfrog;
import com.pmytrio.milkfrog.entity.ModEntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumables;


import java.util.Set;
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

    public static final Item CHOCOLATE_LOLLIPOP = register(
            "chocolate_lollipop",
            Item::new,
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build()
            )
    );

    public static final Item CHORUS_LOLLIPOP = register(
            "chorus_lollipop",
            Item::new,
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.2f)
                            .alwaysEdible()
                            .build()
            )
                    .component(DataComponents.CONSUMABLE, Consumables.CHORUS_FRUIT)
    );

    public static final Item HONEY_LOLLIPOP = register(
            "honey_lollipop",
            Item::new,
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.4f)
                            .alwaysEdible()
                            .build()
            )
    );

    public static final Item ICE_LOLLIPOP = register(
            "ice_lollipop",
            Item::new,
            new Item.Properties().food(
                    MobFoodEffect.ICE_LOLLIPOP_COMPONENT,
                    MobFoodEffect.ICE_LOLLIPOP_CONSUMABLE_COMPONENT
            )
    );

    public static final Item POISONOUS_LOLLIPOP = register(
            "poisonous_lollipop",
            Item::new,
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.2f)
                            .alwaysEdible()
                            .build()
            )
                    .component(DataComponents.CONSUMABLE, Consumables.SPIDER_EYE)
    );

    public static final Item GLISTERING_LOLLIPOP = register(
            "glistering_lollipop",
            Item::new,
            new Item.Properties().food(
                    MobFoodEffect.GLISTERING_LOLLIPOP_COMPONENT,
                    MobFoodEffect.GLISTERING_LOLLIPOP_CONSUMABLE_COMPONENT
            )
    );

    public static final Item GOLDEN_LOLLIPOP = register(
            "golden_lollipop",
            Item::new,
            new Item.Properties().food(
                            new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationModifier(0.7f)
                                    .alwaysEdible()
                                    .build()
                    )
                    .component(DataComponents.CONSUMABLE, Consumables.GOLDEN_APPLE)
    );

    public static final Item STRANGE_LOLLIPOP = register(
            "strange_lollipop",
            Item::new,
            new Item.Properties().food(
                    MobFoodEffect.STRANGE_LOLLIPOP_COMPONENT,
                    MobFoodEffect.STRANGE_LOLLIPOP_CONSUMABLE_COMPONENT
            )
    );

    public static final Item SUPER_SWEET_LOLLIPOP = register(
            "super_sweet_lollipop",
            Item::new,
            new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(0.4f)
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



    /** ---------Sets---------- **/
    public static final Set<Item> LOLLIPOPS = Set.of(
            LOLLIPOP, CHOCOLATE_LOLLIPOP, CHORUS_LOLLIPOP, HONEY_LOLLIPOP, ICE_LOLLIPOP,
            POISONOUS_LOLLIPOP, GLISTERING_LOLLIPOP, GOLDEN_LOLLIPOP, STRANGE_LOLLIPOP, SUPER_SWEET_LOLLIPOP
    );

    public static boolean isLollipop(ItemStack stack) {
        return !stack.isEmpty() && LOLLIPOPS.contains(stack.getItem());
    }
}
