package com.pmytrio.milkfrog.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import static com.pmytrio.milkfrog.sound.CustomSounds.LAUGHTER_NOTE;

public class MilkNoteBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty NOTE = BlockStateProperties.NOTE;

    public MilkNoteBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(NOTE, 0)
                        .setValue(POWERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NOTE, POWERED);
    }

    @Override
    protected void neighborChanged(
            final BlockState state, final Level level, final BlockPos pos, final Block block, final @Nullable Orientation orientation, final boolean movedByPiston
    ) {
        boolean signal = level.hasNeighborSignal(pos);
        if (signal != state.getValue(POWERED)) {
            if (signal) {
                this.playMilkNote(null, state, level, pos);
            }

            level.setBlock(pos, state.setValue(POWERED, signal), 3);
        }
    }

    private void playMilkNote(final @Nullable Entity source, final BlockState state, final Level level, final BlockPos pos) {
        if (level.getBlockState(pos.above()).isAir()) {
            level.blockEvent(pos, this, 0, 0);
            level.gameEvent(source, GameEvent.NOTE_BLOCK_PLAY, pos);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            state = state.cycle(NOTE);
            level.setBlock(pos, state, 3);
            this.playMilkNote(player, state, level, pos);
            player.awardStat(Stats.TUNE_NOTEBLOCK);
        }

        return InteractionResult.SUCCESS;
    }

    protected void attack(final BlockState state, final Level level, final BlockPos pos, final Player player) {
        if (!level.isClientSide()) {
            this.playMilkNote(player, state, level, pos);
            player.awardStat(Stats.PLAY_NOTEBLOCK);
        }
    }

    public static float getPitchFromNote(final int twoOctaveRangeNote) {
        return (float)Math.pow(2.0, (twoOctaveRangeNote - 12) / 12.0);
    }

    @Override
    protected boolean triggerEvent(final BlockState state, final Level level, final BlockPos pos,
                                   final int b0, final int b1) {
        int note = state.getValue(NOTE);
        float pitch = getPitchFromNote(note);

        level.addParticle(ParticleTypes.NOTE, pos.getX()+0.5, pos.getY()+1.2, pos.getZ()+0.5,
                note/24.0, 0.0, 0.0);

        level.playSeededSound(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5,
                Holder.direct(LAUGHTER_NOTE), SoundSource.RECORDS,
                3.0F, pitch, level.getRandom().nextLong());
        return true;
    }
}