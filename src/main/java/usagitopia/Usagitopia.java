package usagitopia;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import usagitopia.world.registry.*;

@Mod(Usagitopia.MOD_ID)
public final class Usagitopia
{
    public static final String MOD_ID = "usagitopia";
    
    public Usagitopia()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::commonSetup);
        
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        SoundEventRegistry.SOUND_EVENTS.register(modEventBus);
        MenuTypeRegistry.MENUS.register(modEventBus);
        MobEffectRegistry.MOB_EFFECTS.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        MobSpawnRegistry.registerMobSpawn(event);
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    
    }
    
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        
        }
        
    }
    
}
