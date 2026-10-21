package com.pmytrio.milkfrog.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.pmytrio.milkfrog.CustomSounds;
import com.pmytrio.milkfrog.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class MilksnailEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int laughTicks = 0;
    private static final int LAUGH_DURATION = 80;
    private boolean isLaughing = false;


    public MilksnailEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(state -> {
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("walk"));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    public static AttributeSupplier.Builder createMilksnailAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 0.5, Ingredient.of(ModItems.LOLLIPOP), false));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 4));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.isLaughing) {
            this.getNavigation().stop();

            if (--this.laughTicks <= 0) {
                this.stopLaughing();
            }
        }
    }


    public void startLaughing() {
        this.isLaughing = true;
        this.laughTicks = LAUGH_DURATION;


        this.getNavigation().stop();
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    public void stopLaughing() {
        this.isLaughing = false;
        this.laughTicks = 0;
    }

    public boolean isLaughing() {
        return this.isLaughing;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);


        if (itemstack.is(ModItems.LOLLIPOP) && this.isLaughing()) {

            return InteractionResult.PASS;
        }

        if (itemstack.is(ModItems.LOLLIPOP)) {
            if (!this.level().isClientSide()) {
                if (!player.getAbilities().instabuild) {
                    itemstack.consume(1, player);
                }

                this.heal(1.0F);
                this.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
                this.playSound(CustomSounds.SHORT_CRAZY_LAUGHTER, 1.0F, 1.0F);
                this.startLaughing();

                if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {

                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL,
                            this.getX(), this.getY() + 1.2, this.getZ(),
                            10,
                            0.2, 0.2, 0.2,
                            0.05
                    );

                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.CHERRY_LEAVES,
                            this.getX(), this.getY() + 1.2, this.getZ(),
                            10,
                            0.2, 0.2, 0.2,
                            0.05
                    );
                }

            }
            else {
                player.swing(hand);
            }


            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }



        return super.mobInteract(player, hand);
    }
    }