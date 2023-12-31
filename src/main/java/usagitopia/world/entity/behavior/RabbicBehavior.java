package usagitopia.world.entity.behavior;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import usagitopia.Usagitopia;

public interface RabbicBehavior
{
    EntityDataAccessor<Boolean> DATA_JUMPING = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BOOLEAN);
    
    void defineSynchedData(); // let aspect logic inject in.
    
    void handleEntityEvent(byte id); // let aspect logic inject in.
    
    void customServerAiStep(); // let aspect logic inject in.
    
    void aiStep(); // let aspect logic inject in.
    
    float getJumpPower(); // let aspect logic inject in. return 'Float.MIN_VALUE' to let aspect logic decide value returning, otherwise returning original value.
    
    void jumpFromGround(); // let aspect logic inject in.
    
    default double getJumpHorizontalModifier()
    {
        return 1.0D;
    }
    
    default void setSpeedModifier(double speedModifier)
    {
        ((Mob)this).getNavigation().setSpeedModifier(speedModifier);
        ((Mob)this).getMoveControl().setWantedPosition(
            ((Mob)this).getMoveControl().getWantedX(),
            ((Mob)this).getMoveControl().getWantedY(),
            ((Mob)this).getMoveControl().getWantedZ(),
            speedModifier
        );
    }
    
    default boolean isJumping()
    {
        try
        {
            return ((Mob)this).getEntityData().get(RabbicBehavior.DATA_JUMPING);
        }
        catch(Exception e)
        {
            Usagitopia.LOGGER.error("Exception@'usagitopia.world.entity.behavior.RabbitBehavior.isJumping()', returning 'false'.");
            Usagitopia.LOGGER.error("  - Cannot get entity data 'RabbitBehavior.DATA_JUMPING'.");
            Usagitopia.LOGGER.error("      - Exception: " + e.getMessage());
            Usagitopia.LOGGER.error("      - Caused by: " + e.getCause());
            return false;
        }
    }
    
    void setJumping(boolean jumping); // let aspect logic inject in.
    
    default void startJumping() // let aspect logic inject in.
    {
    }
    
    default SoundEvent getJumpSound()
    {
        return null;
    }
    
    default float getJumpCompletion(float partialTick)
    {
        return Float.MIN_VALUE; // return 'Float.MIN_VALUE' to let aspect logic decide value returning, otherwise returning original value.
    }
    
    class RabbicMoveControl extends MoveControl
    {
        private double nextJumpSpeed;
        
        public RabbicMoveControl(Mob rabbic)
        {
            super(rabbic);
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
            if(this.mob.isOnGround() && outer().isJumping() && !((RabbicBehavior.RabbicJumpControl)this.mob.getJumpControl()).wantJump())
            {
                this.outer().setSpeedModifier(0.0D);
            }
            else if(this.hasWanted())
            {
                this.outer().setSpeedModifier(this.nextJumpSpeed);
            }
            super.tick();
        }
        
        protected RabbicBehavior outer()
        {
            return (RabbicBehavior)this.mob;
        }
        
    }
    
    class RabbicJumpControl extends JumpControl
    {
        private final Mob     rabbic;
        private       boolean canJump;
        
        public RabbicJumpControl(Mob rabbic)
        {
            super(rabbic);
            this.rabbic = rabbic;
        }
        
        public boolean wantJump()
        {
            return this.jump;
        }
        
        public boolean canJump()
        {
            return this.canJump;
        }
        
        public void setCanJump(boolean canJump)
        {
            this.canJump = canJump;
        }
        
        @Override
        public void tick()
        {
            if(this.jump)
            {
                this.outer().startJumping();
                this.jump = false;
            }
            
        }
        
        protected RabbicBehavior outer()
        {
            return (RabbicBehavior)this.rabbic;
        }
        
    }
    
}
