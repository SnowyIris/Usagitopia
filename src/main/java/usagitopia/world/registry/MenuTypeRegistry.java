package usagitopia.world.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import usagitopia.Usagitopia;

public final class MenuTypeRegistry
{
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Usagitopia.MOD_ID);
    
    private MenuTypeRegistry()
    {
    }
    
}
