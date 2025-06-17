package cl.condorzero.automina.registry;

import cl.condorzero.automina.AutoMina;
import cl.condorzero.automina.content.NucleoMineroBlock;
import cl.condorzero.automina.content.SetPointBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(AutoMina.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AutoMina.MODID);

    /* bloques */
    public static final DeferredBlock<NucleoMineroBlock> NUCLEO_MINERO =
            BLOCKS.register("nucleo_minero",
                    rl -> new NucleoMineroBlock(BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, rl))
                            .strength(4f).mapColor(MapColor.METAL)));

    public static final DeferredBlock<SetPointBlock> SET_POINT =
            BLOCKS.register("set_point",
                    rl -> new SetPointBlock(BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, rl))
                            .strength(1f).mapColor(MapColor.METAL)));

    /* block-entities (nuevo ctor) */
    public static final DeferredHolder<BlockEntityType<?>,
            BlockEntityType<NucleoMineroBlock.NucleoMineroBlockEntity>> NUCLEO_MINERO_BE =
            BLOCK_ENTITIES.register("nucleo_minero",
                    () -> new BlockEntityType<>(NucleoMineroBlock.NucleoMineroBlockEntity::new,
                            false, NUCLEO_MINERO.get()));

    public static final DeferredHolder<BlockEntityType<?>,
            BlockEntityType<SetPointBlock.SetPointBE>> SET_POINT_BE =
            BLOCK_ENTITIES.register("set_point",
                    () -> new BlockEntityType<>(SetPointBlock.SetPointBE::new,
                            false, SET_POINT.get()));

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}
