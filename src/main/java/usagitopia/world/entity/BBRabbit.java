package usagitopia.world.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import usagitopia.world.entity.behavior.RabbicBehavior;
import usagitopia.world.registry.MobEffectRegistry;
import usagitopia.world.registry.SoundEventRegistry;

import java.util.function.Predicate;

public class BBRabbit extends PathfinderMob implements RabbicBehavior
{
    public static final String REGISTRY_NAME         = "bb_rabbit";
    public static final float  WIDTH                 = 0.7F;
    public static final float  HEIGHT                = 0.5F;
    public static final double MAX_HEALTH            = 3.0D;
    public static final double MOVEMENT_SPEED        = 0.3D;
    public static final float  TARGET_LOCK_RANGE     = 16.0F;
    public static final int    DEBUFF_ATTACH_DURANCE = 1000;
    
    public BBRabbit(EntityType<? extends PathfinderMob> entityType, Level level)
    {
        super(entityType, level);
    }
    
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
                  .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                  .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED);
    }
    
    public void throwOutFrom(Entity shooter, double velocity, double inaccuracy)
    {
        this.moveTo(shooter.position().add(0, Math.max(shooter.getEyeHeight() - 0.5, 0), 0));
        Vec3 d = new Vec3(
            -Mth.sin(shooter.getYRot() * (float)Math.PI / 180F) * Mth.cos(shooter.getXRot() * (float)Math.PI / 180F),
            -Mth.sin(shooter.getXRot() * (float)Math.PI / 180F),
            Mth.cos(shooter.getYRot() * (float)Math.PI / 180F) * Mth.cos(shooter.getXRot() * (float)Math.PI / 180F)
        ).normalize().add(
            this.random.triangle(0.0D, 0.0172275D * inaccuracy),
            this.random.triangle(0.0D, 0.0172275D * inaccuracy),
            this.random.triangle(0.0D, 0.0172275D * inaccuracy)
        ).scale(velocity);
        this.setDeltaMovement(d);
        this.setYRot((float)(Mth.atan2(d.x, d.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(d.y, d.horizontalDistance()) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        Vec3 sd = shooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(sd.x, shooter.isOnGround() ? 0.0D : sd.y, sd.z));
    }
    
    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BBRabbitVulneranceAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 1.0D, TARGET_LOCK_RANGE));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D, 1));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false,
                                                                         new BBRabbitVulneranceTargetSelector()
        ));
    }
    
    @Override
    public boolean canAttackType(@NotNull EntityType<?> type)
    {
        if(type == EntityType.PLAYER)
        {
            return false;
        }
        else
        {
            return super.canAttackType(type);
        }
    }
    
    @Override
    public void defineSynchedData()
    {
        super.defineSynchedData();
    }
    
    @Override
    public void aiStep()
    {
        super.aiStep();
    }
    
    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer)
    {
        return false;
    }
    
    @Override
    public void customServerAiStep()
    {
        super.customServerAiStep();
    }
    
    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand)
    {
        return super.mobInteract(player, hand);
    }
    
    @Override
    public boolean doHurtTarget(@NotNull Entity entity)
    {
        boolean flag = super.doHurtTarget(entity);
        if(entity instanceof LivingEntity living)
        {
            living.addEffect(new MobEffectInstance(MobEffectRegistry.VULNERANCE.get(), DEBUFF_ATTACH_DURANCE), this);
        }
        return flag;
    }
    
    @Override
    public double getJumpHorizontalModifier()
    {
        return 1.0D;
    }
    
    @Override
    public SoundEvent getJumpSound()
    {
        return SoundEventRegistry.BB_RABBIT_JUMP.get();
    }
    
    @Override
    public @NotNull Vec3 getLeashOffset()
    {
        return new Vec3(0.0D, 0.3D, 0.0D);
    }
    
    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier)
    {
        return 0;
    }
    
    @Override
    protected void actuallyHurt(@NotNull DamageSource damageSource, float damageAmount)
    {
        if(damageSource.equals(DamageSource.OUT_OF_WORLD))
        {
            super.actuallyHurt(damageSource, damageAmount);
            return;
        }
        if(this.isInvulnerableTo(damageSource))
        {
            return;
        }
        ForgeHooks.onLivingHurt(this, damageSource, damageAmount);
        float damage = 1.0F;
        if(ForgeHooks.onLivingDamage(this, damageSource, damage) > 0.0F)
        {
            float health = this.getHealth();
            this.getCombatTracker().recordDamage(damageSource, health, damage);
            this.setHealth(health - damage);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - damage);
            this.gameEvent(GameEvent.ENTITY_DAMAGE);
        }
    }
    
    @Override
    public float getJumpPower()
    {
        return Float.MIN_VALUE; // let aspect logic inject in. return 'Float.MIN_VALUE' to let aspect logic decide value returning, otherwise returning original value.
    }
    
    @Override
    public void jumpFromGround()
    {
        super.jumpFromGround();
    }
    
    @Override
    protected void doPush(@NotNull Entity entity)
    {
        if(entity instanceof Enemy && this.getRandom().nextInt(10) == 0)
        {
            this.setTarget((LivingEntity)entity);
        }
        super.doPush(entity);
    }
    
    @Override
    public void setJumping(boolean jumping)
    {
        super.setJumping(jumping);
    }
    
    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size)
    {
        return 0.4F;
    }
    
    public static class BBRabbitVulneranceTargetSelector implements Predicate<LivingEntity>
    {
        @Override
        public boolean test(LivingEntity living)
        {
            if(living instanceof Enemy)
            {
                return true;
            }
            return living instanceof NeutralMob neutral && (neutral.getTarget() instanceof Player || neutral.isAngryAtAllPlayers(living.level));
        }
        
    }
    
    public static class BBRabbitVulneranceAttackGoal extends MeleeAttackGoal
    {
        public BBRabbitVulneranceAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen)
        {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }
        
        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity enemy, double pDistToEnemySqr)
        {
            double d0 = this.getAttackReachSqr(enemy);
            if(pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0)
            {
                this.resetAttackCooldown();
                if(!this.mob.level.isClientSide())
                {
                    Mob mob = this.mob.level.getNearestEntity(Mob.class, TargetingConditions.forCombat().range(1.0D).ignoreLineOfSight().selector(new BBRabbitVulneranceTargetSelector()),
                                                              this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(1.0D)
                    );
                    if(mob != null)
                    {
                        mob.addEffect(new MobEffectInstance(MobEffectRegistry.VULNERANCE.get(), DEBUFF_ATTACH_DURANCE), this.mob);
                    }
                }
            }
        }
        
    }
    
}
