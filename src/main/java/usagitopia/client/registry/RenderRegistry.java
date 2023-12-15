package usagitopia.client.registry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderRegistry
{
    private RenderRegistry()
    {
    }
    
    @SubscribeEvent
    public static void onRegisterRenderersEvent(EntityRenderersEvent.RegisterRenderers event)
    {
        registerEntityRenderers(event);
        registerBlockEntityRenderers(event);
    }
    
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    }
    
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    }
    
}
