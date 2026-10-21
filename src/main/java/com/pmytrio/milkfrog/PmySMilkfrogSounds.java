package com.pmytrio.milkfrog;

import net.fabricmc.api.ModInitializer;

public class PmySMilkfrogSounds implements ModInitializer {
    @Override
    public void onInitialize() {
        CustomSounds.initialize();
    }
}