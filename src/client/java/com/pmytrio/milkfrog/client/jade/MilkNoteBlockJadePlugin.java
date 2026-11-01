package com.pmytrio.milkfrog.client.jade;

import com.pmytrio.milkfrog.blocks.MilkNoteBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

/**
 * Jade entrypoint. Fabric only loads this class when the Jade mod is installed, so Jade stays a
 * soft dependency of the mod. Nothing else in the mod may reference this package.
 */
public final class MilkNoteBlockJadePlugin implements IWailaPlugin {
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(MilkNoteBlockProvider.INSTANCE, MilkNoteBlock.class);
	}
}
