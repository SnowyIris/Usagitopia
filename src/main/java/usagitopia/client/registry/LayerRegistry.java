package usagitopia.client.registry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LayerRegistry
{
    private LayerRegistry()
    {
    }
    
    @SubscribeEvent
    public static void onEntityRenderersEvent(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        entityModelLayerRegistry(event);
        blockEntityModelLayerRegistry(event);
    }
    
    private static void entityModelLayerRegistry(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    }
    
    private static void blockEntityModelLayerRegistry(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    }
    
}
