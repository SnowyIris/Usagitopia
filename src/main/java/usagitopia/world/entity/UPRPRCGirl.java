package usagitopia.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import usagitopia.Usagitopia;
import usagitopia.world.entity.behavior.RabbicBehavior;
import usagitopia.world.registry.SoundEventRegistry;

import javax.annotation.Nullable;
import java.util.UUID;

public class UPRPRCGirl extends Monster implements RabbicBehavior, NeutralMob, RangedAttackMob
{
    public static final String REGISTRY_NAME            = "uprprc_girl";
    public static final float  WIDTH                    = 10.0F / 16.0F;
    public static final float  HEIGHT                   = 24.0F / 16.0F;
    public static final double MAX_HEALTH               = 20.0D;
    public static final double MOVEMENT_SPEED           = 0.3D;
    public static final double MELEE_ATTACK_DAMAGE      = 4.0D;
    public static final int    HURTING_ANIMATION_DURING = 50;
    
    private static final UniformInt                  PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<String>  DATA_GIRL_TYPE        = SynchedEntityData.defineId(UPRPRCGirl.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_ANGRY            = SynchedEntityData.defineId(UPRPRCGirl.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HURTING          = SynchedEntityData.defineId(UPRPRCGirl.class, EntityDataSerializers.BOOLEAN);
    
    private final RangedAttackGoal gunGoal      = new RangedAttackGoal(this, 1.25D, 20, 10.0F);
    private final MeleeAttackGoal  meleeGoal    = new MeleeAttackGoal(this, 1.2D, false)
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
    private       int              hurting_tick = 0;
    
    public UPRPRCGirl(EntityType<? extends UPRPRCGirl> entityType, Level level)
    {
        super(entityType, level);
        this.xpReward = Enemy.XP_REWARD_MEDIUM;
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
    public void aiStep()
    {
        super.aiStep();
        if(!this.level.isClientSide)
        {
            this.updatePersistentAnger((ServerLevel)this.level, true);
            if(hurting_tick > 0)
            {
                --hurting_tick;
                if(hurting_tick == 0)
                {
                    this.entityData.set(DATA_HURTING, false);
                }
            }
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource)
    {
        return SoundEventRegistry.UPRPRC_GIRL_HURT.get();
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEventRegistry.UPRPRC_GIRL_DEATH.get();
    }
    
    @Override
    public boolean hurt(@NotNull DamageSource source, float amount)
    {
        if(!this.level.isClientSide)
        {
            hurting_tick = HURTING_ANIMATION_DURING;
            this.entityData.set(DATA_HURTING, true);
        }
        return super.hurt(source, amount);
    }
    
    @Override
    public float getJumpPower()
    {
        return Float.MIN_VALUE;
    }
    
    @Override
    public void jumpFromGround()
    {
        super.jumpFromGround();
    }
    
    @Override
    public void setJumping(boolean jumping)
    {
        super.setJumping(jumping);
    }
    
    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size)
    {
        return 1.40F;
    }
    
    protected float getStandingGunHeight(@NotNull Pose pose, @NotNull EntityDimensions size)
    {
        return 1.25F;
    }
    
    public boolean isHurting()
    {
        return this.entityData.get(DATA_HURTING);
    }
    
    @Override
    public double getJumpHorizontalModifier()
    {
        return 5.0d;
    }
    
    @Override
    public SoundEvent getJumpSound()
    {
        return SoundEventRegistry.UPRPRC_GIRL_JUMP.get();
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
    public void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_GIRL_TYPE, GirlType.GIRL_BLUE.name());
        this.entityData.define(DATA_ANGRY, false);
        this.entityData.define(DATA_HURTING, false);
    }
    
    @Override
    public int getExperienceReward()
    {
        return this.xpReward;
    }
    
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEventRegistry.UPRPRC_GIRL_AMBIENT.get();
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
    public void customServerAiStep()
    {
        super.customServerAiStep();
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
        boolean isGunner;
        if(Difficulty.HARD.equals(difficulty.getDifficulty()))
        {
            isGunner = random.nextFloat() < 0.67f;
        }
        else if(Difficulty.NORMAL.equals(difficulty.getDifficulty()))
        {
            isGunner = random.nextFloat() < 0.50f;
        }
        else
        {
            isGunner = random.nextFloat() < 0.33f;
        }
        if(isGunner)
        {
            switch(random.nextInt(4))
            {
                case 0 -> this.setGirlType(GirlType.GUNNER_BLUE);
                case 1 -> this.setGirlType(GirlType.GUNNER_YELLOW);
                case 2 -> this.setGirlType(GirlType.GUNNER_GREEN);
                case 3 -> this.setGirlType(GirlType.GUNNER_RED);
            }
        }
        else
        {
            switch(random.nextInt(4))
            {
                case 0 -> this.setGirlType(GirlType.GIRL_BLUE);
                case 1 -> this.setGirlType(GirlType.GIRL_YELLOW);
                case 2 -> this.setGirlType(GirlType.GIRL_GREEN);
                case 3 -> this.setGirlType(GirlType.GIRL_RED);
            }
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
        double   dX       = target.getX() - this.getX();
        double   dZ       = target.getZ() - this.getZ();
        double   dY       = target.getEyeY() - snowball.getY(); // + Math.sqrt(dX * dX + dZ * dZ) * (double)0.2F
        snowball.shoot(dX, dY, dZ, 3.0F, 4.0F);
        this.playSound(getGunFireSound(), 1.0F, 1.0F);
        this.level.addFreshEntity(snowball);
    }
    
    protected SoundEvent getGunFireSound()
    {
        return SoundEventRegistry.UPRPRC_GIRL_SHOOT.get();
    }
    
    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block)
    {
        this.playSound(SoundEventRegistry.UPRPRC_GIRL_STEP.get(), 0.15F, 1.0F);
    }
    
    @Override
    public @NotNull Component getDisplayName()
    {
        return PlayerTeam.formatNameForTeam(this.getTeam(), this.getCustomName() != null ?
                                                            removeAction(this.getCustomName()) :
                                                            Component.translatable("entity." + Usagitopia.MOD_ID + "." + this.getGirlType().getRegisterName())
        ).withStyle((style)->style.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID()));
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
    
    public enum GirlType
    {
        GIRL_BLUE("u_girl_blue", false),
        GIRL_YELLOW("u_girl_yellow", false),
        GIRL_GREEN("u_girl_green", false),
        GIRL_RED("u_girl_red", false),
        GUNNER_BLUE("u_gunner_blue", false),
        GUNNER_YELLOW("u_gunner_yellow", false),
        GUNNER_GREEN("u_gunner_green", false),
        GUNNER_RED("u_gunner_red", true);
        
        private final String  registerName;
        private final boolean isGunner;
        
        GirlType(String registerName, boolean isGunner)
        {
            this.registerName = registerName;
            this.isGunner     = isGunner;
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
        
        public String getRegisterName()
        {
            return registerName;
        }
        
        public boolean isGunner()
        {
            return isGunner;
        }
    }
    
}
