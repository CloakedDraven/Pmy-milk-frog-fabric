package com.pmytrio.milkfrog.sound;

import com.pmytrio.milkfrog.PmySMilkfrog;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class CustomSounds {
    private CustomSounds() {
    }

    public static final SoundEvent SHORT_CRAZY_LAUGHTER = registerSound("short_crazy_laughter");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = PmySMilkfrog.id(id);
        return Registry.register(
                BuiltInRegistries.SOUND_EVENT,
                identifier,
                SoundEvent.createVariableRangeEvent(identifier)
        );
    }

    public static void initialize() {
        PmySMilkfrog.LOGGER.info("Registering " + PmySMilkfrog.MOD_ID + " Sounds");
    }
}