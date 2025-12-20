package net.rywir.ravenousmaw.content.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.rywir.ravenousmaw.datagen.provider.RavenousItemTagsProvider;
import net.rywir.ravenousmaw.registry.Mutations;

public class RavenousLivingDamageEvent {
    @SubscribeEvent
    public static void onLivingDamageEvent(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        ItemStack stack = event.getSource().getWeaponItem();

        if (stack == null) return;

        boolean isMaw = stack.is(RavenousItemTagsProvider.MAW);

        if (!isMaw) return;

        float damage = event.getNewDamage();

        Mutations.PERPETUAL_MOTION.ability().stealLife(stack, damage, player);
    }
}