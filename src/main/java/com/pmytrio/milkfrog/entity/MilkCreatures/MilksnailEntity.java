package com.pmytrio.milkfrog.entity.MilkCreatures;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.LoopType;
import com.geckolib.util.GeckoLibUtil;
import com.pmytrio.milkfrog.entity.EntityGoals.LowHealthFreezeGoal;
import com.pmytrio.milkfrog.entity.ModEntityTypes;
import com.pmytrio.milkfrog.entity.MilkCreaturesGeneralMechanism.GeneralMechanism;
import com.pmytrio.milkfrog.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class MilksnailEntity extends GeneralMechanism implements GeoEntity {

    //----------Geckolib Animation Cathe----------
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    /**
     * ----------Constructor----------
     */
    public MilksnailEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }


    /**
     * ----------Attributes----------
     **/

    public static AttributeSupplier.Builder createMilksnailAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }


    /**
     * ----------GeckoLib animations----------
     **/

    @NonNull
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    // Retract into the shell and hold that pose for as long as the snail stays badly hurt.
    private static final RawAnimation RETRACT = RawAnimation.begin().thenPlayAndHold("intoshell");
    // Climb back out of the shell once health is restored. The chained "idle" stage marks the end
    // of the transition, so the controller knows when normal behaviour may resume.
    private static final RawAnimation EMERGE = RawAnimation.begin()
            .then("outofshell", LoopType.PLAY_ONCE)
            .thenLoop("idle");

    // Registers the animations. The names used here must match the keys inside
    // milksnail.animation.json exactly - GeckoLib 5 compares them verbatim, so the file keeps
    // plain names ("idle", "walk", "intoshell", "outofshell", ...) instead of Blockbench's
    // "animation." prefix.
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(new AnimationController<MilksnailEntity>("milksnail", 3, state -> {

            // Badly hurt: retract into the shell and hold that pose (see LowHealthFreezeGoal).
            if (this.getHealth() < 2.0F) {
                return state.setAndContinue(RETRACT);
            }

            // Health is back to 2 or more: climb out of the shell before doing anything else.
            // RETRACT is still the current animation while the snail sits in its shell, and EMERGE
            // stays active until its chained "idle" stage is reached (i.e. until "outofshell" ended).
            if (state.isCurrentAnimation(RETRACT)
                    || (state.isCurrentAnimation(EMERGE) && !state.isCurrentAnimationStage("idle"))) {
                return state.setAndContinue(EMERGE);
            }

            // Just fed a lollipop: laugh for a bit.
            if (this.isLaughing()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("laugh"));
            }

            // Flee when suffer damage.


            // Walk while moving, idle otherwise.
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("walk"));
            }

            return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }


    /**
     * ----------AI goals----------
     **/

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));// float in water
        this.goalSelector.addGoal(1, new LowHealthFreezeGoal(this));//Freeze when health low
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));// panic/flee
        this.goalSelector.addGoal(3, new BreedGoal(this, 0.5D));// breed
        this.goalSelector.addGoal(4, new TemptGoal(this, 0.5D, ModItems::isLollipop, false));// tempted by lollipop
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.5));// random strolling
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 4));// look at nearby players
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));// random looking around
    }



    /** ----------Breeding---------- **/

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {

        return ModEntityTypes.MILKSNAIL.create(level, net.minecraft.world.entity.EntitySpawnReason.BREEDING);
    }

}

