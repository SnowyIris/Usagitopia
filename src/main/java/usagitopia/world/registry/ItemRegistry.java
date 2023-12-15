package usagitopia.world.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import usagitopia.Usagitopia;

public class ItemRegistry
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Usagitopia.MOD_ID);
    
    private ItemRegistry()
    {
    }
    
}
