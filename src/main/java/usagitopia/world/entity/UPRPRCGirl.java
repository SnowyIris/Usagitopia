package usagitopia.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import usagitopia.Usagitopia;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.UUID;

public class UPRPRCGirl extends RabbitLikeMonster implements NeutralMob, RangedAttackMob
{
    public static final String REGISTRY_NAME       = "uprprc_girl";
    public static final float  WIDTH               = 10.0F / 16.0F;
    public static final float  HEIGHT              = 24.0F / 16.0F;
    public static final double MAX_HEALTH          = 20.0D;
    public static final double MOVEMENT_SPEED      = 0.5D;
    public static final double MELEE_ATTACK_DAMAGE = 4.0D;
    
    public static final HashSet<ResourceKey<Biome>> AVAILABLE_SPAWN_BIOME = new HashSet<>();
    
    private static final UniformInt                  PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Boolean> DATA_ANGRY            = SynchedEntityData.defineId(UPRPRCGirl.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String>  DATA_GIRL_TYPE        = SynchedEntityData.defineId(UPRPRCGirl.class, EntityDataSerializers.STRING);
    
    static
    {
        AVAILABLE_SPAWN_BIOME.add(Biomes.JUNGLE);
        AVAILABLE_SPAWN_BIOME.add(Biomes.GROVE);
        AVAILABLE_SPAWN_BIOME.add(Biomes.BAMBOO_JUNGLE);
        AVAILABLE_SPAWN_BIOME.add(Biomes.SPARSE_JUNGLE);
        AVAILABLE_SPAWN_BIOME.add(Biomes.BEACH);
        AVAILABLE_SPAWN_BIOME.add(Biomes.BIRCH_FOREST);
        AVAILABLE_SPAWN_BIOME.add(Biomes.COLD_OCEAN);
        AVAILABLE_SPAWN_BIOME.add(Biomes.DARK_FOREST);
        AVAILABLE_SPAWN_BIOME.add(Biomes.DEEP_COLD_OCEAN);
        AVAILABLE_SPAWN_BIOME.add(Biomes.DEEP_LUKEWARM_OCEAN);
        AVAILABLE_SPAWN_BIOME.add(Biomes.DEEP_OCEAN);
        AVAILABLE_SPAWN_BIOME.add(Biomes.FLOWER_FOREST);
        AVAILABLE_SPAWN_BIOME.add(Biomes.FOREST);
        AVAILABLE_SPAWN_BIOME.add(Biomes.LUKEWARM_OCEAN);
        AVAILABLE_SPAWN_BIOME.add(Biomes.OCEAN);
        AVAILABLE_SPAWN_BIOME.add(Biomes.OLD_GROWTH_BIRCH_FOREST);
        AVAILABLE_SPAWN_BIOME.add(Biomes.OLD_GROWTH_PINE_TAIGA);
        AVAILABLE_SPAWN_BIOME.add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        AVAILABLE_SPAWN_BIOME.add(Biomes.PLAINS);
        AVAILABLE_SPAWN_BIOME.add(Biomes.RIVER);
        AVAILABLE_SPAWN_BIOME.add(Biomes.SAVANNA);
        AVAILABLE_SPAWN_BIOME.add(Biomes.SAVANNA_PLATEAU);
        AVAILABLE_SPAWN_BIOME.add(Biomes.SUNFLOWER_PLAINS);
        AVAILABLE_SPAWN_BIOME.add(Biomes.THE_VOID);
        AVAILABLE_SPAWN_BIOME.add(Biomes.WARM_OCEAN);
        AVAILABLE_SPAWN_BIOME.add(Biomes.WINDSWEPT_FOREST);
        AVAILABLE_SPAWN_BIOME.add(Biomes.WINDSWEPT_GRAVELLY_HILLS);
        AVAILABLE_SPAWN_BIOME.add(Biomes.WINDSWEPT_HILLS);
        AVAILABLE_SPAWN_BIOME.add(Biomes.WINDSWEPT_SAVANNA);
        AVAILABLE_SPAWN_BIOME.add(Biomes.WOODED_BADLANDS);
        
    }
    
    private final RangedAttackGoal gunGoal   = new RangedAttackGoal(this, 1.25D, 20, 10.0F);
    private final MeleeAttackGoal  meleeGoal = new MeleeAttackGoal(this, 1.2D, false)
    {
        @Override
        public void start()
        {
            super.start();
            UPRPRCGirl.this.setAggressive(true);
        }
        
        @Override
        public void stop()
        {
            super.stop();
            UPRPRCGirl.this.setAggressive(false);
        }
    };
    // private int targetChangeTime;
    private       int              remainingPersistentAngerTime;
    private       UUID             persistentAngerTarget;
    
    public UPRPRCGirl(EntityType<? extends UPRPRCGirl> entityType, Level level)
    {
        super(entityType, level);
        this.xpReward = Enemy.XP_REWARD_MEDIUM;
    }
    
    public static boolean canSpawnIn(@Nullable DimensionType dimension, @Nullable Holder<Biome> biome)
    {
        if(dimension != null && !dimension.natural())
        {
            return false;
        }
        return biome == null || AVAILABLE_SPAWN_BIOME.stream().anyMatch(biome::is);
    }
    
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
                  .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                  .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
                  .add(Attributes.ATTACK_DAMAGE, MELEE_ATTACK_DAMAGE);
    }
    
