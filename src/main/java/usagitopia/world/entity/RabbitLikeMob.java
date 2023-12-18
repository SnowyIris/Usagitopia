package usagitopia.world.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public abstract class RabbitLikeMob extends PathfinderMob
{
    private int     jumpTicks;
    private int     jumpDuration;
    private boolean wasOnGround;
    private int     jumpDelayTicks;
    
    protected RabbitLikeMob(EntityType<? extends PathfinderMob> entityType, Level level)
    {
        super(entityType, level);
        this.jumpControl = new RabbitLikeMob.RabbitLikeJumpControl(this);
        this.moveControl = new RabbitLikeMob.RabbitLikeMoveControl(this);
        this.setSpeedModifier(0.0D);
    }
    
    public void setSpeedModifier(double speedModifier)
    {
        this.getNavigation().setSpeedModifier(speedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speedModifier);
    }
    
    @Override
    public void handleEntityEvent(byte pId)
    {
        if(pId == 1)
        {
            this.spawnSprintParticle();
            this.jumpDuration = 10;
            this.jumpTicks    = 0;
        }
        else
        {
            super.handleEntityEvent(pId);
        }
        
    }
    
    @Override
    public void aiStep()
    {
        super.aiStep();
        if(this.jumpTicks != this.jumpDuration)
        {
            ++this.jumpTicks;
        }
        else if(this.jumpDuration != 0)
        {
            this.jumpTicks    = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
        
    }
    
    protected SoundEvent getJumpSound()
    {
        return null;
    }
    
    @Override
    public void customServerAiStep()
    {
        if(this.jumpDelayTicks > 0)
        {
            --this.jumpDelayTicks;
        }
        
        if(this.onGround)
        {
            if(!this.wasOnGround)
            {
                this.setJumping(false);
                this.checkLandingDelay();
            }
            
            RabbitLikeMob.RabbitLikeJumpControl jumpControl = (RabbitLikeMob.RabbitLikeJumpControl)this.jumpControl;
            if(!jumpControl.wantJump())
            {
                if(this.moveControl.hasWanted() && this.jumpDelayTicks == 0)
                {
                    Path path = this.navigation.getPath();
                    Vec3 vec3 = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if(path != null && !path.isDone())
                    {
                        vec3 = path.getNextEntityPos(this);
                    }
                    
                    this.facePoint(vec3.x, vec3.z);
                    this.startJumping();
                }
            }
            else if(!jumpControl.canJump())
            {
                this.enableJumpControl();
            }
        }
        this.wasOnGround = this.onGround;
    }
    
    private void checkLandingDelay()
    {
        this.setLandingDelay();
        this.disableJumpControl();
    }
    
    private void facePoint(double pX, double pZ)
    {
        this.setYRot((float)(Mth.atan2(pZ - this.getZ(), pX - this.getX()) * (double)(180F / (float)Math.PI)) - 90.0F);
    }
    
    public void startJumping()
    {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks    = 0;
    }
    
    private void enableJumpControl()
    {
        ((RabbitLikeMob.RabbitLikeJumpControl)this.jumpControl).setCanJump(true);
    }
    
    private void setLandingDelay()
    {
        if(this.moveControl.getSpeedModifier() < 2.2D)
        {
            this.jumpDelayTicks = 10;
        }
        else
        {
            this.jumpDelayTicks = 1;
        }
        
    }
    
    private void disableJumpControl()
    {
        ((RabbitLikeMob.RabbitLikeJumpControl)this.jumpControl).setCanJump(false);
    }
    
    public boolean isJumping()
    {
        return jumping;
    }
    
    @Override
    public void setJumping(boolean pJumping)
    {
        super.setJumping(pJumping);
        if(pJumping)
        {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
        
    }
    
    @Override
    protected float getJumpPower()
    {
        if(!this.horizontalCollision && (!this.moveControl.hasWanted() || !(this.moveControl.getWantedY() > this.getY() + 0.5D)))
        {
            Path path = this.navigation.getPath();
            if(path != null && !path.isDone())
            {
                Vec3 vec3 = path.getNextEntityPos(this);
                if(vec3.y > this.getY() + 0.5D)
                {
                    return 0.5F;
                }
            }
            
            return this.moveControl.getSpeedModifier() <= 0.6D ? 0.2F : 0.3F;
        }
        else
        {
            return 0.5F;
        }
    }
    
    @Override
    protected void jumpFromGround()
    {
        super.jumpFromGround();
        double d0 = this.moveControl.getSpeedModifier();
        if(d0 > 0.0D)
        {
            double d1 = this.getDeltaMovement().horizontalDistanceSqr();
            if(d1 < 0.01D)
            {
                this.moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
            }
        }
        
        if(!this.level.isClientSide)
        {
            this.level.broadcastEntityEvent(this, (byte)1);
        }
        
    }
    
    public float getJumpCompletion(float pPartialTick)
    {
        return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + pPartialTick) / (float)this.jumpDuration;
    }
    
    public static class RabbitLikeMoveControl extends MoveControl
    {
        private final RabbitLikeMob rabbitLike;
        private       double        nextJumpSpeed;
        
        public RabbitLikeMoveControl(RabbitLikeMob rabbitLike)
        {
            super(rabbitLike);
            this.rabbitLike = rabbitLike;
        }
        
        @Override
        public void setWantedPosition(double x, double y, double z, double speed)
        {
            super.setWantedPosition(x, y, z, speed);
            if(speed > 0.0D)
            {
                this.nextJumpSpeed = speed;
            }
        }
        
        @Override
        public void tick()
        {
            if(this.rabbitLike.isOnGround() && !this.rabbitLike.isJumping() && !((RabbitLikeMob.RabbitLikeJumpControl)this.rabbitLike.getJumpControl()).wantJump())
            {
                this.rabbitLike.setSpeedModifier(0.0D);
            }
            else if(this.hasWanted())
            {
                this.rabbitLike.setSpeedModifier(this.nextJumpSpeed);
            }
            super.tick();
        }
        
    }
    
    public static class RabbitLikeJumpControl extends JumpControl
    {
        private final RabbitLikeMob rabbitLike;
        private       boolean       canJump;
        
        public RabbitLikeJumpControl(RabbitLikeMob rabbitLike)
        {
            super(rabbitLike);
            this.rabbitLike = rabbitLike;
        }
        
        public boolean wantJump()
        {
            return this.jump;
        }
        
        public boolean canJump()
        {
            return this.canJump;
        }
        
        public void setCanJump(boolean pCanJump)
        {
            this.canJump = pCanJump;
        }
        
        @Override
        public void tick()
        {
            if(this.jump)
            {
                this.rabbitLike.startJumping();
                this.jump = false;
            }
            
        }
        
    }
    
}
