package com.pmytrio.milkfrog.blocks;

import com.pmytrio.milkfrog.sound.CustomSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class MilkNoteBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    // 直接用原版的 NOTE 属性（0~24）
    public static final IntegerProperty NOTE = BlockStateProperties.NOTE;

    public MilkNoteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(NOTE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, NOTE);
    }

    // 原版同款音高公式
    public static float getPitchFromNote(int note) {
        return (float) Math.pow(2.0F, (note - 12) / 12.0F);
    }

    // ================= 红石信号触发 =================
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide()) {
            boolean hasSignal = level.hasNeighborSignal(pos);
            if (hasSignal != state.getValue(POWERED)) {
                if (hasSignal) {
                    // 触发方块事件（网络同步）
                    level.blockEvent(pos, this, 0, 0);
                }
                level.setBlock(pos, state.setValue(POWERED, hasSignal), 3);
            }
        }
    }

    // ================= 右键点击 =================
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            // 原版同款优雅写法：0~24 循环
            state = state.cycle(NOTE);
            level.setBlock(pos, state, 3);
            level.blockEvent(pos, this, 0, 0);
        }
        return InteractionResult.SUCCESS;
    }

    // ================= 处理方块事件（真正发声的地方） =================
    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
        // 获取当前音高
        int note = state.getValue(NOTE);
        float pitch = getPitchFromNote(note);

        // 1. 添加原版的音符粒子效果（让画面好看一点）
        level.addParticle(ParticleTypes.NOTE,
                (double) pos.getX() + 0.5D,
                (double) pos.getY() + 1.2D,
                (double) pos.getZ() + 0.5D,
                (double) note / 24.0D, 0.0D, 0.0D);

        // 2. 播放你的自定义声音
        // 注意：原版用的是 SoundSource.RECORDS，这能让声音传得很远。你也可以用 BLOCKS
        level.playSound(null, pos, CustomSounds.LONG_CRAZY_LAUGHTER, SoundSource.BLOCKS, 1.0F, pitch);

        return true;
    }
}
