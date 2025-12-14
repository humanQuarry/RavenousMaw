package net.rywir.ravenousmaw.content.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.rywir.ravenousmaw.content.command.DismutateCommand;
import net.rywir.ravenousmaw.content.command.MutateCommand;
import net.rywir.ravenousmaw.content.command.UpCommand;

public class RavenousRegisterCommandsEvent {
    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        MutateCommand.register(event.getDispatcher());
        UpCommand.register(event.getDispatcher());
        DismutateCommand.register(event.getDispatcher());
    }
}
