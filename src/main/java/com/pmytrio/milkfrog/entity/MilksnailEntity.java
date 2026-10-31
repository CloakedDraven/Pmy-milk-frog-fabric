package com.pmytrio.milkfrog.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.LoopType;
import com.geckolib.util.GeckoLibUtil;
import com.pmytrio.milkfrog.sound.CustomSounds;
import com.pmytrio.milkfrog.item.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class MilksnailEntity extends Animal implements GeoEntity {

    /**
     * ----------Constants----------
     **/

    private static final int LAUGH_DURATION = 80;

    /**
     * ----------State Fields----------
     **/

    //----------Geckolib Animation Cathe----------
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    //----------Laughter Control Parameters----------
    // Synced so the client knows when to play the "laugh" animation.
    private static final EntityDataAccessor<Boolean> DATA_LAUGHING =
            SynchedEntityData.defineId(MilksnailEntity.class, EntityDataSerializers.BOOLEAN);
    private int laughTicks = 0;


    /**
     * ----------Constructor----------
     **/
    public MilksnailEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LAUGHING, false);
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
        this.goalSelector.addGoal(4, new TemptGoal(this, 0.5, Ingredient.of(ModItems.LOLLIPOP), false));// tempted by lollipop
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.5));// random strolling
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 4));// look at nearby players
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));// random looking around
    }


    /**
     * ----------Per-tick logic----------
     **/
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide() && this.isLaughing()) {
            this.getNavigation().stop();

            if (--this.laughTicks <= 0) {
                this.stopLaughing();
            }
        }
    }



    /** ---------Laugh state---------- **/

    public void startLaughing() {
        this.entityData.set(DATA_LAUGHING, true);
        this.laughTicks = LAUGH_DURATION;


        this.getNavigation().stop();
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    public void stopLaughing() {
        this.entityData.set(DATA_LAUGHING, false);
        this.laughTicks = 0;
    }

    public boolean isLaughing() {
        return this.entityData.get(DATA_LAUGHING);
    }



    /** ----------Breeding---------- **/

    public boolean isBreedingOnCooldown() {
        return this.getAge() > 0;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.LOLLIPOP);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        // Generate a chick temporarily.
        return ModEntityTypes.MILKSNAIL.create(level, net.minecraft.world.entity.EntitySpawnReason.BREEDING);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
        super.spawnChildFromBreeding(level, partner);

        for (int i = 0; i < 10; ++i) {
            double x = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * 5.0D;
            double y = this.getY();
            double z = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * 5.0D;

            this.getNavigation().moveTo(x, y, z, 0.5D);

            double pX = partner.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * 5.0D;
            double pY = partner.getY();
            double pZ = partner.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * 5.0D;
            partner.getNavigation().moveTo(pX, pY, pZ, 0.5D);

        }
    }



    /** ----------Player interaction---------- **/

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(ModItems.LOLLIPOP)) {
            if (this.isLaughing()) {
                return InteractionResult.PASS;
            }

            if (this.isInLove()) {
                return super.mobInteract(player, hand);
            }

            if (!this.level().isClientSide()) {
                if (!player.getAbilities().instabuild) {
                    itemstack.consume(1, player);
                }

                this.heal(1.0F);
                this.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
                this.playSound(CustomSounds.SHORT_CRAZY_LAUGHTER, 1.0F, 1.0F);
                this.startLaughing();


                if(!isBreedingOnCooldown()){
                    this.setInLove(player);
                }

                if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL,
                            this.getX(), this.getY() + 1.2, this.getZ(),
                            10, 0.2, 0.2, 0.2, 0.05
                    );
                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.CHERRY_LEAVES,
                            this.getX(), this.getY() + 1.2, this.getZ(),
                            10, 0.2, 0.2, 0.2, 0.05
                    );
                }
            } else {
                player.swing(hand);
            }

            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }
}

