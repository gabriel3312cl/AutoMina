// Contenido idéntico al que ya tiene, no necesita cambios.
// Solo asegúrese de que implementa EntityBlock.
package cl.condorzero.automina.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NucleoBlock extends Block implements EntityBlock {
    public static final MapCodec<NucleoBlock> CODEC = simpleCodec(NucleoBlock::new);

    public NucleoBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NucleoBlockEntity(pos, state);
    }
}