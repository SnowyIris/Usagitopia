package usagitopia.world.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import usagitopia.Usagitopia;

public final class SoundEventRegistry
{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Usagitopia.MOD_ID);
    
    // BB Rabbit
    public static RegistryObject<SoundEvent> BB_RABBIT_JUMP      = SOUND_EVENTS.register("entity.bb_rabbit.jump", ()->new SoundEvent(new ResourceLocation(Usagitopia.MOD_ID, "entity.bb_rabbit.jump")));
    // UPRPRC Girl
    public static RegistryObject<SoundEvent> UPRPRC_GIRL_AMBIENT = SOUND_EVENTS.register("entity.u_girl.ambient", ()->new SoundEvent(new ResourceLocation(Usagitopia.MOD_ID, "entity.u_girl.ambient")));
    public static RegistryObject<SoundEvent> UPRPRC_GIRL_STEP    = SOUND_EVENTS.register("entity.u_girl.step", ()->new SoundEvent(new ResourceLocation(Usagitopia.MOD_ID, "entity.u_girl.step")));
    public static RegistryObject<SoundEvent> UPRPRC_GIRL_JUMP    = SOUND_EVENTS.register("entity.u_girl.jump", ()->new SoundEvent(new ResourceLocation(Usagitopia.MOD_ID, "entity.u_girl.jump")));
    public static RegistryObject<SoundEvent> UPRPRC_GIRL_SHOOT   = SOUND_EVENTS.register("entity.u_girl.shoot", ()->new SoundEvent(new ResourceLocation(Usagitopia.MOD_ID, "entity.u_girl.shoot")));
    public static RegistryObject<SoundEvent> UPRPRC_GIRL_HURT    = SOUND_EVENTS.register("entity.u_girl.hurt", ()->new SoundEvent(new ResourceLocation(Usagitopia.MOD_ID, "entity.u_girl.hurt")));
    public static RegistryObject<SoundEvent> UPRPRC_GIRL_DEATH   = SOUND_EVENTS.register("entity.u_girl.death", ()->new SoundEvent(new ResourceLocation(Usagitopia.MOD_ID, "entity.u_girl.death")));
    
    private SoundEventRegistry()
    {
    }
    
}
