package net.rywir.ravenousmaw.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.rywir.ravenousmaw.system.ability.*;
import net.rywir.ravenousmaw.system.interfaces.IMutationAbility;

import java.util.*;

public enum Mutations {
    TECTONIC_BITE(Component.translatable("mutation.ravenousmaw.tectonic_bite"), Stages.LATENT, Items.STICKY_PISTON, Set.of(), new TectonicBite()),
    COMBUSTIVE_ENZYME(Component.translatable("mutation.ravenousmaw.combustive_enzyme"), Stages.ADVANCED, Items.MAGMA_CREAM, Set.of(), new CombustiveEnzyme()),
    INSATIABLE_VORACITY(Component.translatable("mutation.ravenousmaw.insatiable_voracity"), Stages.ADVANCED, Items.ENCHANTED_GOLDEN_APPLE, Set.of(), new InsatiableVoracity()),
    UNDYING_FLESH(Component.translatable("mutation.ravenousmaw.undying_flesh"), Stages.NOBLE, Items.TOTEM_OF_UNDYING, Set.of(AbilityTypes.ON_CRAFT), new UndyingFlesh()),
    SYMBIOTIC_AID(Component.translatable("mutation.ravenousmaw.symbiotic_aid"), Stages.NOBLE, Items.PUFFERFISH, Set.of(), new SymbioticAid()),
    TENDRIL_LASH(Component.translatable("mutation.ravenousmaw.tendril_lash"), Stages.NOBLE, Items.ANGLER_POTTERY_SHERD, Set.of(AbilityTypes.ON_CRAFT), new TendrilLash()),
    RESONANT_RENDING(Component.translatable("mutation.ravenousmaw.resonant_rending"), Stages.EXCELSIOR, Items.GHAST_TEAR, Set.of(), new ResonantRending()),
    ARCANE_HYPERTROPHY(Component.translatable("mutation.ravenousmaw.arcane_hypertrophy"), Stages.EXCELSIOR, Items.TORCHFLOWER_SEEDS, Set.of(), new ArcaneHypertrophy()),
    INDOMITABLE_WILL(Component.translatable("mutation.ravenousmaw.indomitable_will"), Stages.EXCELSIOR, Items.DRAGON_BREATH, Set.of(), new IndomitableWill()),
    ADAPTIVE_SHIFT(Component.translatable("mutation.ravenousmaw.adaptive_shift"), Stages.EXCELSIOR, Items.NETHER_STAR, Set.of(AbilityTypes.ON_CRAFT), new AdaptiveShift());

    private final Component displayName;
    private final String key;
    private final Stages stage;
    private final Item mutagen;
    private final Set<AbilityTypes> types;
    private final IMutationAbility ability;

    private static final Map<String, Mutations> BY_KEY = new HashMap<>();
    private static final Map<Item, Mutations> BY_MUTAGEN = new HashMap<>();

    private static final Set<Mutations> ON_CRAFT_TYPE_MEMBERS = new HashSet<>();

    Mutations(Component displayName, Stages stage, Item mutagen, Set<AbilityTypes> types, IMutationAbility ability) {
        this.displayName = displayName;
        this.key = this.name().toLowerCase(Locale.ROOT);
        this.stage = stage;
        this.mutagen = mutagen;
        this.types = types;
        this.ability = ability;
    }

    public String getDisplayName() {
        return displayName.getString();
    }

    public String getKey() {
        return key;
    }

    public Stages getStage() {
        return stage;
    }

    public Item getMutagen() {
        return mutagen;
    }

    public IMutationAbility getAbility() {
        return ability;
    }

    public static Set<String> getDisplayNames() {
        Set<String> pseudo = new HashSet<>();

        for (var val : values()) {
            pseudo.add(val.getDisplayName());
        }

        return pseudo;
    }

    public static Set<Mutations> getOnCraftTypeMembers() {
        return ON_CRAFT_TYPE_MEMBERS;
    }

    public static Mutations byMutagen(Item mutagen) {
        return BY_MUTAGEN.get(mutagen);
    }

    public static Mutations byKey(String key) {
        return BY_KEY.get(key);
    }

    static {
        for (Mutations mutation : values()) {
            if (mutation.types.contains(AbilityTypes.ON_CRAFT)) {
                ON_CRAFT_TYPE_MEMBERS.add(mutation);
            }
        }
    }

    static {
        for (Mutations mutation : values()) {
            BY_KEY.put(mutation.getKey(), mutation);
        }
    }

    static {
        for (Mutations mutation : values()) {
            BY_MUTAGEN.put(mutation.mutagen, mutation);
        }
    }

    public enum AbilityTypes {
        ON_CRAFT
    }

    public enum Parameters {
        LOOT,
        SILK_TOUCH;

        private final String key;

        Parameters() {
            this.key = this.name().toLowerCase(Locale.ROOT);
        }

        public String getKey() {
            return key;
        }
    }
}
