package com.pmytrio.milkfrog.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class YellowPeachJarItem extends Item {
    public YellowPeachJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 先执行原版食用逻辑（扣饥饿值、加饱和度等）
        ItemStack result = super.finishUsingItem(stack, level, entity);

        // 仅对玩家且非创造模式生效
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack emptyJar = new ItemStack(ModItems.GLASS_JAR); // 替换为实际变量

            // 如果吃的是最后一罐，直接把手上的物品替换为空罐
            if (stack.isEmpty()) {
                return emptyJar;
            }
            // 如果还有剩余，把空罐塞进背包或掉落
            if (!player.getInventory().add(emptyJar)) {
                player.drop(emptyJar, false);
            }
        }
        return result;
    }
}