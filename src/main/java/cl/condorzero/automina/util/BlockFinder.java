package cl.condorzero.automina.util;

import cl.condorzero.automina.content.SetPointBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

/** BFS / búsqueda simple alrededor del núcleo */
public class BlockFinder {

    public static List<SetPointBlock.SetPointBE> findSetPoints(
            Level lvl, String id, BlockPos center, int radius) {

        List<SetPointBlock.SetPointBE> res = new ObjectArrayList<>();
        BlockPos.betweenClosedStream(center.offset(-radius, -radius, -radius),
                        center.offset( radius,  radius,  radius))
                .forEach(p -> {
                    if (lvl.getBlockEntity(p) instanceof SetPointBlock.SetPointBE be
                            && be.getId().equals(id)) res.add(be);
                });
        return res;
    }
}
