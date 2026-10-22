package com.pmytrio.milkfrog.entity;

import net.fabricmc.api.ModInitializer;

public class PmySMilkfrogEntity implements ModInitializer {
    @Override
    public void onInitialize() {
        ModEntityTypes.registerModEntityTypes();

        ModEntityTypes.registerAttributes();
    }
}