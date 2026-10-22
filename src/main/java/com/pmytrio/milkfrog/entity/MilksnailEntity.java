package com.pmytrio.milkfrog.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.pmytrio.milkfrog.sound.CustomSounds;
import com.pmytrio.milkfrog.item.ModItems;
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

public class MilksnailEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int laughTicks = 0;
    private static final int LAUGH_DURATION = 80;
    private boolean isLaughing = false;


    public MilksnailEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isBreedingOnCooldown() {
        return this.getAge() > 0;
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

        // 添加繁殖 AI，优先级放在 TemptGoal 之前（数值越小优先级越高）
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.5D));

        this.goalSelector.addGoal(3, new TemptGoal(this, 0.5, Ingredient.of(ModItems.LOLLIPOP), false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 4));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
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

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.LOLLIPOP);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        // Generate a chick temporarily.
        return net.minecraft.world.entity.EntityType.CHICKEN.create(level, net.minecraft.world.entity.EntitySpawnReason.BREEDING);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
        super.spawnChildFromBreeding(level, partner);

        this.stopLaughing();
        if (partner instanceof MilksnailEntity milksnailPartner) {
            milksnailPartner.stopLaughing();
        }

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

}

