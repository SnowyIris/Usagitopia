package usagitopia.world.entity.behavior.aspect;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import usagitopia.world.entity.behavior.RabbitBehavior;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public privileged aspect RabbitBehaviorAspect
{
    // Pointcut
    pointcut isRabbic(): target(net.minecraft.world.entity.Mob) && target(usagitopia.world.entity.behavior.RabbitBehavior);
    
    // Inter-type Declarations
    int     RabbitBehavior.jumpTicks;
    int     RabbitBehavior.jumpDuration;
    boolean RabbitBehavior.wasOnGround;
    int     RabbitBehavior.jumpDelayTicks;
    
    // Introduction
    void RabbitBehavior.checkLandingDelay()
        {
            this.setLandingDelay();
            this.disableJumpControl();
        }
    
    void RabbitBehavior.facePoint(double pX, double pZ)
        {
            ((Mob)this).setYRot((float)(Mth.atan2(pZ - ((Mob)this).getZ(), pX - ((Mob)this).getX()) * (double)(180F / (float)Math.PI)) - 90.0F);
        }
    
    void RabbitBehavior.enableJumpControl()
        {
            ((RabbitBehavior.RabbicJumpControl)((Mob)this).getJumpControl()).setCanJump(true);
        }
    
    void RabbitBehavior.setLandingDelay()
        {
            if(((Mob)this).getMoveControl().getSpeedModifier() < 2.2D)
            {
                this.jumpDelayTicks = 10;
            }
            else
            {
                this.jumpDelayTicks = 1;
            }
            
        }
    
    void RabbitBehavior.disableJumpControl()
        {
            ((RabbitBehavior.RabbicJumpControl)((Mob)this).getJumpControl()).setCanJump(false);
        }
    
    // Advice
    after():execution(net.minecraft.world.entity.Mob+.new(..)) && isRabbic()
        {
            // System.out.println("##Advising after 'net.minecraft.world.entity.Mob+.new(..)'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
            Mob            mob    = (Mob)thisJoinPoint.getThis();
            RabbitBehavior rabbic = (RabbitBehavior)thisJoinPoint.getThis();
            rabbic.setSpeedModifier(0.0D);
            try
            {
                Field jumpControl = Mob.class.getDeclaredField("jumpControl");
                jumpControl.setAccessible(true);
                jumpControl.set(mob, new RabbitBehavior.RabbicJumpControl(mob));
            }
            catch(Exception e)
            {
                System.out.println("Exception when advising after 'net.minecraft.world.entity.Mob+.new(..)'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
                System.out.println("  - Cannot set jumpControl to rabbic mob using reflect.");
                System.out.println("      - Exception: " + e.getMessage());
                System.out.println("      - Caused by: " + e.getCause());
            }
            try
            {
                Field moveControl = Mob.class.getDeclaredField("moveControl");
                moveControl.setAccessible(true);
                moveControl.set(mob, new RabbitBehavior.RabbicMoveControl(mob));
            }
            catch(Exception e)
            {
                System.out.println("Exception when advising after 'net.minecraft.world.entity.Mob+.new(..)'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
                System.out.println("  - Cannot set moveControl to rabbic mob using reflect.");
                System.out.println("      - Exception: " + e.getMessage());
                System.out.println("      - Caused by: " + e.getCause());
            }
        }
    
    after():execution(void net.minecraft.world.entity.Mob+.defineSynchedData()) && isRabbic()
        {
            System.out.println("##Advising after 'void net.minecraft.world.entity.Mob+.defineSynchedData()'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
            Mob mob = (Mob)thisJoinPoint.getThis();
            mob.getEntityData().define(RabbitBehavior.DATA_JUMPING, false);
        }
    
    void around(byte id):execution(void net.minecraft.world.entity.Mob+.handleEntityEvent(byte)) && args(id) && isRabbic()
        {
            System.out.println("##Advising around 'void net.minecraft.world.entity.Mob+.handleEntityEvent(byte)'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
            if(id == 1)
            {
                Mob            mob    = (Mob)thisJoinPoint.getThis();
                RabbitBehavior rabbic = (RabbitBehavior)thisJoinPoint.getThis();
                rabbic.jumpDuration = 10;
                rabbic.jumpTicks    = 0;
                try
                {
                    Method spawnSprintParticle = Entity.class.getDeclaredMethod("spawnSprintParticle");
                    spawnSprintParticle.setAccessible(true);
                    spawnSprintParticle.invoke(mob);
                }
                catch(Exception e)
                {
                    System.out.println("Exception when advising 'void net.minecraft.world.entity.Mob+.handleEntityEvent(byte)'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
                    System.out.println("  - Cannot invoke spawnSprintParticle in class 'Entity' using reflect.");
                    System.out.println("      - Exception: " + e.getMessage());
                    System.out.println("      - Caused by: " + e.getCause());
                }
            }
            else
            {
                proceed(id);
            }
        }
    
    before():execution(void net.minecraft.world.entity.Mob+.customServerAiStep()) && isRabbic()
        {
            // System.out.println("##Advising before 'void net.minecraft.world.entity.Mob+.customServerAiStep()'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
            Mob            mob    = (Mob)thisJoinPoint.getThis();
            RabbitBehavior rabbic = (RabbitBehavior)thisJoinPoint.getThis();
            if(rabbic.jumpDelayTicks > 0)
            {
                --rabbic.jumpDelayTicks;
            }
            if(mob.isOnGround())
            {
                if(!rabbic.wasOnGround)
                {
                    mob.setJumping(false);
                    rabbic.checkLandingDelay();
                }
                if(mob.getJumpControl() instanceof RabbitBehavior.RabbicJumpControl jumpControl){
                    if(!jumpControl.wantJump())
                    {
                        if(mob.getMoveControl().hasWanted() && rabbic.jumpDelayTicks == 0)
                        {
                            Path path = mob.getNavigation().getPath();
                            Vec3 vec3 = new Vec3(mob.getMoveControl().getWantedX(), mob.getMoveControl().getWantedY(), mob.getMoveControl().getWantedZ());
                            if(path != null && !path.isDone())
                            {
                                vec3 = path.getNextEntityPos(mob);
                            }
        
                            rabbic.facePoint(vec3.x, vec3.z);
                            rabbic.startJumping();
                        }
                    }
                    else if(!jumpControl.canJump())
                    {
                        rabbic.enableJumpControl();
                    }
                }
            }
            rabbic.wasOnGround = mob.isOnGround();
        }
    
    before():execution(void net.minecraft.world.entity.Mob+.startJumping()) && isRabbic()
        {
            System.out.println("##Advising before 'void net.minecraft.world.entity.Mob+.startJumping()'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
            Mob            mob    = (Mob)thisJoinPoint.getThis();
            RabbitBehavior rabbic = (RabbitBehavior)thisJoinPoint.getThis();
            mob.setJumping(true);
            rabbic.jumpDuration = 10;
            rabbic.jumpTicks    = 0;
        }
    
    after():execution(void net.minecraft.world.entity.Mob+.aiStep()) && isRabbic()
        {
            // System.out.println("##Advising after 'void net.minecraft.world.entity.Mob+.aiStep()'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
            Mob            mob    = (Mob)thisJoinPoint.getThis();
            RabbitBehavior rabbic = (RabbitBehavior)thisJoinPoint.getThis();
            if(rabbic.jumpTicks != rabbic.jumpDuration)
            {
                ++rabbic.jumpTicks;
            }
            else if(rabbic.jumpDuration != 0)
            {
                rabbic.jumpTicks    = 0;
                rabbic.jumpDuration = 0;
                mob.setJumping(false);
            }
        }
    
    float around(float partialTick):execution(void net.minecraft.world.entity.Mob+.getJumpCompletion(float)) && args(partialTick) && isRabbic()
        {
            System.out.println("##Advising around 'void net.minecraft.world.entity.Mob+.getJumpCompletion(float)'@'usagitopia.world.entity.behavior.aspect.RabbitBehaviorAspect'");
            RabbitBehavior rabbic = (RabbitBehavior)thisJoinPoint.getThis();
            float          rtn    = proceed(partialTick);
            if(rtn == Float.MIN_VALUE)
            {
                return rabbic.jumpDuration == 0 ? 0.0F : ((float)rabbic.jumpTicks + partialTick) / (float)rabbic.jumpDuration;
            }
            else
            {
                return rtn;
            }
        }
}
