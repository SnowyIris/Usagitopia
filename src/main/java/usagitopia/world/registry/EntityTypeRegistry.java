package usagitopia.world.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import usagitopia.Usagitopia;
import usagitopia.world.entity.BBRabbitMob;

@Mod.EventBusSubscriber(modid = Usagitopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypeRegistry
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Usagitopia.MOD_ID);
    
    public static RegistryObject<EntityType<BBRabbitMob>> BB_RABBIT =
        ENTITY_TYPES.register(BBRabbitMob.REGISTRY_NAME, ()->EntityType.Builder.of(BBRabbitMob::new, MobCategory.MISC).sized(BBRabbitMob.WIDTH, BBRabbitMob.HEIGHT).build(BBRabbitMob.REGISTRY_NAME));
    
    private EntityTypeRegistry()
    {
    }
    
    @SubscribeEvent
    public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event)
    {
        event.put(EntityTypeRegistry.BB_RABBIT.get(), BBRabbitMob.createAttributes().build());
    }
    
}
