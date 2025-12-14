package net.rywir.ravenousmaw.system;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.ItemStack;
import net.rywir.ravenousmaw.RavenousMaw;
import net.rywir.ravenousmaw.content.component.AdaptiveShiftComponent;
import net.rywir.ravenousmaw.registry.DataComponentTypes;
import net.rywir.ravenousmaw.registry.Mutations;

import java.util.HashMap;
import java.util.Map;

public class AdaptiveShiftHandler {
    private final ItemStack stack;
    private final ImmutableMap<String, Boolean> immutableMap;

    public AdaptiveShiftHandler(ItemStack stack) {
        this.stack = stack;
        this.immutableMap = stack.get(DataComponentTypes.ADAPTIVE_SHIFT_COMPONENT_TYPE).parameters();
    }

    public boolean getConfigBool(Mutations.Parameters parameter) {
        return immutableMap.get(parameter.getKey());
    }

    public void reconfigure(Mutations.Parameters parameter, boolean value) {
        Map<String, Boolean> pseudo = new HashMap<>(immutableMap);
        pseudo.put(parameter.getKey(), value);
        ImmutableMap<String, Boolean> newMap = ImmutableMap.copyOf(pseudo);
        AdaptiveShiftComponent component = new AdaptiveShiftComponent(newMap);
        stack.set(DataComponentTypes.ADAPTIVE_SHIFT_COMPONENT_TYPE, component);
    }

    public void toggle(Mutations.Parameters parameter) {
        Map<String, Boolean> pseudo = new HashMap<>(immutableMap);
        boolean exVal = pseudo.get(parameter.getKey());
        boolean newVal = !exVal;
        pseudo.put(parameter.getKey(), newVal);
        ImmutableMap<String, Boolean> newMap = ImmutableMap.copyOf(pseudo);
        AdaptiveShiftComponent component = new AdaptiveShiftComponent(newMap);
        stack.set(DataComponentTypes.ADAPTIVE_SHIFT_COMPONENT_TYPE, component);
    }
}
