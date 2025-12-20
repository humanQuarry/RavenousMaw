package net.rywir.ravenousmaw.content.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class MawTickData {
    public static final Codec<MawTickData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("ravenous_tick_counter").forGetter(MawTickData::getTickCounter)
        ).apply(instance, MawTickData::new)
    );

    private int tickCounter;

    public MawTickData() {
        this.tickCounter = 0;
    }

    public MawTickData(int tickCounter) {
        this.tickCounter = tickCounter;
    }

    public int getTickCounter() {
        return tickCounter;
    }

    public void incrementTicks() {
        tickCounter++;
    }

    public void resetTicks() {
        tickCounter = 0;
    }
}