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

        ItemStack result = super.finishUsingItem(stack, level, entity);


        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack emptyJar = new ItemStack(ModItems.GLASS_JAR);


            if (stack.isEmpty()) {
                return emptyJar;
            }

            if (!player.getInventory().add(emptyJar)) {
                player.drop(emptyJar, false);
            }
        }
        return result;
    }
}