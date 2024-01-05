package usagitopia.world.entity.behavior;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;

public interface RabbitBehavior
{
    EntityDataAccessor<Boolean> DATA_JUMPING = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BOOLEAN);
    
    void defineSynchedData(); // let aspect logic inject in.
    
    void handleEntityEvent(byte id); // let aspect logic inject in.
    
    void customServerAiStep(); // let aspect logic inject in.
    
    void aiStep(); // let aspect logic inject in.
    
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
            return ((Mob)this).getEntityData().get(RabbitBehavior.DATA_JUMPING);
        }
        catch(Exception e)
        {
            System.out.println("Exception@'usagitopia.world.entity.behavior.RabbitBehavior.isJumping()', returning 'false'.");
            System.out.println("  - Cannot get entity data 'RabbitBehavior.DATA_JUMPING'.");
            System.out.println("      - Exception: " + e.getMessage());
            System.out.println("      - Caused by: " + e.getCause());
            return false;
        }
    }
    
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
            if(this.mob.isOnGround() && outer().isJumping() && !((RabbitBehavior.RabbicJumpControl)this.mob.getJumpControl()).wantJump())
            {
                this.outer().setSpeedModifier(0.0D);
            }
            else if(this.hasWanted())
            {
                this.outer().setSpeedModifier(this.nextJumpSpeed);
            }
            super.tick();
        }
        
        protected RabbitBehavior outer()
        {
            return (RabbitBehavior)this.mob;
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
        
        public void setCanJump(boolean pCanJump)
        {
            this.canJump = pCanJump;
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
        
        protected RabbitBehavior outer()
        {
            return (RabbitBehavior)this.rabbic;
        }
        
    }
    
}
