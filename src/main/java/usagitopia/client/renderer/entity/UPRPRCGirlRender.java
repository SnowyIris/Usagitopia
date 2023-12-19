package usagitopia.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import usagitopia.Usagitopia;
import usagitopia.client.model.entity.UPRPRCGirlModel;
import usagitopia.world.entity.UPRPRCGirl;

public class UPRPRCGirlRender extends MobRenderer<UPRPRCGirl, UPRPRCGirlModel<UPRPRCGirl>>
{
    
    public UPRPRCGirlRender(EntityRendererProvider.Context ctx)
    {
        super(ctx, new UPRPRCGirlModel<>(ctx.bakeLayer(UPRPRCGirlModel.LAYER_LOCATION)), UPRPRCGirlModel.SHADOW_SIZE);
    }
    
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull UPRPRCGirl entity)
    {
        return new ResourceLocation(Usagitopia.MOD_ID, "textures/entity/" + entity.getGirlType().getName4Registry() + ".png");
    }
    
}
