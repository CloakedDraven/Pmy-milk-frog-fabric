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

    private static final TagKey<Biome> IS_FOREST_TAG = TagKey.create(
            Registries.BIOME,
            Identifier.fromNamespaceAndPath("minecraft", "is_forest")
    );
    public static void registerSpawns() {

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS),
                MobCategory.CREATURE,
                ModEntityTypes.MILKSNAIL,
                3, 2, 4
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.MUSHROOM_FIELDS),
                MobCategory.CREATURE,
                ModEntityTypes.MILKSNAIL,
                3, 1, 2
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.tag(IS_FOREST_TAG),
                MobCategory.CREATURE,
                ModEntityTypes.MILKSNAIL,
                5, 2, 4
        );

    }
}