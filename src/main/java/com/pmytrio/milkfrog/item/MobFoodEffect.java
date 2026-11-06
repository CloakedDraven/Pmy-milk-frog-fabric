package com.pmytrio.milkfrog.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class MobFoodEffect {

    public static final Consumable ICE_LOLLIPOP_CONSUMABLE_COMPONENT = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOWNESS, 6 * 20, 2), 1.0f))
            .build();

    public static final FoodProperties ICE_LOLLIPOP_COMPONENT = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.2f)
            .alwaysEdible()
            .build();
}
