package cl.condorzero.automina.content;

import cl.condorzero.automina.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SetPointBlock extends BaseEntityBlock {

    public static final MapCodec<SetPointBlock> CODEC = simpleCodec(SetPointBlock::new);

    public SetPointBlock(Properties p){ super(p); }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState st){
        return new SetPointBE(pos, st);
    }

    // Usamos el método correcto para la interacción con un ítem.
    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide && pStack.is(Items.NAME_TAG) && pStack.has(DataComponents.CUSTOM_NAME)) {
            String id = pStack.getHoverName().getString();
            if (pLevel.getBlockEntity(pPos) instanceof SetPointBE be) {
                be.setId(id);
                pPlayer.displayClientMessage(
                        Component.literal("Set-Point vinculado a [" + id + "]"), true);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override public RenderShape getRenderShape(BlockState st){ return RenderShape.MODEL; }

    public static class SetPointBE extends BlockEntity {
        private String linkId = "";
        public SetPointBE(BlockPos pos, BlockState st){ super(ModBlocks.SET_POINT_BE.get(), pos, st); }
        @Override
        protected void saveAdditional(CompoundTag tag, HolderLookup.Provider prov){ super.saveAdditional(tag, prov); tag.putString("LinkId", linkId); }
        @Override
        public void loadAdditional(CompoundTag tag, HolderLookup.Provider prov){
            super.loadAdditional(tag, prov);
            // CORRECCIÓN DEFINITIVA: El método es .getString() y se usa .orElse() sobre el resultado.
            linkId = tag.getString("LinkId").orElse("");
        }
        public String getId(){ return linkId; }
        public void setId(String id){ linkId=id; setChanged(); }
    }
}