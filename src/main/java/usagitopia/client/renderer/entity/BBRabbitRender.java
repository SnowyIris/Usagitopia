package usagitopia.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import usagitopia.Usagitopia;
import usagitopia.client.model.entity.BBRabbitModel;
import usagitopia.world.entity.BBRabbitMob;

public class BBRabbitRender extends MobRenderer<BBRabbitMob, BBRabbitModel<BBRabbitMob>>
{
    
    public BBRabbitRender(EntityRendererProvider.Context ctx)
    {
        super(ctx, new BBRabbitModel<>(ctx.bakeLayer(BBRabbitModel.LAYER_LOCATION)), BBRabbitModel.SHADOW_SIZE);
    }
    
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BBRabbitMob entity)
    {
        return new ResourceLocation(Usagitopia.MOD_ID, "textures/entity/" + BBRabbitMob.REGISTRY_NAME + ".png");
    }
    
}
