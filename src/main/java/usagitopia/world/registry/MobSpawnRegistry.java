package usagitopia.world.registry;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber
public final class MobSpawnRegistry
{
    
    public static void registerMobSpawn(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->SpawnPlacements.register(EntityTypeRegistry.UPRPRC_GIRL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Monster::checkMonsterSpawnRules));
    }
    
}
