package de.melanx.boohoo.ghost;
// Made with Blockbench 3.9.3
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class GhostModel extends EntityModel<Ghost> implements ArmedModel, HeadedModel {

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
    public float swimAmount;

    public GhostModel(ModelPart model) {
        super(RenderType::entityTranslucent);
        this.head = model.getChild("head");
        this.body = model.getChild("body");
        this.rightArm = model.getChild("right_arm");
        this.leftArm = model.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();


        partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 4.0F, 0.0F));
        partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, 0.0F, -1.5F, 8.0F, 12.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(0.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(4.0F, 4.0F, 0.0F));
        partDefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(-3.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(-4.0F, 4.0F, 0.0F));

//        partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
//                        .texOffs(0, 15).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 6.0f, 6.0f),
//                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
//
//        partDefinition.addOrReplaceChild("body", CubeListBuilder.create()
//                        .texOffs(0, 0).addBox(-4.0f, 4.0f, -1.5f, 8.0f, 12.0f, 3.0f),
//                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.1745f, 0.0f, 0.0f));
//
//        partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
//                        .texOffs(22, 0).addBox(-1.0f, 0.0f, -1.5f, 3.0f, 8.0f, 3.0f),
//                PartPose.offsetAndRotation(-5.0f, -8.0f, 0.0f, 0.0f, 0.0f, 0.0f));
//
//        partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
//                        .texOffs(22, 0).addBox(0f, 0.0f, 0f, 3.0f, 8.0f, 3.0f),
//                PartPose.offsetAndRotation(3.0f, 4.0f, -1.5f, 0.0f, 0.0f, 0.0f));

//        partDefinition.addOrReplaceChild("body", CubeListBuilder.create()
//                        .texOffs(0, 0).addBox(-4.0f, 0.0f, -1.5f, 8.0f, 12.0f, 3.0f),
//                PartPose.offsetAndRotation(0.0f, 4.0f, 0.0f, 0.1745f, 0.0f, 0.0f));
//
//        partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
//                        .texOffs(22, 0).addBox(4.0f, -15.0f, -1.5f, 3.0f, 8.0f, 3.0f),
//                PartPose.offset(0.0f, 19.0f, 0.0f));
//
//        partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
//                        .texOffs(22, 0).addBox(-7.0f, -15.0f, -1.5f, 3.0f, 8.0f, 3.0f).mirror(),
//                PartPose.offset(0.0f, 19.0f, 0.0f));
//
//        partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
//                        .texOffs(0, 15).addBox(-3.0f, -21.0f, -3.0f, 6.0f, 6.0f, 6.0f),
//                PartPose.offset(0.0f, 19.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void prepareMobModel(@Nonnull Ghost ghost, float limbSwing, float limbSwingAmount, float partialTick) {
        this.swimAmount = ghost.getSwimAmount(partialTick);
        super.prepareMobModel(ghost, limbSwing, limbSwingAmount, partialTick);
    }

    @Override
    public void setupAnim(@Nonnull Ghost ghost, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        HumanoidModel.ArmPose mainHandPose = this.getArmPose(ghost, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose offHandPose = this.getArmPose(ghost, InteractionHand.OFF_HAND);

        if (ghost.getMainArm() == HumanoidArm.RIGHT) {
            this.rightArmPose = mainHandPose;
            this.leftArmPose = offHandPose;
        } else {
            this.rightArmPose = offHandPose;
            this.leftArmPose = mainHandPose;
        }

        boolean flag = ghost.getFallFlyingTicks() > 4;
        boolean flag1 = ghost.isVisuallySwimming();
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        if (flag) {
            this.head.xRot = (-(float) Math.PI / 4F);
        } else if (this.swimAmount > 0.0F) {
            if (flag1) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (-(float) Math.PI / 4F));
            } else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, headPitch * ((float) Math.PI / 180F));
            }
        } else {
            this.head.xRot = headPitch * ((float) Math.PI / 180F);
        }

        this.body.yRot = 0.0F;
        this.rightArm.z = 0;
        this.rightArm.x = -4; // -5
        this.leftArm.z = 0;
        this.leftArm.x = 4;
        float f = 1.0F;
        if (flag) {
            f = (float) ghost.getDeltaMovement().lengthSqr();
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        if (this.riding) {
            this.rightArm.xRot += (-(float) Math.PI / 5F);
            this.leftArm.xRot += (-(float) Math.PI / 5F);
        }

        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        boolean flag2 = ghost.getMainArm() == HumanoidArm.RIGHT;
        if (ghost.isUsingItem()) {
            boolean flag3 = ghost.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (flag3 == flag2) {
                this.poseRightArm(ghost);
            } else {
                this.poseLeftArm(ghost);
            }
        } else {
            boolean flag4 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if (flag2 != flag4) {
                this.poseLeftArm(ghost);
                this.poseRightArm(ghost);
            } else {
                this.poseRightArm(ghost);
                this.poseLeftArm(ghost);
            }
        }

        this.setupAttackAnimation(ghost, ageInTicks);
        this.body.xRot = 0.1745F;
        this.head.y = 2; // 19.0F; // 0
        this.body.y = 2; // 4.0F; // 2
        this.leftArm.y = 2; // 19.0F; // 0
        this.rightArm.y = 2; // 19.0F; // 0

        if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
//            AnimationUtils.bobModelPart(this.rightArm, ageInTicks, 1.0F);
        }

        if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
