package net.rywir.ravenousmaw.registry;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rywir.ravenousmaw.RavenousMaw;
import net.rywir.ravenousmaw.content.item.CodexItem;
import net.rywir.ravenousmaw.content.item.FeastItem;
import net.rywir.ravenousmaw.content.item.MawItem;
import net.rywir.ravenousmaw.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class RavenousItems {
    public static final DeferredRegister.Items FEASTS = DeferredRegister.createItems(RavenousMaw.MOD_ID);
    public static final DeferredRegister.Items MAWS = DeferredRegister.createItems(RavenousMaw.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RavenousMaw.MOD_ID);


    // FEASTS
    public static final DeferredItem<Item> PIGLIN_PIE = FEASTS.register("piglin_pie",
        () -> new FeastItem(MobEffects.POISON, Constants.BASE_NUTRITION_MODIFIER, Constants.BASE_SATURATION_MODIFIER));

    public static final DeferredItem<Item> CHORUS_CRACKER = FEASTS.register("chorus_cracker",
        () -> new FeastItem(MobEffects.LEVITATION, Constants.BASE_NUTRITION_MODIFIER * 2, Constants.BASE_SATURATION_MODIFIER * 2));

    public static final DeferredItem<Item> SCULK_CROISSANT = FEASTS.register("sculk_croissant",
        () -> new FeastItem(MobEffects.DARKNESS, Constants.BASE_NUTRITION_MODIFIER * 4, Constants.BASE_SATURATION_MODIFIER * 4));


    // MAWS
    public static final Map<Stages, Item> MAW_BY_STAGE = new HashMap<>();

    public static final DeferredItem<Item> RAVENOUS_MAW_LATENT = MAWS.register("ravenous_maw_latent",
        () -> new MawItem(Stages.LATENT));

    public static final DeferredItem<Item> RAVENOUS_MAW_ADVANCED = MAWS.register("ravenous_maw_advanced",
        () -> new MawItem(Stages.ADVANCED));

    public static final DeferredItem<Item> RAVENOUS_MAW_NOBLE = MAWS.register("ravenous_maw_noble",
        () -> new MawItem(Stages.NOBLE));

    public static final DeferredItem<Item> RAVENOUS_MAW_EXCELSIOR = MAWS.register("ravenous_maw_excelsior",
        () -> new MawItem(Stages.EXCELSIOR));


    // OTHER ITEMS
    public static final DeferredItem<Item> CONTAMINATION_CORE = ITEMS.register("contamination_core",
        () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MUTATION_CODEX = ITEMS.register("mutation_codex",
        () -> new CodexItem(new Item.Properties()));


    public static void registerFeasts(IEventBus bus) {
        FEASTS.register(bus);
    }

    public static void registerMaws(IEventBus bus) {
        MAWS.register(bus);
    }

    public static void registerItems(IEventBus bus) {
        ITEMS.register(bus);
    }
}
