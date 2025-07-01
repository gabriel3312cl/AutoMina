package cl.condorzero.automina.content;

// CORRECCIÓN: Apuntar a la clase principal para el registro
import cl.condorzero.automina.AutoMina;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NucleoBlockEntity extends BlockEntity {

    private String nucleusName = "Default";

    public NucleoBlockEntity(BlockPos pos, BlockState state) {
        // CORRECCIÓN: Apuntar a AutoMina.NUCLEO_BE en lugar de ModBlockEntities
        super(AutoMina.NUCLEO_BE.get(), pos, state);
    }

    // ... (el resto de la clase se mantiene igual)
    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putString("NucleusName", this.nucleusName);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.nucleusName = nbt.getString("NucleusName").orElse("Default");
    }

    public Component getDisplayName() {
        return Component.literal(this.nucleusName);
    }

    public void setDisplayName(String name) {
        this.nucleusName = name;
        setChanged();
    }
}