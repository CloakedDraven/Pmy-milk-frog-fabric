package com.pmytrio.milkfrog;

import com.pmytrio.milkfrog.entity.ModEntityTypes;
import net.fabricmc.api.ModInitializer;

public class PmySMilkfrogEntity implements ModInitializer {
    @Override
    public void onInitialize() {
        ModEntityTypes.registerModEntityTypes();

        ModEntityTypes.registerAttributes();
    }
}