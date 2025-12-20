package net.rywir.ravenousmaw.content.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class RavenousCloneEvent {
    @SubscribeEvent
    public static void onCloneEvent(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player dead = event.getOriginal();
        Player reborn = event.getEntity();

        CompoundTag inheritance = dead.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

        if (inheritance.contains("SavedMaw")) {
            ItemStack restoredMaw = ItemStack.parseOptional(
                reborn.registryAccess(),
                inheritance.getCompound("SavedMaw")
            );

            if (!restoredMaw.isEmpty()) {
                reborn.getInventory().add(restoredMaw);
            }

            inheritance.remove("SavedMaw");
        }
    }
}
