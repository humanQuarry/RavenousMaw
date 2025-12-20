package net.rywir.ravenousmaw.system.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.rywir.ravenousmaw.registry.Mutations;
import net.rywir.ravenousmaw.registry.RavenousMobEffects;
import net.rywir.ravenousmaw.system.MutationHandler;
import net.rywir.ravenousmaw.system.interfaces.IMutationAbility;

public class SymbioticAid implements IMutationAbility {
    // look into RavenousMobEffectAddedEvent for impl
    private static final int DURATION = 8 * 20;

    @Override
    public void onInstability(MutationHandler mutationHandler, ItemStack stack, Player player) {
        if (player.level().isClientSide) return;

        boolean hasSymbioticAid = mutationHandler.has(Mutations.SYMBIOTIC_AID);

        if (!hasSymbioticAid) return;

        player.addEffect(new MobEffectInstance(RavenousMobEffects.SYMBIOTIC_INFECTION, DURATION));
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, DURATION));
        player.displayClientMessage(Component.translatable("instability_message.ravenousmaw.symbiotic_aid"), true);
    }
}
