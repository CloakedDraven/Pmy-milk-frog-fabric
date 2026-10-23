package com.pmytrio.milkfrog.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class LowHealthFreezeGoal extends Goal {
    private final Mob mob;

    public LowHealthFreezeGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return this.mob.getHealth() < 2.0F;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getHealth() < 2.0F;
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.mob.getNavigation().stop();
    }
}