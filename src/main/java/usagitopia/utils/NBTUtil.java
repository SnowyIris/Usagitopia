package usagitopia.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public final class NBTUtil
{
    private NBTUtil()
    {
    }
    
    public static GlobalPos readGlobalPosFromCompoundTag(CompoundTag tag)
    {
        return GlobalPos.of(ResourceKey.create(
            ResourceKey.createRegistryKey(new ResourceLocation(tag.getString("DimensionRegistryName"))),
            new ResourceLocation(tag.getString("DimensionResourceLocation"))
        ), readBlockPosFromCompoundTag(tag));
    }
    
    public static BlockPos readBlockPosFromCompoundTag(CompoundTag tag)
    {
        return new BlockPos(tag.getInt("BlockPosX"), tag.getInt("BlockPosY"), tag.getInt("BlockPosZ"));
    }
    
    public static Vec3 readVec3FromCompoundTag(CompoundTag tag)
    {
        return new Vec3(tag.getDouble("Vec3X"), tag.getDouble("Vec3Y"), tag.getDouble("Vec3Z"));
    }
    
    public static CompoundTag writeCompoundTagFromGlobalPos(GlobalPos globalPos)
    {
        CompoundTag tag = writeCompoundTagFromBlockPos(globalPos.pos());
        tag.put("DimensionRegistryName", StringTag.valueOf(globalPos.dimension().registry().toString()));
        tag.put("DimensionResourceLocation", StringTag.valueOf(globalPos.dimension().location().toString()));
        return tag;
    }
    
    public static CompoundTag writeCompoundTagFromBlockPos(BlockPos blockPos)
    {
        CompoundTag tag = new CompoundTag();
        tag.put("BlockPosX", IntTag.valueOf(blockPos.getX()));
        tag.put("BlockPosY", IntTag.valueOf(blockPos.getY()));
        tag.put("BlockPosZ", IntTag.valueOf(blockPos.getZ()));
        return tag;
    }
    
    public static CompoundTag writeCompoundTagFromVec3(Vec3 vec3)
    {
        CompoundTag tag = new CompoundTag();
        tag.put("Vec3X", DoubleTag.valueOf(vec3.x()));
        tag.put("Vec3Y", DoubleTag.valueOf(vec3.y()));
        tag.put("Vec3Z", DoubleTag.valueOf(vec3.z()));
        return tag;
    }
    
}
