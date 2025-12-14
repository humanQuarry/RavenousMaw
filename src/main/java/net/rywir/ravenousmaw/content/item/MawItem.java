package net.rywir.ravenousmaw.content.item;

import com.google.common.collect.BoundType;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.rywir.ravenousmaw.RavenousMaw;
import net.rywir.ravenousmaw.content.component.MutationComponent;
import net.rywir.ravenousmaw.content.gui.menu.ConfigurationMenu;
import net.rywir.ravenousmaw.datagen.provider.RavenousItemTagsProvider;
import net.rywir.ravenousmaw.registry.DataComponentTypes;
import net.rywir.ravenousmaw.registry.Mutations;
import net.rywir.ravenousmaw.registry.RavenousItems;
import net.rywir.ravenousmaw.registry.Stages;
import net.rywir.ravenousmaw.datagen.provider.RavenousBlockTagsProvider;
import net.rywir.ravenousmaw.system.AbilityDispatcher;
import net.rywir.ravenousmaw.system.MutationHandler;
import net.rywir.ravenousmaw.system.StageHandler;
import net.rywir.ravenousmaw.util.Constants;
import net.rywir.ravenousmaw.util.HelperData;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class MawItem extends Item {
    public final Stages stage;
    private static final Component DURABILITY_WARNING = Component.translatable("ravenousmaw.maw_threshold_message");

    public MawItem(Stages stage) {
        super(createProperties(stage));
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "ravenous_maw_" + this.stage.name();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Tool tool = (Tool)stack.get(DataComponents.TOOL);

        float divider = 1;

        MutationHandler mutationHandler = new MutationHandler(stack);
        boolean hasTectonicBite = mutationHandler.has(Mutations.TECTONIC_BITE);

        // Tectonic Bite instability
        if (hasTectonicBite) {
            int range = mutationHandler.getConfigVal(Mutations.TECTONIC_BITE);

            StageHandler stageHandler = new StageHandler(stack);
            Stages stage = stageHandler.getStage();

            boolean isExcelsior = stage == Stages.EXCELSIOR;

            if (!isExcelsior && range != 1) {
                divider = range / 2;
            }
        }

        return tool.getMiningSpeed(state) / divider;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        StageHandler stageHandler = new StageHandler(stack);
        String stageString = Component.translatable("maw.ravenousmaw.stage_string").getString();
        tooltipComponents.add(Component.literal(stageString + ": " + stageHandler.getStage().getDisplayName()));

        MutationHandler handler = new MutationHandler(stack);
        List<String> displayNames = handler.getDisplayNames();

        if (Screen.hasShiftDown()) {
            displayNames.forEach(name -> tooltipComponents.add(Component.literal("・" + name).withStyle(ChatFormatting.GRAY))
            );
        } else {
            String mutationString = Component.translatable("maw.ravenousmaw.mutation_string").getString();
            tooltipComponents.add(Component.literal(mutationString + ": " + displayNames.size()).withStyle(ChatFormatting.YELLOW));
            tooltipComponents.add(Component.translatable("tooltip.ravenousmaw.shift").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        AbilityDispatcher dispatcher = new AbilityDispatcher();
        dispatcher.onAttack(stack, target, target.level());
        return true;
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        Level level;

        try {
            level = target.level();
        } catch (Exception e) {
            return 1.0F;
        }

        if (level.isClientSide()) {
            return 0F;
        }

        ItemStack stack = damageSource.getWeaponItem();

        StageHandler handler = new StageHandler(stack);
        Stages stage = handler.getStage();

        MutationHandler mutationHandler = new MutationHandler(stack);

        AbilityDispatcher dispatcher = new AbilityDispatcher();
        float bonusDamage = dispatcher.getAttackDamageBonus(mutationHandler, stack, (LivingEntity) target, level, stage);

        return bonusDamage;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        boolean isClientSide = level.isClientSide();

        if (isClientSide) {
            return InteractionResultHolder.fail(stack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.fail(stack);
        }

        boolean isShiftDown = player.isShiftKeyDown();

        if (!isShiftDown) {
            return InteractionResultHolder.fail(stack);
        }

        callMenu(serverPlayer, player, stack);

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return canSurviveUsage(player.getItemInHand(InteractionHand.MAIN_HAND), level, player, () -> super.canAttackBlock(state, level, pos, player), false);
    }

    private int tickCounter = 0;
    private final int MAX_COUNT = 20040; // 17 min.
    private final int DIALOGUE_FREQUENCY = 9600;

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        tickCounter = tickCounter + 1;

        if (tickCounter % DIALOGUE_FREQUENCY == 0) {
            StageHandler handler = new StageHandler(stack);
            Stages stage = handler.getStage();

            String dialogue = generateDialogue(stage);
            player.displayClientMessage(Component.literal(dialogue), true);
        }

        if (tickCounter >= MAX_COUNT) {
            StageHandler stageHandler = new StageHandler(stack);
            Stages stage = stageHandler.getStage();

            if (stage == Stages.EXCELSIOR) {
                return;
            }

            boolean isMaw = player.getMainHandItem().is(RavenousItemTagsProvider.MAW);

            if (!isMaw) {
                return;
            }

            MutationHandler handler = new MutationHandler(stack);
            List<Mutations> muts = new ArrayList<>(handler.matchMutations());

            if (muts.isEmpty()) {
                tickCounter = 0;
                return;
            }

            Random random = new Random();
            int index = random.nextInt(muts.size());

            muts.get(index).getAbility().onInstability(handler, stack, player);
            tickCounter = 0;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (playerHasShieldUseIntent(context)) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        InteractionResult axeResult = tryAxeActions(context, level, pos, state);
        if (axeResult != InteractionResult.PASS) {
            return axeResult;
        }

        InteractionResult shovelResult = tryShovelActions(context, level, pos, state);
        if (shovelResult != InteractionResult.PASS) {
            return shovelResult;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
            || ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility)
            || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility)
            || ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }

    // todo: add more dialogues in future
    public String generateDialogue(Stages stage) {
        return switch (stage) {
            case LATENT -> Component.translatable("dialogue.ravenousmaw.latent").getString();
            case ADVANCED -> Component.translatable("dialogue.ravenousmaw.advanced").getString();
            case NOBLE -> Component.translatable("dialogue.ravenousmaw.noble").getString();
            case EXCELSIOR -> Component.translatable("dialogue.ravenousmaw.excelsior").getString();
            default -> " ";
        };
    }

    private void callMenu(ServerPlayer serverPlayer, Player player, ItemStack stack) {
        serverPlayer.openMenu(new SimpleMenuProvider(
            (id, inv, entity) -> new ConfigurationMenu(id, player.getInventory(), stack),
            Component.translatable("maw.ravenousmaw.configuration_string")
        ));
    }

    private boolean playerHasShieldUseIntent(UseOnContext context) {
        Player player = context.getPlayer();
        return context.getHand().equals(InteractionHand.MAIN_HAND)
            && player.getOffhandItem().is(Items.SHIELD)
            && !player.isSecondaryUseActive();
    }

    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initalBlockPos, ServerPlayer player) {
        range = (range - 1) / 2;

        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
            (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if(traceResult.getType() == HitResult.Type.MISS) {
            return positions;
        }

        if(traceResult.getDirection() == Direction.DOWN || traceResult.getDirection() == Direction.UP) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY(), initalBlockPos.getZ() + y));
                }
            }
        }

        if(traceResult.getDirection() == Direction.NORTH || traceResult.getDirection() == Direction.SOUTH) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY() + y, initalBlockPos.getZ()));
                }
            }
        }

        if(traceResult.getDirection() == Direction.EAST || traceResult.getDirection() == Direction.WEST) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY() + y, initalBlockPos.getZ() + x));
                }
            }
        }

        return positions;
    }

    public static Set<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initalBlockPos, Player player) {
        range = (range - 1) / 2;

        Set<BlockPos> positions = new HashSet<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
            (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if(traceResult.getType() == HitResult.Type.MISS) {
            return positions;
        }

        if(traceResult.getDirection() == Direction.DOWN || traceResult.getDirection() == Direction.UP) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY(), initalBlockPos.getZ() + y));
                }
            }
        }

        if(traceResult.getDirection() == Direction.NORTH || traceResult.getDirection() == Direction.SOUTH) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY() + y, initalBlockPos.getZ()));
                }
            }
        }

        if(traceResult.getDirection() == Direction.EAST || traceResult.getDirection() == Direction.WEST) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY() + y, initalBlockPos.getZ() + x));
                }
            }
        }

        return positions;
    }

    private InteractionResult tryAxeActions(UseOnContext context, Level level, BlockPos pos, BlockState state) {
        Player player = context.getPlayer();

        BlockState stripped = state.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false);

        if (stripped != null) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return applyBlockStateChange(context, stripped, pos);
        }

        BlockState scraped = state.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false);

        if (scraped != null) {
            level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, pos, 0);
            return applyBlockStateChange(context, scraped, pos);
        }

        BlockState unwaxed = state.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, false);

        if (unwaxed != null) {
            level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3004, pos, 0);
            return applyBlockStateChange(context, unwaxed, pos);
        }

        return InteractionResult.PASS;
    }

    private InteractionResult tryShovelActions(UseOnContext context, Level level, BlockPos pos, BlockState state) {
        Player player = context.getPlayer();

        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        }

        BlockState flattened = state.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false);

        if (flattened != null && level.getBlockState(pos.above()).isAir()) {
            level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            return applyBlockStateChange(context, flattened, pos);
        }

        BlockState doused = state.getToolModifiedState(context, ItemAbilities.SHOVEL_DOUSE, false);

        if (doused != null) {
            if (!level.isClientSide()) {
                level.levelEvent(null, 1009, pos, 0);
            }
            return applyBlockStateChange(context, doused, pos);
        }

        return InteractionResult.PASS;
    }

    private InteractionResult applyBlockStateChange(UseOnContext context, BlockState newState, BlockPos pos) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
        }

        if(level.isClientSide() || player != null) {
            return InteractionResult.PASS;
        }

        level.setBlock(pos, newState, 11);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static Item.Properties createProperties(Stages stage) {
        return new Item.Properties()
            .durability(stage.getUses())
            .attributes(createAttributes(stage))
            .component(DataComponents.TOOL, createToolComponent(stage))
            .component(DataComponentTypes.MUTATION_COMPONENT_TYPE, MutationComponent.EMPTY);
    }

    private static Tool createToolComponent(Stages stage) {
        return stage.createToolProperties(RavenousBlockTagsProvider.CORRECT_FOR_MAW);
    }

    private static ItemAttributeModifiers createAttributes(Stages stage) {
        return ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (Constants.MAW_ATTACK_DAMAGE_MODIFIER + stage.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(BASE_ATTACK_SPEED_ID, (Constants.MAW_ATTACK_SPEED_MODIFIER), AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            )
            .build();
    }

    public <T> T canSurviveUsage(ItemStack stack, Level level, @Nullable LivingEntity entity, Supplier<T> action, T failureResult) {
        int remainingDurability = stack.getMaxDamage() - stack.getDamageValue();
        int threshold = (int)(stack.getMaxDamage() * Constants.MAW_DURABILITY_PERCENTAGE_THRESHOLD);

        if (remainingDurability > threshold) {
            return action.get();
        }

        if (level.isClientSide && entity instanceof Player player) {
            level.playSound(player, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 0.5F, 0.5F);
            player.displayClientMessage(DURABILITY_WARNING, true);
        }

        return failureResult;
    }

    public Stages getStage() {
        return stage;
    }
}