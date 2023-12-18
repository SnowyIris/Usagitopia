package usagitopia.world.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import usagitopia.Usagitopia;
import usagitopia.world.item.BBRabbitItem;

public class ItemRegistry
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Usagitopia.MOD_ID);
    
    public static RegistryObject<Item> BB_RABBIT = ITEMS.register(BBRabbitItem.REGISTRY_NAME, BBRabbitItem::new);
    
    private ItemRegistry()
    {
    }
    
}
