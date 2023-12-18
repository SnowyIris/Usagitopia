package usagitopia.client.registry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import usagitopia.client.model.entity.BBRabbitModel;

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
        event.registerLayerDefinition(BBRabbitModel.LAYER_LOCATION, BBRabbitModel::createBodyLayer);
    }
    
    private static void blockEntityModelLayerRegistry(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    }
    
}
