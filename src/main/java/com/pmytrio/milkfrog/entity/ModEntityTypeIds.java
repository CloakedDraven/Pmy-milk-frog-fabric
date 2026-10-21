package com.pmytrio.milkfrog.entity;

import com.pmytrio.milkfrog.PmySMilkfrog;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeIds {
    public static ResourceKey<EntityType<?>> MILKSNAIL = create("milksnail");

    private static ResourceKey<EntityType<?>> create(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, PmySMilkfrog.id(name));
    }
}