package usagitopia.world.entity.behavior;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import usagitopia.world.entity.RabbitLikeMob;

public interface RabbitBehavior
{
    
    int     jumpTicks    = 0;
    int     jumpDuration = 0;
    boolean wasOnGround    = false;
    int     jumpDelayTicks = 0;
    
    private Mob asMob()
    {
        return (Mob)this;
    }
    
    
    public default void startJumping()
    {
        asMob().setJumping(true);
        this.jumpDuration = 10;
        asMob().jumpTicks    = 0;
    }
    
    public default void setJumpControl(JumpControl jumpControl)
    {
    
    }
    
    public default void setMoveControl(MoveControl moveControl)
    {
    
    }
    
    public default void setSpeedModifier(double speedModifier)
    {
        asMob().getNavigation().setSpeedModifier(speedModifier);
        asMob().getMoveControl().setWantedPosition(asMob().getMoveControl().getWantedX(), asMob().getMoveControl().getWantedY(), asMob().getMoveControl().getWantedZ(), speedModifier);
    }
    
    public static class RabbicMoveControl extends MoveControl
    {
        private       double nextJumpSpeed;
        
        public <R extends Mob & RabbitBehavior> RabbicMoveControl(R mob)
        {
            super(mob);
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
            if(this.mob.isOnGround() && !((RabbitBehavior)this.mob).syncIsJumping() && !((RabbitLikeMob.RabbitLikeJumpControl)this.mob.getJumpControl()).wantJump())
            {
                ((RabbitBehavior)this.mob).setSpeedModifier(0.0D);
            }
            else if(this.hasWanted())
            {
                ((RabbitBehavior)this.mob).setSpeedModifier(this.nextJumpSpeed);
            }
            super.tick();
        }
        
    }
    
    public static class RabbicJumpControl extends JumpControl
    {
        private final Mob     mob;
        private       boolean canJump;
        
        public <R extends Mob & RabbitBehavior>  RabbicJumpControl(R mob)
        {
            super(mob);
            this.mob = mob;
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
                ((RabbitBehavior)this.mob).startJumping();
                this.jump = false;
            }
            
        }
        
    }
}
