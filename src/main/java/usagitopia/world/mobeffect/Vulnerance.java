package usagitopia.world.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import usagitopia.world.registry.MobEffectRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Vulnerance extends MobEffect
{
    public static final String REGISTRY_NAME     = "vulnerance";
    public static final float  DAMAGE_MULTIPLIER = 1.20F;
    
    public Vulnerance()
    {
        super(MobEffectCategory.HARMFUL, 0x333333);
    }
    
    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event)
    {
        if(event.getEntity().hasEffect(MobEffectRegistry.VULNERANCE.get()))
        {
            event.setAmount(event.getAmount() * DAMAGE_MULTIPLIER);
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int remainingTicks, int level)
    {
        return false;
    }
    
    @Override
    public boolean isInstantenous()
    {
        return false;
    }
    
}
