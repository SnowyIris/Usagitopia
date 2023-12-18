package usagitopia.world.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import usagitopia.Usagitopia;
import usagitopia.world.mobeffect.Vulnerance;

public class MobEffectRegistry
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Usagitopia.MOD_ID);
    
    public static RegistryObject<MobEffect> VULNERANCE = MOB_EFFECTS.register(Vulnerance.REGISTRY_NAME, Vulnerance::new);
    
    private MobEffectRegistry()
    {
    }
    
}
