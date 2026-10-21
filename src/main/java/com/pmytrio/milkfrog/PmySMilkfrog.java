package com.pmytrio.milkfrog;

import com.pmytrio.milkfrog.entity.ModEntitySpawns;
import com.pmytrio.milkfrog.entity.ModEntityTypes;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PmySMilkfrog implements ModInitializer {
	public static final String MOD_ID = "pmymilkfrog";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModEntityTypes.registerModEntityTypes();
		ModEntityTypes.registerAttributes();
		ModEntitySpawns.registerSpawns();
		CustomSounds.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	}

