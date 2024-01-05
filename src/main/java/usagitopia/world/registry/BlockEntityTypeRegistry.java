package usagitopia.world.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import usagitopia.Usagitopia;

public final class BlockEntityTypeRegistry
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Usagitopia.MOD_ID);
    
    private BlockEntityTypeRegistry()
    {
    }
    
}
