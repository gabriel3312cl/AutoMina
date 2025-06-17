package cl.condorzero.automina.content;

import cl.condorzero.automina.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class NucleoMineroBlock extends Block implements EntityBlock {

    public static final BooleanProperty ACTIVE = BlockStateProperties.LIT;

    public NucleoMineroBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof NucleoMineroBlockEntity core) {
                core.toggle();
                player.displayClientMessage(Component.translatable(
                        core.isActive() ? "message.automina.core_on" : "message.automina.core_off"), true);
            }
        }
        return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NucleoMineroBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return level.isClientSide ? null :
                (type == ModBlocks.NUCLEO_MINERO_BE.get()
                        ? (lvl, p, st, be) -> ((NucleoMineroBlockEntity) be).serverTick()
                        : null);
    }

    public static class NucleoMineroBlockEntity extends BlockEntity implements MenuProvider {

        private boolean active = false;

        public NucleoMineroBlockEntity(BlockPos pos, BlockState state) {
            super(ModBlocks.NUCLEO_MINERO_BE.get(), pos, state);
        }

        public void serverTick() {
            if (!active) return;
        }

        public void toggle() {
            active = !active;
            if (level != null) {
                level.setBlock(worldPosition, getBlockState().setValue(ACTIVE, active), 3);
            }
            setChanged();
        }

        public boolean isActive() {
            return active;
        }

        @Override
        protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
            super.saveAdditional(tag, provider);
            tag.putBoolean("Active", active);
        }

        @Override
        public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
            super.loadAdditional(tag, provider);
            active = tag.getBoolean("Active").orElse(false);
        }

        @Override
        public Component getDisplayName() {
            return Component.literal("Mining Core");
        }

        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
            return null;
        }
    }
}
