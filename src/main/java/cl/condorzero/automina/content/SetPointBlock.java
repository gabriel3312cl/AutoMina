package cl.condorzero.automina.content;

import cl.condorzero.automina.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

public class SetPointBlock extends Block implements EntityBlock {

    public SetPointBlock(Properties p){ super(p); }

    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState st){
        return new SetPointBE(pos, st);
    }

    /* ---------- BE ---------- */
    public static class SetPointBE extends BlockEntity {
        private String linkId = "";

        public SetPointBE(BlockPos pos, BlockState st){
            super(ModBlocks.SET_POINT_BE.get(), pos, st);
        }

        @Override
        protected void saveAdditional(CompoundTag tag, HolderLookup.Provider prov){
            super.saveAdditional(tag, prov);
            tag.putString("LinkId", linkId);
        }
        @Override
        public void loadAdditional(CompoundTag tag, HolderLookup.Provider prov){
            super.loadAdditional(tag, prov);
            linkId = tag.getString("LinkId").orElse("");   // ← Optional
        }

        public String getId(){ return linkId; }

        public void setId(String id){ linkId=id; setChanged(); }
    }

    public InteractionResult use(BlockState st, Level lvl, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        ItemStack held = player.getItemInHand(hand);
        if(!lvl.isClientSide && held.is(Items.NAME_TAG)
                && !held.getHoverName().getString().isEmpty()){
            String id = held.getHoverName().getString();
            ((SetPointBE)lvl.getBlockEntity(pos)).setId(id);
            player.displayClientMessage(
                    Component.literal("Set-Point vinculado a ["+id+"]"), true);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override public RenderShape getRenderShape(BlockState st){ return RenderShape.MODEL; }
}
