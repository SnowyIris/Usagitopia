package usagitopia.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import usagitopia.Usagitopia;
import usagitopia.world.entity.BBRabbit;

public class BBRabbitModel<T extends Entity> extends EntityModel<T>
{
    public static final float              SHADOW_SIZE    = 0.40F;
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Usagitopia.MOD_ID, BBRabbit.REGISTRY_NAME), "main");
    
    protected final ModelPart BODY;
    protected final ModelPart EAR_LEFT_UPPER;
    protected final ModelPart EAR_RIGHT_UPPER;
    protected final ModelPart SPRING;
    
    public BBRabbitModel(ModelPart root)
    {
        this.BODY            = root.getChild("body");
        this.EAR_LEFT_UPPER  = BODY.getChild("ear_left").getChild("ear_left_upper");
        this.EAR_RIGHT_UPPER = BODY.getChild("ear_right").getChild("ear_right_upper");
        this.SPRING          = BODY.getChild("spring");
    }
    
    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -5.0F, -3.5F, 9.0F, 6.0F, 7.0F, new CubeDeformation(-1.0F)),
                                                               PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F)
        );
        
        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        
        PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                                                        PartPose.offsetAndRotation(4.25F, -3.5F, 0.0F, 0.0F, 0.7854F, -1.0472F)
        );
        
        PartDefinition ear_left = body.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(10, 13).addBox(-3.75F, -7.5F, -0.76F, 1.75F, 4.0F, 3.02F, new CubeDeformation(-0.5F)),
                                                         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F)
        );
        
        PartDefinition ear_left_upper = ear_left.addOrReplaceChild("ear_left_upper", CubeListBuilder.create().texOffs(0, 13).addBox(-1.25F, -3.5F, -2.75F, 1.75F, 4.0F, 3.0F, new CubeDeformation(-0.5F)),
                                                                   PartPose.offsetAndRotation(-2.5F, -7.0F, 2.0F, 0.0F, 0.0F, -1.0472F)
        );
        
        PartDefinition cube_r2 = ear_left_upper.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 13).addBox(0.76F, -1.2F, -1.2F, 1.73F, 2.4F, 2.4F, new CubeDeformation(-0.5F)),
                                                                  PartPose.offsetAndRotation(-2.0F, -3.0F, -1.25F, -0.7854F, 0.0F, 0.0F)
        );
        
        PartDefinition ear_right = body.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(10, 13).addBox(-3.75F, -7.5F, -2.26F, 1.75F, 4.0F, 3.02F, new CubeDeformation(-0.5F)),
                                                          PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F)
        );
        
        PartDefinition ear_right_upper = ear_right.addOrReplaceChild("ear_right_upper", CubeListBuilder.create().texOffs(0, 13).addBox(-1.25F, -3.5F, -4.25F, 1.75F, 4.0F, 3.0F, new CubeDeformation(-0.5F)),
                                                                     PartPose.offsetAndRotation(-2.5F, -7.0F, 2.0F, 0.0F, 0.0F, -0.5236F)
        );
        
        PartDefinition cube_r3 = ear_right_upper.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 13).addBox(0.76F, -1.2F, -1.2F, 1.73F, 2.4F, 2.4F, new CubeDeformation(-0.5F)),
                                                                   PartPose.offsetAndRotation(-2.0F, -3.0F, -2.75F, -0.7854F, 0.0F, 0.0F)
        );
        
        PartDefinition spring = body.addOrReplaceChild("spring", CubeListBuilder.create().texOffs(0, 20).addBox(-0.5F, -3.25F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                .texOffs(0, 20).addBox(0.5F, -4.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                .texOffs(0, 20).addBox(1.5F, -3.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                .texOffs(0, 20).addBox(0.5F, -2.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                .texOffs(0, 20).addBox(-1.5F, -4.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                .texOffs(0, 20).addBox(-2.5F, -3.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                .texOffs(0, 20).addBox(-1.5F, -2.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, -4.5F, 0.0F, 0.0F, 0.2182F, 0.2182F));
        
        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    
    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.SPRING.yRot          = (float)((ageInTicks * 0.2F) % (2 * Math.PI));
        this.EAR_LEFT_UPPER.zRot  = -1.0F + (float)Math.sin(ageInTicks * 0.15f) * 0.1f;
        this.EAR_RIGHT_UPPER.zRot = -0.5F + (float)Math.sin(ageInTicks * 0.15f + 1) * 0.08f;
    }
    
    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        BODY.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    
}
