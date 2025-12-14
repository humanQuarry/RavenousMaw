package net.rywir.ravenousmaw.system.interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.rywir.ravenousmaw.registry.Stages;
import net.rywir.ravenousmaw.system.MutationHandler;

public interface IMutationAbility {
    default void onMine() {}

    default float getAttackDamageBonus(MutationHandler handler, ItemStack stack, LivingEntity target, Level level, Stages stage) {
        return 0.0F;
    }

    default void onAttack(ItemStack stack, LivingEntity target, Level level) {}

    default void onCraft(ItemStack stack) {}

    default void onInstability(MutationHandler mutationHandler, ItemStack stack, Player player) {}

    default void onUpdate(ItemStack stack, Level level) {}

    default void decraft(ItemStack stack) {}

    default boolean isMutatable(ItemStack stack, Level level) { return true; }
}
