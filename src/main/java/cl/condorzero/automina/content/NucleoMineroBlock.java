package cl.condorzero.automina.content;

import cl.condorzero.automina.registry.ModBlocks;
import cl.condorzero.automina.util.BlockFinder;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NucleoMineroBlock extends BaseEntityBlock {

    public static final MapCodec<NucleoMineroBlock> CODEC = simpleCodec(NucleoMineroBlock::new);
    public static final BooleanProperty ACTIVE = BlockStateProperties.LIT;

    public NucleoMineroBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    // --- MÉTODOS DE INTERACCIÓN CORREGIDOS ---

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof NucleoMineroBlockEntity core) {
            // Chequeo específico para la Etiqueta de Nombre
            if (pPlayer.isShiftKeyDown() && pStack.is(Items.NAME_TAG) && pStack.has(DataComponents.CUSTOM_NAME)) {
                if (!pLevel.isClientSide) {
                    String id = pStack.getHoverName().getString();
                    core.setLinkId(id);
                    pPlayer.displayClientMessage(Component.literal("Núcleo Minero vinculado a [" + id + "]"), true);
                }
                // CORRECCIÓN: Devolvemos CONSUME para indicar que la acción se completó.
                return InteractionResult.CONSUME;
            }
        }
        // Si no es una etiqueta, llamamos a la interacción sin ítem.
        return useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof NucleoMineroBlockEntity core) {
            if (!pLevel.isClientSide) {
                // Shift-Click con mano vacía para escanear
                if (pPlayer.isShiftKeyDown()) {
                    defineArea(pLevel, pPos, core, pPlayer);
                } else {
                    // Clic normal para encender/apagar
                    core.toggle();
                    pPlayer.displayClientMessage(Component.translatable(core.isActive() ? "message.automina.core_on" : "message.automina.core_off"), true);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void defineArea(Level level, BlockPos pos, NucleoMineroBlockEntity core, Player player) {
        String id = core.getLinkId();
        if (id.isEmpty()) {
            player.displayClientMessage(Component.literal("¡El Núcleo Minero no tiene un ID vinculado! Usa una Etiqueta."), false);
            return;
        }
        List<SetPointBlock.SetPointBE> setPoints = BlockFinder.findSetPoints(level, id, pos, 64);
        if (setPoints.size() != 4) {
            player.displayClientMessage(Component.literal("Se encontraron " + setPoints.size() + " SetPoints con el ID [" + id + "]. Se necesitan 4."), false);
            return;
        }
        player.displayClientMessage(Component.literal("¡Área definida! Se encontraron 4 SetPoints para el ID [" + id + "]."), true);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NucleoMineroBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlocks.NUCLEO_MINERO_BE.get(), (lvl, p, st, be) -> be.serverTick());
    }

    public static class NucleoMineroBlockEntity extends BlockEntity implements MenuProvider {
        private boolean active = false;
        private String linkId = "";
        public NucleoMineroBlockEntity(BlockPos pos, BlockState state) { super(ModBlocks.NUCLEO_MINERO_BE.get(), pos, state); }
        public void serverTick() { if (!active) return; }
        public void toggle() { active = !active; if (level != null) { level.setBlock(worldPosition, getBlockState().setValue(ACTIVE, active), 3); } setChanged(); }
        public boolean isActive() { return active; }
        public String getLinkId() { return this.linkId; }
        public void setLinkId(String id) { this.linkId = id; setChanged(); }
        @Override
        protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
            super.saveAdditional(tag, provider);
            tag.putBoolean("Active", active);
            tag.putString("LinkId", linkId);
        }
        @Override
        public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
            super.loadAdditional(tag, provider);
            // CORRECCIÓN DEFINITIVA: El método es .getBoolean() y se usa .orElse() sobre el resultado.
            this.active = tag.getBoolean("Active").orElse(false);
            this.linkId = tag.getString("LinkId").orElse("");
        }
        @Override public Component getDisplayName() { return Component.literal("Mining Core"); }
        @Nullable @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) { return null; }
    }
}