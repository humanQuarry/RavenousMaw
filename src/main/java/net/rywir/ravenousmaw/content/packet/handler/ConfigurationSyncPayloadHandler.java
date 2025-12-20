package net.rywir.ravenousmaw.content.packet.handler;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rywir.ravenousmaw.content.gui.screen.ConfigurationScreen;
import net.rywir.ravenousmaw.content.packet.payload.ConfigurationSyncPayload;

public class ConfigurationSyncPayloadHandler {
    public static void handle(final ConfigurationSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();

            if (mc.screen instanceof ConfigurationScreen screen) {
                screen.update(payload.key(), payload.val());
            }
        });
    }
}
