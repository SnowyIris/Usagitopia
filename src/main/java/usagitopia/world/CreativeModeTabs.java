package usagitopia.world;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import usagitopia.world.registry.ItemRegistry;

public final class CreativeModeTabs
{
    public static final CreativeModeTab USAGITOPIA_TAB = new CreativeModeTab("usagitopia.main")
    {
        @Override
        public @NotNull ItemStack makeIcon()
        {
            return new ItemStack(ItemRegistry.BB_RABBIT.get());
        }
    };
    
}
