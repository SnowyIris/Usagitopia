package usagitopia.client.registry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ScreenRegistry
{
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event)
    {
    }
    
}