    private static Component removeAction(Component name)
    {
        MutableComponent mutablecomponent = name.plainCopy().setStyle(name.getStyle().withClickEvent(null));
        for(Component component : name.getSiblings())
        {
            mutablecomponent.append(removeAction(component));
        }
        return mutablecomponent;
    }
    
    public boolean syncIsAngry()
    {
        return this.entityData.get(DATA_ANGRY);
    }
    
    public boolean wantAttack(@NotNull LivingEntity living)
    {
        if(living instanceof Player player)
        {
            // TODO: 判断玩家是否为兔子
            return false;
        }
        return living instanceof Rabbit;
    }
    
    @Override
    public @NotNull SoundSource getSoundSource()
    {
        return SoundSource.HOSTILE;
    }
    
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource)
    {
        return SoundEvents.PIGLIN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.PIGLIN_DEATH;
    }
    
    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_ANGRY, false);
        this.entityData.define(DATA_GIRL_TYPE, GirlType.GIRL_BLUE.name());
    }
    
    @Override
    public void customServerAiStep()
    {
        super.customServerAiStep();
    }
    
    @Override
    public void aiStep()
    {
        super.aiStep();
        if(!this.level.isClientSide)
        {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
    }
    
    @Override
    protected double getJumpHorizontalModifier()
    {
        return 5.0d;
    }
    
    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D, 50));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ResetUniversalAngerTargetGoal<>(this, false));
    }
    
    @Override
    public void setTarget(@Nullable LivingEntity livingEntity)
    {
        if(livingEntity == null)
        {
            this.entityData.set(DATA_ANGRY, false);
        }
        else
        {
            // this.targetChangeTime = this.tickCount;
            this.entityData.set(DATA_ANGRY, true);
        }
        super.setTarget(livingEntity);
    }
    
    @Override
    public int getExperienceReward()
    {
        return this.xpReward;
    }
    
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.PIGLIN_AMBIENT;
    }
    
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putString("GirlType", this.getGirlType().name());
    }
    
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        this.setGirlType(GirlType.valueOfWithDefault(tag.getString("GirlType"), GirlType.GIRL_BLUE));
        this.reassessWeaponGoal();
    }
    
    @Override
    public @Nullable
    SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag
    )
    {
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        RandomSource randomsource = level.getRandom();
        this.populateDefaultGirlType(randomsource, difficulty);
        this.reassessWeaponGoal();
        return spawnData;
    }
    
    protected void populateDefaultGirlType(RandomSource random, DifficultyInstance difficulty)
    {
        switch(random.nextInt(4))
        {
            case 1 -> this.setGirlType(GirlType.GIRL_GREEN);
            case 2 -> this.setGirlType(GirlType.GIRL_RED);
            case 3 -> this.setGirlType(GirlType.GUNNER_RED);
            default -> this.setGirlType(GirlType.GIRL_BLUE);
        }
    }
    
    public void reassessWeaponGoal()
    {
        if(!this.level.isClientSide)
        {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.gunGoal);
            if(this.getGirlType().isGunner())
            {
                this.goalSelector.addGoal(1, this.gunGoal);
            }
            else
            {
                this.goalSelector.addGoal(1, this.meleeGoal);
            }
            
        }
    }
    
    public GirlType getGirlType()
    {
        return GirlType.valueOfWithDefault(this.entityData.get(DATA_GIRL_TYPE), GirlType.GIRL_BLUE);
    }
    
    protected void setGirlType(GirlType newGirlType)
    {
        this.entityData.set(DATA_GIRL_TYPE, newGirlType.name());
    }
    
    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor)
    {
        Snowball snowball = new Snowball(this.level, this);
        double   d0       = target.getEyeY() - (double)1.1F;
        double   d1       = target.getX() - this.getX();
        double   d2       = d0 - snowball.getY();
        double   d3       = target.getZ() - this.getZ();
        double   d4       = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(getGunFireSound(), 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(snowball);
    }
    
    protected SoundEvent getGunFireSound()
    {
        return SoundEvents.SKELETON_SHOOT;
    }
    
    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size)
    {
        return 1.40F;
    }
    
    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block)
    {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }
    
    @Override
    public @NotNull Component getDisplayName()
    {
        return PlayerTeam.formatNameForTeam(this.getTeam(), this.getCustomName() != null ?
                                                            removeAction(this.getCustomName()) :
                                                            Component.translatable("entity." + Usagitopia.MOD_ID + "." + this.getGirlType().getName4Registry())
        ).withStyle((p_185975_)->p_185975_.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID()));
    }
    
    @Override
    public int getRemainingPersistentAngerTime()
    {
        return this.remainingPersistentAngerTime;
    }
    
    @Override
    public void setRemainingPersistentAngerTime(int time)
    {
        this.remainingPersistentAngerTime = time;
    }
    
    @Override
    public @Nullable
    UUID getPersistentAngerTarget()
    {
        return this.persistentAngerTarget;
    }
    
    @Override
    public void setPersistentAngerTarget(@Nullable UUID target)
    {
        this.persistentAngerTarget = target;
    }
    
    @Override
    public void startPersistentAngerTimer()
    {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }
    
    public enum GirlColor
    {
        RED("red"),
        BLUE("blue"),
        GREEN("green");
        
        private final String colorName;
        
        GirlColor(String colorName)
        {
            this.colorName = colorName;
        }
        
        public String getColorName()
        {
            return colorName;
        }
    }
    
    public enum GirlType
    {
        GUNNER_RED(GirlColor.RED, true),
        GIRL_RED(GirlColor.RED, false),
        GIRL_BLUE(GirlColor.BLUE, false),
        GIRL_GREEN(GirlColor.GREEN, false);
        
        private final GirlColor color;
        private final boolean   isGunner;
        
        GirlType(GirlColor color, boolean isGunner)
        {
            this.color    = color;
            this.isGunner = isGunner;
        }
        
        public static GirlType valueOfWithDefault(String name, GirlType defaultType)
        {
            try
            {
                return valueOf(name);
            }
            catch(Exception e)
            {
                return defaultType;
            }
        }
        
        public String getName4Registry()
        {
            return "u_" + (isGunner() ? "gunner_" : "girl_") + getColor().getColorName();
        }
        
        public boolean isGunner()
        {
            return isGunner;
        }
        
        public GirlColor getColor()
        {
            return color;
        }
    }
    
}
