package usagitopia.client.registry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import usagitopia.client.renderer.entity.BBRabbitRender;
import usagitopia.client.renderer.entity.UPRPRCGirlRender;
import usagitopia.world.registry.EntityTypeRegistry;

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
        event.registerEntityRenderer(EntityTypeRegistry.BB_RABBIT.get(), BBRabbitRender::new);
        event.registerEntityRenderer(EntityTypeRegistry.UPRPRC_GIRL.get(), UPRPRCGirlRender::new);
    }
    
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    }
    
}
