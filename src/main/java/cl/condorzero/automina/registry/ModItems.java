package cl.condorzero.automina.registry;

import cl.condorzero.automina.AutoMina;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import cl.condorzero.automina.content.SetPointBlock;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(AutoMina.MODID);

    public static final DeferredHolder<Item, BlockItem> NUCLEO_MINERO_ITEM =
            ITEMS.register("nucleo_minero",
                    rl -> new BlockItem(
                            ModBlocks.NUCLEO_MINERO.get(),
                            new Item.Properties()
                                    .setId(ResourceKey.create(Registries.ITEM, rl))
                                    .useBlockDescriptionPrefix()
                    ));

    public static final DeferredHolder<Item, BlockItem> SET_POINT_ITEM =
            ITEMS.register("set_point",
                    rl -> new BlockItem(ModBlocks.SET_POINT.get(),
                            new Item.Properties()
                                    .setId(ResourceKey.create(Registries.ITEM, rl))));

    /* ---------- combustible ---------- */
    public static final DeferredHolder<Item, Item> FUEL_PELLET =
            ITEMS.registerSimpleItem("fuel_pellet",
                    new Item.Properties().stacksTo(64));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
