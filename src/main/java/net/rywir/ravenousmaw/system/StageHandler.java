package net.rywir.ravenousmaw.system;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.rywir.ravenousmaw.registry.Mutations;
import net.rywir.ravenousmaw.registry.RavenousItems;
import net.rywir.ravenousmaw.registry.Stages;
import net.rywir.ravenousmaw.util.HelperData;

public class StageHandler {
    private final ItemStack stack;

    public StageHandler(ItemStack stack) {
        this.stack = stack;
    }

    public Stages getStage() {
        if (this.stack.is(RavenousItems.RAVENOUS_MAW_LATENT)) {
            return Stages.LATENT;
        }

        if (this.stack.is(RavenousItems.RAVENOUS_MAW_ADVANCED)) {
            return Stages.ADVANCED;
        }

        if (this.stack.is(RavenousItems.RAVENOUS_MAW_NOBLE)) {
            return Stages.NOBLE;
        }

        if (this.stack.is(RavenousItems.RAVENOUS_MAW_EXCELSIOR)) {
            return Stages.EXCELSIOR;
        }

        return null;
    }

    public ItemStack stageUp(int currentStage, ItemStack source) {
        if (currentStage >= 3) {
            return source;
        }

        Stages newStage = Stages.byId(currentStage + 1);
        ItemStack newMaw = HelperData.getMawItem(newStage).getDefaultInstance();

        MutationHandler handler = new MutationHandler(source);

        handler.syncMutations(newMaw);
        transferCustomItemData(newMaw);

        for (Mutations mutation : Mutations.values()) {
            mutation.ability().onUpdate(newMaw, null);
        }

        return newMaw;
    }

    public void transferCustomItemData(ItemStack target) {
        if (target.isEmpty()) {
            return;
        }

        Component name = stack.get(DataComponents.CUSTOM_NAME);

        if (name != null) {
            target.set(DataComponents.CUSTOM_NAME, name);
        }

        ItemLore lore = stack.get(DataComponents.LORE);

        if (lore != null) {
            target.set(DataComponents.LORE, lore);
        }

        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);

        if (enchantments != null) {
            target.set(DataComponents.ENCHANTMENTS, enchantments);
        }

        ItemEnchantments stored = stack.get(DataComponents.STORED_ENCHANTMENTS);

        if (enchantments != null) {
            target.set(DataComponents.STORED_ENCHANTMENTS, stored);
        }

        int repairCost = stack.getOrDefault(DataComponents.REPAIR_COST, 0);

        if (repairCost > 0) {
            target.set(DataComponents.REPAIR_COST, repairCost);
        }

        int damageVal = stack.getDamageValue();

        target.setDamageValue(damageVal);
    }
}
