package net.rywir.ravenousmaw.system;

import com.google.common.collect.ImmutableMap;
import com.ibm.icu.impl.CollectionSet;
import net.minecraft.world.item.ItemStack;
import net.rywir.ravenousmaw.RavenousMaw;
import net.rywir.ravenousmaw.content.component.AdaptiveShiftComponent;
import net.rywir.ravenousmaw.content.component.MutationComponent;
import net.rywir.ravenousmaw.registry.DataComponentTypes;
import net.rywir.ravenousmaw.registry.Mutations;

import java.util.*;

public class MutationHandler {
    private final ItemStack stack;
    private final ImmutableMap<String, Integer> immutableMap;
    private static final int DEFAULT_VALUE = 1;

    public MutationHandler(ItemStack stack) {
        this.stack = stack;
        this.immutableMap = stack.get(DataComponentTypes.MUTATION_COMPONENT_TYPE).mutations();
    }

    public static AdaptiveShiftComponent generate() {
        Map<String, Boolean> parameters = new HashMap<>();

        parameters.put(Mutations.Parameters.LOOT.getKey(), true);
        parameters.put(Mutations.Parameters.SILK_TOUCH.getKey(), false);

        ImmutableMap<String, Boolean> pseudo = ImmutableMap.copyOf(parameters);
        AdaptiveShiftComponent component = new AdaptiveShiftComponent(pseudo);

        return component;
    }

    public void syncMutations(ItemStack target) {
        MutationComponent component = stack.get(DataComponentTypes.MUTATION_COMPONENT_TYPE);
        target.set(DataComponentTypes.MUTATION_COMPONENT_TYPE, new MutationComponent(component.mutations()));
    }

    public boolean has(Mutations mutation) {
        return immutableMap.containsKey(mutation.getKey());
    }

    public void add(Mutations mutation) {
        if(has(mutation)) {
            return;
        }

        Map<String, Integer> pseudo = new HashMap<>(immutableMap);
        pseudo.put(mutation.getKey(), DEFAULT_VALUE);
        stack.set(DataComponentTypes.MUTATION_COMPONENT_TYPE, new MutationComponent(ImmutableMap.copyOf(pseudo)));
    }

    public void addWithCraft(Mutations mutation) {
        if(has(mutation)) {
            return;
        }

        Map<String, Integer> pseudo = new HashMap<>(immutableMap);
        pseudo.put(mutation.getKey(), DEFAULT_VALUE);
        stack.set(DataComponentTypes.MUTATION_COMPONENT_TYPE, new MutationComponent(ImmutableMap.copyOf(pseudo)));

        mutation.getAbility().onCraft(stack);
    }

    public void remove(Mutations mutation) {
        if (!has(mutation)) {
            return;
        }

        Map<String, Integer> pseudo = new HashMap<>(immutableMap);
        pseudo.remove(mutation.getKey());
        stack.set(DataComponentTypes.MUTATION_COMPONENT_TYPE, new MutationComponent(ImmutableMap.copyOf(pseudo)));

        mutation.getAbility().decraft(stack);
    }

    public void configure(Mutations mutation, int value) {
        if(!has(mutation)) {
            return;
        }

        Map<String, Integer> pseudo = new HashMap<>(immutableMap);
        pseudo.put(mutation.getKey(), value);
        stack.set(DataComponentTypes.MUTATION_COMPONENT_TYPE, new MutationComponent(ImmutableMap.copyOf(pseudo)));
    }

    public int getConfigVal(Mutations mutation) {
        if (!has(mutation)) {
            return 0;
        }

        int configVal = immutableMap.get(mutation.getKey());

        return configVal;
    }

    public ItemStack getStack() {
        return stack;
    }

    public List<String> getDisplayNames() {
        List<String> displayNames = new ArrayList<>();

        for (String key : immutableMap.keySet()) {
            Mutations pseudo = Mutations.byKey(key);

            if (pseudo != null) {
                displayNames.add(pseudo.getDisplayName());
            }
        }

        return displayNames;
    }

    public Set<Mutations> matchMutations() {
        MutationComponent component = stack.get(DataComponentTypes.MUTATION_COMPONENT_TYPE);

        Set<String> mutationKeys = new HashSet<>(component.mutations().keySet());

        Set<Mutations> muts = new HashSet<>();

        mutationKeys.forEach(
            (key) -> muts.add(Mutations.byKey(key))
        );

        return muts;
    }
}
