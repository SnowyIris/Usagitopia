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
import usagitopia.world.entity.UPRPRCGirl;

public class UPRPRCGirlModel<T extends Entity> extends EntityModel<T>
{
    public static final float              SHADOW_SIZE    = 0.30F;
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Usagitopia.MOD_ID, UPRPRCGirl.REGISTRY_NAME), "main");
    
    private static final float DTA = (float)(Math.PI / 180);
    
    protected final ModelPart BODY;
    protected final ModelPart HEAD;
    protected final ModelPart EAR_LEFT;
    protected final ModelPart EAR_LEFT_UPPER;
    protected final ModelPart EAR_RIGHT;
    protected final ModelPart EAR_RIGHT_UPPER;
    protected final ModelPart ARM_LEFT;
    protected final ModelPart ARM_RIGHT;
    protected final ModelPart WEAPON;
    protected final ModelPart LEG_LEFT;
    protected final ModelPart LEG_LEFT_LOWER;
    protected final ModelPart LEG_RIGHT;
    protected final ModelPart LEG_RIGHT_LOWER;
    
    public UPRPRCGirlModel(ModelPart root)
    {
        this.BODY            = root.getChild("body");
        this.HEAD            = BODY.getChild("head");
        this.EAR_LEFT        = HEAD.getChild("ear_left");
        this.EAR_LEFT_UPPER  = EAR_LEFT.getChild("ear_left_upper");
        this.EAR_RIGHT       = HEAD.getChild("ear_right");
        this.EAR_RIGHT_UPPER = EAR_RIGHT.getChild("ear_right_upper");
        this.ARM_LEFT        = BODY.getChild("arm_left");
        this.ARM_RIGHT       = BODY.getChild("arm_right");
        this.WEAPON          = ARM_RIGHT.getChild("weapon");
        this.LEG_LEFT        = BODY.getChild("leg_left");
        this.LEG_LEFT_LOWER  = LEG_LEFT.getChild("leg_left_lower");
        this.LEG_RIGHT       = BODY.getChild("leg_right");
        this.LEG_RIGHT_LOWER = LEG_RIGHT.getChild("leg_right_lower");
    }
    
    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -19.0F, -1.5F, 6.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                                                                                      .texOffs(0, 28).addBox(-3.0F, -19.0F, -1.5F, 6.0F, 9.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -26.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.0F))
                                                                            .texOffs(32, 0).addBox(-4.0F, -26.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.75F))
                                                                            .texOffs(54, 25).addBox(-2.0F, -28.0F, 1.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        
        PartDefinition ear_left = head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(42, 25).addBox(-1.0F, -3.75F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
                                                         PartPose.offsetAndRotation(2.0F, -25.0F, 0.0F, 0.0F, 0.0F, 0.7418F)
        );
        
        PartDefinition ear_left_upper = ear_left.addOrReplaceChild("ear_left_upper", CubeListBuilder.create().texOffs(42, 31).addBox(0.0F, -6.0F, -0.49F, 2.0F, 6.0F, 0.98F, new CubeDeformation(0.0F)),
                                                                   PartPose.offsetAndRotation(-1.0F, -3.75F, 0.0F, 0.0F, 0.0F, 1.3963F)
        );
        
        PartDefinition ear_right = head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(48, 25).addBox(-1.0F, -3.75F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
                                                          PartPose.offsetAndRotation(-2.0F, -25.0F, 0.0F, 0.0F, 0.0F, -0.7418F)
        );
        
        PartDefinition ear_right_upper = ear_right.addOrReplaceChild("ear_right_upper", CubeListBuilder.create().texOffs(48, 31).addBox(-2.0F, -6.0F, -0.49F, 2.0F, 6.0F, 0.98F, new CubeDeformation(0.0F)),
                                                                     PartPose.offsetAndRotation(1.0F, -3.75F, 0.0F, 0.0F, 0.0F, -1.3963F)
        );
        
        PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(42, 16).addBox(0.0F, -1.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)),
                                                         PartPose.offsetAndRotation(3.0F, -18.0F, 0.0F, 0.0F, 0.0F, -0.2182F)
        );
        
        PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(50, 16).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)),
                                                          PartPose.offsetAndRotation(-3.0F, -18.0F, 0.0F, -1.5708F, 0.0F, 0.2182F)
        );
        
        PartDefinition weapon = arm_right.addOrReplaceChild("weapon", CubeListBuilder.create().texOffs(0, 43).addBox(-2.0F, 5.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                                                                                     .texOffs(8, 43).addBox(-2.0F, 4.75F, -4.75F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
                                                                                     .texOffs(8, 47).addBox(-2.0F, 4.75F, -5.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                                                                                     .texOffs(8, 47).addBox(-2.0F, 6.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                     .texOffs(8, 47).addBox(-2.0F, 10.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                     .texOffs(8, 49).addBox(-2.0F, 4.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                                                                                     .texOffs(16, 43).addBox(-2.0F, 6.25F, -5.25F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.25F))
                                                                                     .texOffs(24, 43).addBox(-2.0F, 7.25F, -5.75F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.25F))
                                                                                     .texOffs(24, 47).addBox(-0.5F, 7.25F, -5.25F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.25F))
                                                                                     .texOffs(24, 47).addBox(-2.5F, 7.25F, -5.25F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.25F))
                                                                                     .texOffs(24, 43).addBox(-2.0F, 7.25F, -3.75F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        
        PartDefinition leg_left = body.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(18, 16).addBox(-1.99F, -1.0F, -1.49F, 2.98F, 6.0F, 2.98F, new CubeDeformation(-0.5F)), PartPose.offset(2.0F, -10.0F, 0.0F));
        
        PartDefinition leg_left_lower = leg_left.addOrReplaceChild("leg_left_lower", CubeListBuilder.create().texOffs(18, 25).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.5F))
                                                                                                    .texOffs(18, 34).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.25F)),
                                                                   PartPose.offsetAndRotation(-0.5F, 4.5F, -1.0F, 1.0472F, 0.0F, 0.0F)
        );
        
        PartDefinition leg_right = body.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(30, 16).addBox(-0.99F, -1.0F, -1.49F, 2.98F, 6.0F, 2.98F, new CubeDeformation(-0.5F)), PartPose.offset(-2.0F, -10.0F, 0.0F));
        
        PartDefinition leg_right_lower = leg_right.addOrReplaceChild("leg_right_lower", CubeListBuilder.create().texOffs(30, 25).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.5F))
                                                                                                       .texOffs(30, 34).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.5F, 4.5F, -1.0F));
        
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    
    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.EAR_LEFT.zRot        = 42.5F * DTA + (float)Math.sin(ageInTicks * 0.15f) * 0.1f;
        this.EAR_LEFT_UPPER.zRot  = 80.0F * DTA + (float)Math.sin(ageInTicks * 0.15f) * 0.1f;
        this.EAR_RIGHT.zRot       = -42.5F * DTA - (float)Math.sin(ageInTicks * 0.15f) * 0.1f;
        this.EAR_RIGHT_UPPER.zRot = -80.0F * DTA - (float)Math.sin(ageInTicks * 0.15f) * 0.1f;
        if(entity instanceof UPRPRCGirl uGirl)
        {
            if(uGirl.getGirlType().isGunner())
            {
                this.WEAPON.visible = uGirl.syncIsAngry();
                this.ARM_LEFT.xRot  = 0.0F;
                this.ARM_RIGHT.xRot = uGirl.syncIsAngry() ? -90.0F * DTA : 0.0F;
                this.ARM_LEFT.yRot  = 0.0F;
                this.ARM_RIGHT.yRot = 0.0F;
            }
            else
            {
                this.WEAPON.visible = false;
                if(uGirl.syncIsAngry())
                {
                    this.ARM_LEFT.xRot  = -115.0F * DTA;
                    this.ARM_RIGHT.xRot = -115.0F * DTA;
                    this.ARM_LEFT.yRot  = -22.5F * DTA;
                    this.ARM_RIGHT.yRot = 22.5F * DTA;
                }
                else
                {
                    this.ARM_LEFT.xRot  = 0.0F;
                    this.ARM_RIGHT.xRot = 0.0F;
                    this.ARM_LEFT.yRot  = 0.0F;
                    this.ARM_RIGHT.yRot = 0.0F;
                }
            }
            if(uGirl.syncIsJumping())
            {
                this.LEG_LEFT.xRot        = -10.0F * DTA;
                this.LEG_RIGHT.xRot       = -10.0F * DTA;
                this.LEG_LEFT_LOWER.xRot  = 0.0F;
                this.LEG_RIGHT_LOWER.xRot = 60.0F * DTA;
            }
            else
            {
                this.LEG_LEFT.xRot        = 0.0F;
                this.LEG_RIGHT.xRot       = 0.0F;
                this.LEG_LEFT_LOWER.xRot  = uGirl.syncIsAngry() ? 60.0F * DTA : 0.0F;
                this.LEG_RIGHT_LOWER.xRot = 0.0F;
            }
        }
    }
    
    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        BODY.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    
}
