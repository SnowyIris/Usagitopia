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
import usagitopia.world.entity.BBRabbit;
import usagitopia.world.entity.UPRPRCGirl;

@Mod.EventBusSubscriber(modid = Usagitopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EntityTypeRegistry
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Usagitopia.MOD_ID);
    
    public static RegistryObject<EntityType<BBRabbit>>   BB_RABBIT   =
        ENTITY_TYPES.register(BBRabbit.REGISTRY_NAME, ()->EntityType.Builder.of(BBRabbit::new, MobCategory.MISC).sized(BBRabbit.WIDTH, BBRabbit.HEIGHT).build(BBRabbit.REGISTRY_NAME));
    public static RegistryObject<EntityType<UPRPRCGirl>> UPRPRC_GIRL =
        ENTITY_TYPES.register(UPRPRCGirl.REGISTRY_NAME, ()->EntityType.Builder.of(UPRPRCGirl::new, MobCategory.MONSTER).sized(UPRPRCGirl.WIDTH, UPRPRCGirl.HEIGHT).build(UPRPRCGirl.REGISTRY_NAME));
    
    private EntityTypeRegistry()
    {
    }
    
    @SubscribeEvent
    public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event)
    {
        event.put(EntityTypeRegistry.BB_RABBIT.get(), BBRabbit.createAttributes().build());
        event.put(EntityTypeRegistry.UPRPRC_GIRL.get(), UPRPRCGirl.createAttributes().build());
    }
}
