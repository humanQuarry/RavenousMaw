package net.rywir.ravenousmaw.content.component;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import java.util.HashMap;
import java.util.Map;

public record MutationComponent(ImmutableMap<String, ConfigurationComponent> mutmap) {
    public static final Codec<MutationComponent> CODEC;
    public static final StreamCodec<ByteBuf, MutationComponent> STREAM_CODEC;

    public static MutationComponent generate() {
        Map<String, ConfigurationComponent> pseudoMutmap = new HashMap<>();
        return new MutationComponent(ImmutableMap.copyOf(pseudoMutmap));
    }

    static {
        CODEC = Codec.unboundedMap(Codec.STRING, ConfigurationComponent.CODEC)
            .xmap(
                map -> new MutationComponent(ImmutableMap.copyOf(map)),
                MutationComponent::mutmap
            );

        STREAM_CODEC = ByteBufCodecs.map(
            HashMap::new,
            ByteBufCodecs.STRING_UTF8,
            ConfigurationComponent.STREAM_CODEC
        ).map(
            map -> new MutationComponent(ImmutableMap.copyOf(map)),
            comp -> new HashMap<>(comp.mutmap)
        );
    }

    public record ConfigurationComponent(ImmutableMap<String, Integer> confmap) {
        public static final Codec<ConfigurationComponent> CODEC;
        public static final StreamCodec<ByteBuf, ConfigurationComponent> STREAM_CODEC;

        static {
            CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT)
                .xmap(
                    map -> new ConfigurationComponent(ImmutableMap.copyOf(map)),
                    ConfigurationComponent::confmap
                );

            STREAM_CODEC = ByteBufCodecs.map(
                HashMap::new,
                ByteBufCodecs.STRING_UTF8,
                ByteBufCodecs.VAR_INT
            ).map(
                map -> new ConfigurationComponent(ImmutableMap.copyOf(map)),
                comp -> new HashMap<>(comp.confmap)
            );
        }
    }
}