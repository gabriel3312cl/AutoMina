package cl.condorzero.automina.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface TickingBlockEntity {
    void tick(Level level, BlockPos pos, BlockState state);
}