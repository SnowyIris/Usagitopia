package usagitopia.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class LevelUtil
{
    private LevelUtil()
    {
    }
    
    public static @Nullable Player getPlayerFromUUIDString(Level level, String uuid)
    {
        try
        {
            return level.getPlayerByUUID(UUID.fromString(uuid));
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    // Copy from net.minecraft.server.level.ServerLevel
    public static BlockPos findLightningTargetAround(ServerLevel serverLevel, BlockPos p_143289_)
    {
        BlockPos           blockpos = serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, p_143289_);
        Optional<BlockPos> optional = LevelUtil.findLightningRod(serverLevel, blockpos);
        if(optional.isPresent())
        {
            return optional.get();
        }
        else
        {
            AABB               aabb = (new AABB(blockpos, new BlockPos(blockpos.getX(), serverLevel.getMaxBuildHeight(), blockpos.getZ()))).inflate(3.0D);
            List<LivingEntity> list = serverLevel.getEntitiesOfClass(LivingEntity.class, aabb, (p_184067_)->p_184067_ != null && p_184067_.isAlive() && serverLevel.canSeeSky(p_184067_.blockPosition()));
            if(!list.isEmpty())
            {
                return list.get(serverLevel.random.nextInt(list.size())).blockPosition();
            }
            else
            {
                if(blockpos.getY() == serverLevel.getMinBuildHeight() - 1)
                {
                    blockpos = blockpos.above(2);
                }
                
                return blockpos;
            }
        }
    }
    
    // Copy from net.minecraft.server.level.ServerLevel
    private static Optional<BlockPos> findLightningRod(ServerLevel serverLevel, BlockPos p_143249_)
    {
        Optional<BlockPos> optional = serverLevel.getPoiManager().findClosest((p_215059_)->p_215059_.is(PoiTypes.LIGHTNING_ROD),
                                                                              (p_184055_)->p_184055_.getY() == serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE, p_184055_.getX(), p_184055_.getZ()) - 1,
                                                                              p_143249_, 128, PoiManager.Occupancy.ANY
        );
        return optional.map((p_184053_)->p_184053_.above(1));
    }
    
}
