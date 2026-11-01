package com.pmytrio.milkfrog.client.jade;

import com.pmytrio.milkfrog.blocks.MilkNoteBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

/**
 * Jade tooltip component for the Milk Note Block. Mirrors Jade's built-in vanilla note block
 * support: just the note name (enharmonic spellings like {@code F#/G♭}), coloured by octave.
 */
public enum MilkNoteBlockProvider implements IBlockComponentProvider {
	INSTANCE;

	public static final Identifier UID = Identifier.fromNamespaceAndPath("pmymilkfrog", "milk_note_block");

	/** Same pitch names Jade uses for the vanilla note block; vanilla note 0 is F#. */
	private static final String[] PITCH = {
			"F#/G♭", "G", "G#/A♭", "A", "A#/B♭", "B",
			"C", "C#/D♭", "D", "D#/E♭", "E", "F"
	};
	/** Octave colours, same palette Jade uses for note blocks. */
	private static final ChatFormatting[] OCTAVE = {ChatFormatting.WHITE, ChatFormatting.YELLOW, ChatFormatting.GOLD};
	private static final ChatFormatting[] OCTAVE_LIGHT = {ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_BLUE, ChatFormatting.BLUE};

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		BlockState state = accessor.getBlockState();
		if (!(state.getBlock() instanceof MilkNoteBlock)) {
			return;
		}

		int note = state.getValue(MilkNoteBlock.NOTE);
		int octaveBand = note / PITCH.length; // 0..2, used for the colour, exactly like vanilla
		ChatFormatting[] palette = IThemeHelper.get().isLightColorScheme() ? OCTAVE_LIGHT : OCTAVE;
		ChatFormatting color = palette[Math.min(octaveBand, palette.length - 1)];

		tooltip.add(Component.literal(PITCH[note % PITCH.length]).withStyle(color));
	}

	@Override
	public Identifier getUid() {
		return UID;
	}
}