//            AnimationUtils.bobModelPart(this.leftArm, ageInTicks, -1.0F);
        }

        if (this.swimAmount > 0.0F) {
            float f5 = limbSwing % 26.0F;
            HumanoidArm humanoidarm = this.getAttackArm(ghost);
            float f1 = humanoidarm == HumanoidArm.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            float f2 = humanoidarm == HumanoidArm.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            if (!ghost.isUsingItem()) {
                if (f5 < 14.0F) {
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, 0.0F);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, 0.0F);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, (float) Math.PI + 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, (float) Math.PI - 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
                } else if (f5 >= 14.0F && f5 < 22.0F) {
                    float f6 = (f5 - 14.0F) / 8.0F;
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, ((float) Math.PI / 2F) * f6);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, ((float) Math.PI / 2F) * f6);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, 5.012389F - 1.8707964F * f6);
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, 1.2707963F + 1.8707964F * f6);
                } else if (f5 >= 22.0F && f5 < 26.0F) {
                    float f3 = (f5 - 22.0F) / 4.0F;
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f3);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f3);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, (float) Math.PI);
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, (float) Math.PI);
                }
            }
        }

        if (ghost.getTarget() == null) {
            AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
        }
    }

    private void poseRightArm(Ghost ghost) {
        switch (this.rightArmPose) {
            case EMPTY -> this.rightArm.yRot = 0.0F;
            case BLOCK -> {
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
                this.rightArm.yRot = (-(float) Math.PI / 6F);
            }
            case ITEM -> {
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
                this.rightArm.yRot = 0.0F;
            }
            case THROW_SPEAR -> {
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
                this.rightArm.yRot = 0.0F;
            }
            case BOW_AND_ARROW -> {
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            }
            case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, ghost, true);
            case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
            case SPYGLASS -> {
                this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (ghost.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
                this.rightArm.yRot = this.head.yRot - 0.2617994F;
            }
        }
    }

    private void poseLeftArm(Ghost ghost) {
        switch (this.leftArmPose) {
            case EMPTY -> this.leftArm.yRot = 0.0F;
            case BLOCK -> {
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
                this.leftArm.yRot = ((float) Math.PI / 6F);
            }
            case ITEM -> {
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
                this.leftArm.yRot = 0.0F;
            }
            case THROW_SPEAR -> {
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
                this.leftArm.yRot = 0.0F;
            }
            case BOW_AND_ARROW -> {
                this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
                this.leftArm.yRot = 0.1F + this.head.yRot;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            }
            case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, ghost, false);
            case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
            case SPYGLASS -> {
                this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (ghost.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
                this.leftArm.yRot = this.head.yRot + 0.2617994F;
            }
        }
    }

    protected HumanoidModel.ArmPose getArmPose(Ghost ghost, InteractionHand hand) {
        ItemStack stack = ghost.getItemInHand(hand);
        return stack.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
    }

    protected float rotlerpRad(float angle, float maxAngle, float mul) {
        float f = (mul - maxAngle) % ((float) Math.PI * 2F);
        if (f < -(float) Math.PI) {
            f += ((float) Math.PI * 2F);
        }

        if (f >= (float) Math.PI) {
            f -= ((float) Math.PI * 2F);
        }

        return maxAngle + angle * f;
    }

    private HumanoidArm getAttackArm(Ghost entity) {
        HumanoidArm humanoidarm = entity.getMainArm();
        return entity.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }

    private float quadraticArmUpdate(float limbSwing) {
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }

    protected void setupAttackAnimation(Ghost ghost, float ageInTicks) {
        if (!(this.attackTime <= 0.0F)) {
            HumanoidArm humanoidarm = this.getAttackArm(ghost);
            ModelPart modelpart = this.getArm(humanoidarm);
            float f = this.attackTime;
            this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
            if (humanoidarm == HumanoidArm.LEFT) {
                this.body.yRot *= -1.0F;
            }

            this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
            this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
            this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
            this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            //noinspection SuspiciousNameCombination
            this.leftArm.xRot += this.body.yRot;
            f = 1.0F - this.attackTime;
            f = f * f;
            f = f * f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            modelpart.xRot = (float) ((double) modelpart.xRot - ((double) f1 * 1.2D + (double) f2));
            modelpart.yRot += this.body.yRot * 2.0F;
            modelpart.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay);
        this.head.render(poseStack, buffer, packedLight, packedOverlay);
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay);
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void translateToHand(@Nonnull HumanoidArm side, @Nonnull PoseStack poseStack) {
//        poseStack.popPose();
        poseStack.scale(0.7f, 0.7f, 0.7f);
        poseStack.translate(-0.18 * (side == HumanoidArm.LEFT ? -1 : 1), 0.1, 0);
        this.getArm(side).translateAndRotate(poseStack);
//        poseStack.pushPose();
    }

    protected ModelPart getArm(HumanoidArm side) {
        return side == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    @Nonnull
    @Override
    public ModelPart getHead() {
        return this.head;
    }
}
