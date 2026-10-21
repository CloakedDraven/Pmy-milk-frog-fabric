package com.pmytrio.milkfrog.entity;

import com.pmytrio.milkfrog.PmySMilkfrog;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {
    public static final EntityType<MilksnailEntity> MILKSNAIL = register(
            ModEntityTypeIds.MILKSNAIL,
            EntityType.Builder.<MilksnailEntity>of(MilksnailEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 1.75f)
    );

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        PmySMilkfrog.LOGGER.info("Registering EntityTypes for " + PmySMilkfrog.MOD_ID);
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(MILKSNAIL, MilksnailEntity.createMilksnailAttributes());
    }
}