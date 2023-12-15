package usagitopia.world.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import usagitopia.Usagitopia;

@Mod.EventBusSubscriber(modid = Usagitopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypeRegistry
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Usagitopia.MOD_ID);
    
    private EntityTypeRegistry()
    {
    }
    
    @SubscribeEvent
    public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event)
    {
    }
    
}
