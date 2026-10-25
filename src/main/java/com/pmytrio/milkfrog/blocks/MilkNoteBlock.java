package com.pmytrio.milkfrog.blocks;

import com.pmytrio.milkfrog.sound.CustomSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MilkNoteBlock extends Block {
    public MilkNoteBlock(Properties properties) {
        super(properties);
    }

    // 1.21.2+ 的签名，如果编译报错可尝试旧版的 onUse
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        System.out.println("[MilkNoteBlock] useWithoutItem called, client=" + level.isClientSide());
        if (!level.isClientSide()) {

            level.playSound(null, pos, CustomSounds.LONG_CRAZY_LAUGHTER, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }
}
