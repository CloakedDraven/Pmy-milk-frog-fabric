package com.pmytrio.milkfrog.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class ModEntitySpawns {

    // 获取原版的“森林”标签，这个标签包含了所有树林（橡木林、白桦林、黑森林、丛林等）
    private static final TagKey<Biome> IS_FOREST_TAG = TagKey.create(
            Registries.BIOME,
            Identifier.fromNamespaceAndPath("minecraft", "is_forest")
    );
    public static void registerSpawns() {

        // 1. 繁花森林、平原、向日葵平原（权重：5，数量：2~4）
        // 注意：繁花森林其实也算“森林”，但这里为了给你演示不同概率，把它单独列出来了
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS),
                MobCategory.CREATURE,
                ModEntityTypes.MILKSNAIL,
                2, 2, 4
        );

        // 2. 蘑菇岛（权重：3，数量：1~2）蘑菇岛比较特殊，让它少刷一点
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.MUSHROOM_FIELDS),
                MobCategory.CREATURE,
                ModEntityTypes.MILKSNAIL,
                3, 1, 2
        );

        // 3. 所有树林（使用标签，权重：10，数量：2~4）
        // 配合上面单独的繁花森林，森林类群系的奶蜗会非常丰富
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(IS_FOREST_TAG),
                MobCategory.CREATURE,
                ModEntityTypes.MILKSNAIL,
                5, 2, 4
        );

        // （可选）樱花树林是 1.20 加入的，不在原版 is_forest 标签里，如果你想要也可以单独加
        // BiomeModifications.addSpawn(
        //     BiomeSelectors.includeByKey(Biomes.CHERRY_GROVE),
        //     MobCategory.CREATURE,
        //     ModEntityTypes.MILKSNAIL,
        //     10, 2, 4
        // );
    }
}