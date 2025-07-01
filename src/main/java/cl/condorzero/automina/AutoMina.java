package cl.condorzero.automina;

import cl.condorzero.automina.content.NucleoBlock;
import cl.condorzero.automina.content.NucleoBlockEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(AutoMina.MODID)
public class AutoMina {

    public static final String MODID = "automina";
    private static final Logger LOGGER = LogUtils.getLogger();

    // REGISTROS CENTRALIZADOS
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // --- BLOQUES ---
    // Se registra explícitamente con .setId() para cumplir el requisito de la API.
    public static final DeferredHolder<Block, Block> NUCLEO_BLOCK = BLOCKS.register("nucleo",
            rl -> new NucleoBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, rl))
                    .strength(5.0F).sound(SoundType.METAL)));

    public static final DeferredHolder<Block, Block> DEMARCADOR_BLOCK = BLOCKS.register("demarcador",
            rl -> new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, rl))
                    .strength(2.0F).sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> BODEGA_BLOCK = BLOCKS.register("bodega",
            rl -> new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, rl))
                    .strength(3.0F).sound(SoundType.WOOD)));

    // --- ÍTEMS DE BLOQUE ---
    // Se registra explícitamente con .setId() para CADA item.
    public static final DeferredHolder<Item, Item> NUCLEO_ITEM = ITEMS.register("nucleo",
            rl -> new BlockItem(NUCLEO_BLOCK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, rl))));
    public static final DeferredHolder<Item, Item> DEMARCADOR_ITEM = ITEMS.register("demarcador",
            rl -> new BlockItem(DEMARCADOR_BLOCK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, rl))));
    public static final DeferredHolder<Item, Item> BODEGA_ITEM = ITEMS.register("bodega",
            rl -> new BlockItem(BODEGA_BLOCK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, rl))));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NucleoBlockEntity>> NUCLEO_BE =
            BLOCK_ENTITIES.register("nucleo", _loc ->
                    new BlockEntityType<>(
                            NucleoBlockEntity::new, // fábrica (pos, state) -> nueva instancia
                            false,                  // ≈ allowUnsafeNBT. Pon true sólo si necesitas restringir NBT a OPs
                            NUCLEO_BLOCK.get()      // var‑arg de bloques que usan esta entidad
                    ));

    // --- PESTAÑA DE CREATIVO ---
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AUTOMINA_TAB = CREATIVE_MODE_TABS.register("automina_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.automina_tab"))
                    .icon(() -> new ItemStack(NUCLEO_ITEM.get()))
                    .displayItems((params, output) -> {
                        output.accept(NUCLEO_ITEM.get());
                        output.accept(DEMARCADOR_ITEM.get());
                        output.accept(BODEGA_ITEM.get());
                    }).build());

    public AutoMina(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        LOGGER.info("AutoMina Registries Initialized.");
    }
}