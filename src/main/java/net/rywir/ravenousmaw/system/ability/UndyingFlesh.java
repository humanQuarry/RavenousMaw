package net.rywir.ravenousmaw.system.ability;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;
import net.rywir.ravenousmaw.registry.Mutations;
import net.rywir.ravenousmaw.system.EnchantmentHandler;
import net.rywir.ravenousmaw.system.MutationHandler;
import net.rywir.ravenousmaw.system.interfaces.IMutationAbility;

public class UndyingFlesh implements IMutationAbility {
    @Override
    public void onCraft(ItemStack stack) {
        onUpdate(stack, null);
    }

    @Override
    public void onUpdate(ItemStack stack, Level level) {
        MutationHandler handler = new MutationHandler(stack);

        if (!handler.has(Mutations.UNDYING_FLESH)) {
            return;
        }

        stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
    }

    @Override
    public void decraft(ItemStack stack) {
        stack.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
    }

    @Override
    public void onInstability(MutationHandler mutationHandler, ItemStack stack, Player player) {
        boolean hasUndyingFlesh = mutationHandler.has(Mutations.UNDYING_FLESH);

        if (!hasUndyingFlesh) {
            return;
        }

        int durationInSeconds = 8;

        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, durationInSeconds * 20, 5));
        player.displayClientMessage(Component.translatable("instability_message.ravenousmaw.undying_flesh"), true);
    }
}