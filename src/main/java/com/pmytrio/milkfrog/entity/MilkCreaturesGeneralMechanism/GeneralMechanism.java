package com.pmytrio.milkfrog.entity.MilkCreaturesGeneralMechanism;

import com.pmytrio.milkfrog.item.ModItems;
import com.pmytrio.milkfrog.sound.CustomSounds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class GeneralMechanism extends Animal {

    /**
     * ----------Constants----------
     **/

    private static final int LAUGH_DURATION = 80;



    /**
     * ----------State Fields----------
     **/

    //----------Laughter Control Parameters----------
    // Synced so the client knows when to play the "laugh" animation.
    private static final EntityDataAccessor<Boolean> DATA_LAUGHING =
            SynchedEntityData.defineId(GeneralMechanism.class, EntityDataSerializers.BOOLEAN);
    private int laughTicks = 0;



    /**
     * ----------Constructor----------
     **/
    public GeneralMechanism(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LAUGHING, false);
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
        return ModItems.isLollipop(stack);
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


    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    /** ----------Player Interaction---------- **/
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (ModItems.isLollipop(heldStack)) {
            if (this.isLaughing()) {
                return InteractionResult.PASS;
            }

            if (this.isInLove()) {
                return super.mobInteract(player, hand);
            }

            if (!this.level().isClientSide()) {
                Item fedItem = heldStack.getItem();
                ItemStack fedStack = heldStack.copy();

                if (!player.getAbilities().instabuild) {
                    heldStack.consume(1, player);
                }

                this.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
                LollipopFeeding.applyConsumeEffects(this, fedStack);

                boolean cheerfulReaction = LollipopFeeding.causesLaughter(fedItem);
                if (cheerfulReaction) {
                    this.heal(1.0F);
                    this.playSound(CustomSounds.SHORT_CRAZY_LAUGHTER, 1.0F, 1.0F);
                    this.startLaughing();
                }

                if (LollipopFeeding.allowsBreeding(fedItem) && !this.isBreedingOnCooldown()) {
                    this.setInLove(player);
                }

                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            new ItemParticleOption(ParticleTypes.ITEM, fedItem),
                            this.getX(), this.getY() + 1.2, this.getZ(),
                            12, 0.2, 0.2, 0.2, 0.05
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
