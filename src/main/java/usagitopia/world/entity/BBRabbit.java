package usagitopia.world.entity;

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
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import usagitopia.world.registry.MobEffectRegistry;

public class BBRabbit extends RabbitLikeMob
{
    public static final String REGISTRY_NAME         = "bb_rabbit";
    public static final float  WIDTH                 = 0.7F;
    public static final float  HEIGHT                = 0.5F;
    public static final double MAX_HEALTH            = 3.0D;
    public static final double MOVEMENT_SPEED        = 0.3D;
    public static final double ATTACK_DAMAGE         = 0.0D;
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
                  .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
                  .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
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
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 1.0D, TARGET_LOCK_RANGE));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D, 1));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (entity)->entity instanceof Enemy));
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
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer)
    {
        return false;
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
    public void aiStep()
    {
        super.aiStep();
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
        if(!this.isInvulnerableTo(damageSource))
        {
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
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size)
    {
        return 0.4F;
    }
    
}
