package cl.condorzero.automina.registry;

import cl.condorzero.automina.AutoMina;
import cl.condorzero.automina.content.NucleoMineroBlock;
import cl.condorzero.automina.content.SetPointBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, AutoMina.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AutoMina.MODID);

    // --- Bloques ---
    // CORRECCIÓN: Usamos DeferredHolder en lugar de DeferredBlock.
    public static final DeferredHolder<Block, NucleoMineroBlock> NUCLEO_MINERO =
            BLOCKS.register("nucleo_minero",
                    rl -> new NucleoMineroBlock(BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, rl))
                            .strength(4.0f)
                            .noLootTable()));

    public static final DeferredHolder<Block, SetPointBlock> SET_POINT =
            BLOCKS.register("set_point",
                    rl -> new SetPointBlock(BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, rl))
                            .strength(1.0f)
                            .noLootTable()));

    // --- Block Entities ---
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NucleoMineroBlock.NucleoMineroBlockEntity>> NUCLEO_MINERO_BE =
            BLOCK_ENTITIES.register("nucleo_minero",
                    () -> new BlockEntityType<>(NucleoMineroBlock.NucleoMineroBlockEntity::new,
                            Set.of(NUCLEO_MINERO.get()), false));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SetPointBlock.SetPointBE>> SET_POINT_BE =
            BLOCK_ENTITIES.register("set_point",
                    () -> new BlockEntityType<>(SetPointBlock.SetPointBE::new,
                            Set.of(SET_POINT.get()), false));

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}