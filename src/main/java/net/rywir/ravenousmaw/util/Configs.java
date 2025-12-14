package net.rywir.ravenousmaw.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Configs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // REPAIR VALUES
    // LATENT
    public static final ModConfigSpec.IntValue LATENT_ROTTEN_FLESH = BUILDER
        .comment("Latent Stage - Rotten Flesh Repair Value")
        .defineInRange("latentFlesh", Constants.LATENT_ROTTEN_FLESH, 0, Integer.MAX_VALUE);

    // ADVANCED
    public static final ModConfigSpec.IntValue ADVANCED_ROTTEN_FLESH = BUILDER
        .comment("Advanced Stage - Rotten Flesh Repair Value")
        .defineInRange("advancedFlesh", Constants.ADVANCED_ROTTEN_FLESH, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue ADVANCED_PIGLIN_PIE = BUILDER
        .comment("Advanced Stage - Piglin Pie Repair Value")
        .defineInRange("advancedPie", Constants.ADVANCED_PIGLIN_PIE, 0, Integer.MAX_VALUE);

    // NOBLE
    public static final ModConfigSpec.IntValue NOBLE_ROTTEN_FLESH = BUILDER
        .comment("Noble Stage - Rotten Flesh Repair Value")
        .defineInRange("nobleFlesh", Constants.NOBLE_ROTTEN_FLESH, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue NOBLE_PIGLIN_PIE = BUILDER
        .comment("Noble Stage - Piglin Pie Repair Value")
        .defineInRange("noblePie", Constants.NOBLE_PIGLIN_PIE, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue NOBLE_CHORUS_CRACKER = BUILDER
        .comment("Noble Stage - Chorus Cracker Repair Value")
        .defineInRange("nobleCracker", Constants.NOBLE_CHORUS_CRACKER, 0, Integer.MAX_VALUE);

    // EXCELSIOR
    public static final ModConfigSpec.IntValue EXCELSIOR_ROTTEN_FLESH = BUILDER
        .comment("Excelsior Stage - Rotten Flesh Repair Value")
        .defineInRange("excelsiorFlesh", Constants.EXCELSIOR_ROTTEN_FLESH, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue EXCELSIOR_PIGLIN_PIE = BUILDER
        .comment("Excelsior Stage - Piglin Pie Repair Value")
        .defineInRange("excelsiorPie", Constants.EXCELSIOR_PIGLIN_PIE, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue EXCELSIOR_CHORUS_CRACKER = BUILDER
        .comment("Excelsior Stage - Chorus Cracker Repair Value")
        .defineInRange("excelsiorCracker", Constants.EXCELSIOR_CHORUS_CRACKER, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue EXCELSIOR_SCULK_CRONUT = BUILDER
        .comment("Excelsior Stage - Sculk Cronut Repair Value")
        .defineInRange("excelsiorCronut", Constants.EXCELSIOR_SCULK_CRONUT, 0, Integer.MAX_VALUE);


    // COMBUSTIVE ENZYME
    public static final ModConfigSpec.DoubleValue COMBUSTIVE_ENZYME_ADVANCED_DAMAGE_BONUS = BUILDER
        .comment("Advanced Stage - Combustive Enzyme Bonus Damage Value")
        .defineInRange("advancedCombustive", Constants.COMBUSTIVE_ENZYME_ADVANCED_DAMAGE_BONUS, 0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue COMBUSTIVE_ENZYME_NOBLE_DAMAGE_BONUS = BUILDER
        .comment("Noble Stage - Combustive Enzyme Bonus Damage Value")
        .defineInRange("nobleCombustive", Constants.COMBUSTIVE_ENZYME_NOBLE_DAMAGE_BONUS, 0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue COMBUSTIVE_ENZYME_EXCELSIOR_DAMAGE_BONUS = BUILDER
        .comment("Excelsior Stage - Combustive Enzyme Bonus Damage Value")
        .defineInRange("excelsiorCombustive", Constants.COMBUSTIVE_ENZYME_EXCELSIOR_DAMAGE_BONUS, 0, Double.MAX_VALUE);


    // RESONANT RENDING
    public static final ModConfigSpec.DoubleValue RESONANT_RENDING_PERCENTAGE = BUILDER
        .comment("Resonant Rending - Max Health Percentage")
        .defineInRange("resonantPercentage", Constants.RESONANT_RENDING_PERCENTAGE, 0, Double.MAX_VALUE);


    // INSATIABLE VORACITY
    public static final ModConfigSpec.DoubleValue INSATIABLE_VORACITY_ADVANCED_DAMAGE_MULTIPLIER = BUILDER
        .comment("Advanced Stage - Insatiable Voracity Damage Multiplier")
        .defineInRange("advancedVoracity", Constants.INSATIABLE_VORACITY_ADVANCED_DAMAGE_MULTIPLIER, 0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue INSATIABLE_VORACITY_NOBLE_DAMAGE_MULTIPLIER = BUILDER
        .comment("Noble Stage - Insatiable Voracity Damage Multiplier")
        .defineInRange("nobleVoracity", Constants.INSATIABLE_VORACITY_NOBLE_DAMAGE_MULTIPLIER, 0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue INSATIABLE_VORACITY_EXCELSIOR_DAMAGE_MULTIPLIER = BUILDER
        .comment("Excelsior Stage - Insatiable Voracity Damage Multiplier")
        .defineInRange("excelsiorVoracity", Constants.INSATIABLE_VORACITY_EXCELSIOR_DAMAGE_MULTIPLIER, 0, Double.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}