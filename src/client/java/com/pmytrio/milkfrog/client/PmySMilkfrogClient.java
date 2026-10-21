package com.pmytrio.milkfrog.client;

import com.geckolib.renderer.GeoEntityRenderer;
import com.pmytrio.milkfrog.entity.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class PmySMilkfrogClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRenderers.register(
				ModEntityTypes.MILKSNAIL,
				context -> new GeoEntityRenderer<>(context, ModEntityTypes.MILKSNAIL)
		);
	}
}