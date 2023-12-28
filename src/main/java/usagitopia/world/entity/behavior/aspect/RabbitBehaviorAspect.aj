package usagitopia.world.entity.behavior.aspect;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

public privileged aspect RabbitBehaviorAspect pertypewithin(usagitopia.world.entity.behavior.RabbitBehavior)
{
    pointcut rabbic() : target(usagitopia.world.entity.behavior.RabbitBehavior+);
    
    after():rabbic() && staticinitialization(*)
        {
            System.out.println(getClass().getName() + "+++++++++++++++++++++++++++++");
        }
}
