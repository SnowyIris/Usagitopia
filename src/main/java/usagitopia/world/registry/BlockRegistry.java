package usagitopia.world.registry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import usagitopia.Usagitopia;

public class BlockRegistry
{
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Usagitopia.MOD_ID);
    
    private BlockRegistry()
    {
    }
    
}
