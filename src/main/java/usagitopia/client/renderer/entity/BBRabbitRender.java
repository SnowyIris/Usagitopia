package usagitopia.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import usagitopia.Usagitopia;
import usagitopia.client.model.entity.BBRabbitModel;
import usagitopia.world.entity.BBRabbit;

public class BBRabbitRender extends MobRenderer<BBRabbit, BBRabbitModel<BBRabbit>>
{
    
    public BBRabbitRender(EntityRendererProvider.Context ctx)
    {
        super(ctx, new BBRabbitModel<>(ctx.bakeLayer(BBRabbitModel.LAYER_LOCATION)), BBRabbitModel.SHADOW_SIZE);
    }
    
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BBRabbit entity)
    {
        return new ResourceLocation(Usagitopia.MOD_ID, "textures/entity/" + BBRabbit.REGISTRY_NAME + ".png");
    }
    
}
