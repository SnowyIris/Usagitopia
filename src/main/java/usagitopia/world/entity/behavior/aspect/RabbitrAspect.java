package usagitopia.world.entity.behavior.aspect;

import net.minecraft.world.entity.Mob;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.weaver.patterns.PerTypeWithin;
import usagitopia.world.entity.RabbitLikeMob;
import usagitopia.world.entity.behavior.RabbitBehavior;

@Aspect()
public class RabbitrAspect<R extends Mob & RabbitBehavior>
{
    @Pointcut("target(usagitopia.world.entity.behavior.RabbitBehavior) && target(net.minecraft.world.entity.Mob)")
    private void isRabbic()
    {
    }
    
    @After("isRabbic() && execution(*.new(..))")
    public void onNew(JoinPoint j)
    {
        this.getRabbic(j).setJumpControl(new RabbitBehavior.RabbicJumpControl(this.getRabbic(j)));
        this.getRabbic(j).setMoveControl(new RabbitBehavior.RabbicMoveControl(this.getRabbic(j)));
        this.getRabbic(j).setSpeedModifier(0.0D);
    }
    
    private R getRabbic(JoinPoint j)
    {
        try
        {
            return (R)j.getTarget();
        }
        catch(Exception ignored)
        {
        }
    }
}
