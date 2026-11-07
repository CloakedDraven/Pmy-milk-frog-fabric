package com.pmytrio.milkfrog.entity.MilkCreaturesGeneralMechanism;

import com.pmytrio.milkfrog.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;

import java.util.Set;

/**
 * Feeding rules shared by every lollipop.
 *
 * <p>All lollipops are food for milk creatures, but the ones listed in
 * {@link #NON_LAUGHING_LOLLIPOPS} are eaten without the laugh reaction and without starting
 * breeding. Every lollipop replays its player-facing consume effects on the creature, so the
 * creature receives the same status effects a player would get from eating it.</p>
 */
public final class LollipopFeeding {

    /** Lollipops that are eaten without laughter and without putting the creature in love. */
    private static final Set<Item> NON_LAUGHING_LOLLIPOPS = Set.of(
            ModItems.ICE_LOLLIPOP,
            ModItems.POISONOUS_LOLLIPOP,
            ModItems.STRANGE_LOLLIPOP
    );

    private LollipopFeeding() {
    }

    /** Whether eating this lollipop should play the laugh reaction. */
    public static boolean causesLaughter(Item fedItem) {
        return !NON_LAUGHING_LOLLIPOPS.contains(fedItem);
    }

    /** Whether eating this lollipop should put the creature in love. */
    public static boolean allowsBreeding(Item fedItem) {
        return !NON_LAUGHING_LOLLIPOPS.contains(fedItem);
    }

    /**
     * Applies the item's player-facing consume effects to the creature. Must run on the server
     * side, and the caller should pass a copy because the held stack may already be consumed.
     */
    public static void applyConsumeEffects(GeneralMechanism creature, ItemStack fedStack) {
        Consumable consumable = fedStack.get(DataComponents.CONSUMABLE);
        if (consumable == null) {
            return;
        }
        consumable.onConsume(creature.level(), creature, fedStack.copy());
    }
}
