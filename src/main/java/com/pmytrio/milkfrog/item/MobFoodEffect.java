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



    public static final Consumable GLISTERING_LOLLIPOP_CONSUMABLE_COMPONENT = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 15 * 20, 2), 1.0f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 15 * 20, 2), 1.0f))
            .build();

    public static final FoodProperties GLISTERING_LOLLIPOP_COMPONENT = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(0.7f)
            .alwaysEdible()
            .build();

    public static final Consumable STRANGE_LOLLIPOP_CONSUMABLE_COMPONENT = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOWNESS, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HASTE, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.NAUSEA, 10 * 20, 2), 0.1f))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2), 0.1f))
            .build();

    public static final FoodProperties STRANGE_LOLLIPOP_COMPONENT = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(0.3f)
            .alwaysEdible()
            .build();
}
