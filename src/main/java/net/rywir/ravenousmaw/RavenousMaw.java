package net.rywir.ravenousmaw;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.rywir.ravenousmaw.content.event.*;
import net.rywir.ravenousmaw.content.gui.menu.ConfigurationMenu;
import net.rywir.ravenousmaw.content.gui.screen.ConfigurationScreen;
import net.rywir.ravenousmaw.content.gui.screen.MutationMatrixScreen;
import net.rywir.ravenousmaw.registry.*;
import net.rywir.ravenousmaw.util.Configs;
import net.rywir.ravenousmaw.util.HelperData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(RavenousMaw.MOD_ID)
public class RavenousMaw {
    public static final String MOD_ID = "ravenousmaw";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RavenousMaw(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        // EVENTS
        NeoForge.EVENT_BUS.addListener(LivingDeathEvent.class, RavenousLivingDeathEvent::onLivingDeath);
        NeoForge.EVENT_BUS.addListener(PlayerEvent.BreakSpeed.class, RavenousBreakSpeedEvent::onBreakSpeedEvent);
        NeoForge.EVENT_BUS.addListener(MobEffectEvent.Added.class, RavenousMobEffectAddedEvent::onMobEffectAddedEvent);
        NeoForge.EVENT_BUS.addListener(LivingDropsEvent.class, RavenousLivingDropsEvent::onLivingDropsEvent);
        NeoForge.EVENT_BUS.addListener(BlockDropsEvent.class, RavenousBlockDropsEvent::onBlockDropsEvent);
        NeoForge.EVENT_BUS.addListener(BlockEvent.BreakEvent.class, RavenousBreakEvent::onBreakEvent);
        NeoForge.EVENT_BUS.addListener(MovementInputUpdateEvent.class, RavenousMovementInputUpdateEvent::onMovementInputUpdateEvent);
        NeoForge.EVENT_BUS.addListener(RenderHighlightEvent.Block.class, RavenousRenderHighlightEvent::onRenderHighlightEvent);
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, RavenousRegisterCommandsEvent::onRegisterCommandsEvent);

        modEventBus.addListener(RegisterPayloadHandlersEvent.class, RavenousRegisterPayloadHandlersEvent::onRegisterPayloadHandlersEvent);

        // MOD REGISTRATION
        DataComponentTypes.register(modEventBus);

        RavenousItems.registerFeasts(modEventBus);
        RavenousItems.registerItems(modEventBus);
        RavenousItems.registerMaws(modEventBus);

        MenuTypes.register(modEventBus);

        RavenousBlocks.registerBlocks(modEventBus);
        BlockEntityTypes.register(modEventBus);

        RavenousCreativeModeTabs.register(modEventBus);

        RavenousRecipes.register(modEventBus);

        RavenousMobEffects.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        // CONFIG REGISTRATION
        modContainer.registerConfig(ModConfig.Type.COMMON, Configs.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {}

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(MenuTypes.MUTATION_MATRIX_MENU.get(), MutationMatrixScreen::new);
            event.register(MenuTypes.CONFIGURATION_MENU.get(), ConfigurationScreen::new);
        }
    }
}