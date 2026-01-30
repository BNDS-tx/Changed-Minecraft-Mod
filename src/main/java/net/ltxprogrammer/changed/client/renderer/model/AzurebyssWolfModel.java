package net.ltxprogrammer.changed.client.renderer.model;
// Made with Blockbench 5.0.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.entity.AzurebyssCreate;
import net.ltxprogrammer.changed.entity.beast.AzurebyssEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AzurebyssWolfModel extends AdvancedHumanoidModel<AzurebyssEntity> implements AzurebyssCreate {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Changed.modResource("textures/azurebyss_entity/customized_entity.png"), "main");
	private final ModelPart Root;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLowerLeg2;
	private final ModelPart LeftFoot2;
	private final ModelPart LeftPad2;
	private final ModelPart LeftPawBeans;
	private final ModelPart RightLeg;
	private final ModelPart RightLowerLeg2;
	private final ModelPart RightFoot2;
	private final ModelPart RightPad2;
	private final ModelPart RightPawBeans;
	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart RightEar2;
	private final ModelPart RightEarPivot2;
	private final ModelPart LeftEar2;
	private final ModelPart LeftEarPivot2;
	private final ModelPart Hair;
	private final ModelPart CheekFur2;
	private final ModelPart RightCheek2;
	private final ModelPart LeftCheek2;
	private final ModelPart Torso;
	private final ModelPart NeckFur;
	private final ModelPart Tail2;
	private final ModelPart TailPrimary2;
	private final ModelPart TailSecondary2;
	private final ModelPart TailTertiary2;
	private final ModelPart TailQuaternary;
	private final ModelPart TorsoFurSide;
	private final ModelPart Muscles;
	private final ModelPart Abs;
	private final ModelPart RightArm;
	private final ModelPart RightArmPawBeans;
	private final ModelPart RightArmFur;
	private final ModelPart RightHandFur2;
	private final ModelPart LeftArm;
	private final ModelPart LeftArmPawBeans;
	private final ModelPart LeftArmFurLower;
	private final ModelPart LeftHandFur2;
	private final HumanoidAnimator<AzurebyssEntity, AzurebyssWolfModel> animator;

	public AzurebyssWolfModel(ModelPart root) {
		super(root);
		this.Root = root.getChild("Root");
		this.LeftLeg = this.Root.getChild("LeftLeg");
		this.LeftLowerLeg2 = this.LeftLeg.getChild("LeftLowerLeg2");
		this.LeftFoot2 = this.LeftLowerLeg2.getChild("LeftFoot2");
		this.LeftPad2 = this.LeftFoot2.getChild("LeftPad2");
		this.LeftPawBeans = this.LeftPad2.getChild("LeftPawBeans");
		this.RightLeg = this.Root.getChild("RightLeg");
		this.RightLowerLeg2 = this.RightLeg.getChild("RightLowerLeg2");
		this.RightFoot2 = this.RightLowerLeg2.getChild("RightFoot2");
		this.RightPad2 = this.RightFoot2.getChild("RightPad2");
		this.RightPawBeans = this.RightPad2.getChild("RightPawBeans");
		this.Body = this.Root.getChild("Body");
		this.Head = this.Body.getChild("Head");
		this.RightEar2 = this.Head.getChild("RightEar2");
		this.RightEarPivot2 = this.RightEar2.getChild("RightEarPivot2");
		this.LeftEar2 = this.Head.getChild("LeftEar2");
		this.LeftEarPivot2 = this.LeftEar2.getChild("LeftEarPivot2");
		this.Hair = this.Head.getChild("Hair");
		this.CheekFur2 = this.Head.getChild("CheekFur2");
		this.RightCheek2 = this.CheekFur2.getChild("RightCheek2");
		this.LeftCheek2 = this.CheekFur2.getChild("LeftCheek2");
		this.Torso = this.Body.getChild("Torso");
		this.NeckFur = this.Torso.getChild("NeckFur");
		this.Tail2 = this.Torso.getChild("Tail2");
		this.TailPrimary2 = this.Tail2.getChild("TailPrimary2");
		this.TailSecondary2 = this.TailPrimary2.getChild("TailSecondary2");
		this.TailTertiary2 = this.TailSecondary2.getChild("TailTertiary2");
		this.TailQuaternary = this.TailTertiary2.getChild("TailQuaternary");
		this.TorsoFurSide = this.Torso.getChild("TorsoFurSide");
		this.Muscles = this.Torso.getChild("Muscles");
		this.Abs = this.Muscles.getChild("Abs");
		this.RightArm = this.Body.getChild("RightArm");
		this.RightArmPawBeans = this.RightArm.getChild("RightArmPawBeans");
		this.RightArmFur = this.RightArm.getChild("RightArmFur");
		this.RightHandFur2 = this.RightArmFur.getChild("RightHandFur2");
		this.LeftArm = this.Body.getChild("LeftArm");
		this.LeftArmPawBeans = this.LeftArm.getChild("LeftArmPawBeans");
		this.LeftArmFurLower = this.LeftArm.getChild("LeftArmFurLower");
		this.LeftHandFur2 = this.LeftArmFurLower.getChild("LeftHandFur2");

		animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
				.addPreset(AnimatorPresets.wolfLike(
						Head, LeftEar2, RightEar2,
						Torso, LeftArm, RightArm,
						Tail2, List.of(TailPrimary2, TailSecondary2, TailTertiary2),
						LeftLeg, LeftLowerLeg2, LeftFoot2, LeftFoot2.getChild("LeftPad2"), RightLeg, RightLowerLeg2, RightFoot2, RightFoot2.getChild("RightPad2")));
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LeftLeg = Root.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, -13.5F, 0.0F));

		PartDefinition LeftArmFur_r1 = LeftLeg.addOrReplaceChild("LeftArmFur_r1", CubeListBuilder.create().texOffs(65, -3).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.65F, 10.7F, 1.6F, -0.2031F, 0.505F, -0.0727F));

		PartDefinition LeftArmFur_r2 = LeftLeg.addOrReplaceChild("LeftArmFur_r2", CubeListBuilder.create().texOffs(65, -3).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.65F, 5.7F, -0.4F, 0.1459F, 0.6959F, 0.0246F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(32, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg2 = LeftLeg.addOrReplaceChild("LeftLowerLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg2.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot2 = LeftLowerLeg2.addOrReplaceChild("LeftFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot2.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(56, 11).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad2 = LeftFoot2.addOrReplaceChild("LeftPad2", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftPawBeans = LeftPad2.addOrReplaceChild("LeftPawBeans", CubeListBuilder.create().texOffs(11, 93).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(11, 89).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(11, 87).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(11, 91).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(-1.0F, -8.5F, -0.05F));

		PartDefinition RightLeg = Root.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, -13.5F, 0.0F));

		PartDefinition fur_r1 = RightLeg.addOrReplaceChild("fur_r1", CubeListBuilder.create().texOffs(65, -3).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.65F, 10.7F, 1.6F, -0.2477F, -0.5839F, 0.1131F));

		PartDefinition fur_r2 = RightLeg.addOrReplaceChild("fur_r2", CubeListBuilder.create().texOffs(65, -3).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.65F, 5.7F, -0.4F, 0.1459F, -0.6959F, -0.0246F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(32, 44).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg2 = RightLeg.addOrReplaceChild("RightLowerLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r1 = RightLowerLeg2.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot2 = RightLowerLeg2.addOrReplaceChild("RightFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot2.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(13, 57).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad2 = RightFoot2.addOrReplaceChild("RightPad2", CubeListBuilder.create().texOffs(52, 32).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition RightPawBeans = RightPad2.addOrReplaceChild("RightPawBeans", CubeListBuilder.create().texOffs(19, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(19, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(19, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(19, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(1.0F, -8.5F, -0.05F));

		PartDefinition Body = Root.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(15, 32).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 22).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.5F, 0.0F));

		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition RightEar2 = Head.addOrReplaceChild("RightEar2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -7.7F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition RightEarFur_r1 = RightEar2.addOrReplaceChild("RightEarFur_r1", CubeListBuilder.create().texOffs(86, 35).mirror().addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1F, -0.1F, -1.25F, -0.0027F, -0.0526F, -0.6979F));

		PartDefinition RightEarPivot2 = RightEar2.addOrReplaceChild("RightEarPivot2", CubeListBuilder.create().texOffs(0, 4).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(0, 16).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(32, 22).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(24, 0).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0501F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar2 = Head.addOrReplaceChild("LeftEar2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -7.7F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition LeftEarFur_r1 = LeftEar2.addOrReplaceChild("LeftEarFur_r1", CubeListBuilder.create().texOffs(92, 35).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, -0.1F, -1.25F, -0.0027F, 0.0526F, 0.6979F));

		PartDefinition LeftEarPivot2 = LeftEar2.addOrReplaceChild("LeftEarPivot2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(0, 20).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(32, 24).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(0, 32).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0501F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
		.texOffs(24, 8).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition CheekFur2 = Head.addOrReplaceChild("CheekFur2", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -2.0F));

		PartDefinition RightCheek2 = CheekFur2.addOrReplaceChild("RightCheek2", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = RightCheek2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(84, 36).mirror().addBox(-0.45F, -1.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.85F, -5.5F, -0.8F, 0.5995F, -0.2317F, -0.3404F));

		PartDefinition cube_r2 = RightCheek2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(84, 42).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.85F, -2.0F, -0.8F, -0.1047F, -0.4451F, 0.0F));

		PartDefinition cube_r3 = RightCheek2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(84, 40).mirror().addBox(-0.45F, -3.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.95F, -1.9F, 0.5F, 0.1309F, -0.3403F, 0.0F));

		PartDefinition LeftCheek2 = CheekFur2.addOrReplaceChild("LeftCheek2", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = LeftCheek2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(90, 36).addBox(0.45F, -1.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.85F, -5.5F, -0.8F, 0.5995F, 0.2317F, 0.3404F));

		PartDefinition cube_r5 = LeftCheek2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(90, 40).addBox(0.45F, -3.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.95F, -1.9F, 0.5F, 0.1309F, 0.3403F, 0.0F));

		PartDefinition cube_r6 = LeftCheek2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(90, 42).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.85F, -2.0F, -0.8F, -0.1047F, 0.4451F, 0.0F));

		PartDefinition Torso = Body.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 33).addBox(-3.5F, 4.5F, -2.0F, 7.0F, 7.5F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(64, 55).addBox(-3.5F, 5.6F, -2.0F, 7.0F, 6.4F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(45, 67).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.4F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -24.5F, 0.0F));

		PartDefinition NeckFur = Torso.addOrReplaceChild("NeckFur", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.5F));

		PartDefinition NeckFur_r1 = NeckFur.addOrReplaceChild("NeckFur_r1", CubeListBuilder.create().texOffs(11, 71).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.75F, -1.9F, 0.2618F, 0.0F, 0.0F));

		PartDefinition NeckFur_r2 = NeckFur.addOrReplaceChild("NeckFur_r2", CubeListBuilder.create().texOffs(11, 66).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.5F, -2.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Tail2 = Torso.addOrReplaceChild("Tail2", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.4F));

		PartDefinition TailPrimary2 = Tail2.addOrReplaceChild("TailPrimary2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary2.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(48, 50).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F))
		.texOffs(10, 76).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition TailSecondary2 = TailPrimary2.addOrReplaceChild("TailSecondary2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

		PartDefinition Base_r2 = TailSecondary2.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(27, 55).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.7F))
		.texOffs(43, 83).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition TailTertiary2 = TailSecondary2.addOrReplaceChild("TailTertiary2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

		PartDefinition Base_r3 = TailTertiary2.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F))
		.texOffs(63, 88).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

		PartDefinition TailQuaternary = TailTertiary2.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition Base_r4 = TailQuaternary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(48, 59).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F))
		.texOffs(65, 80).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

		PartDefinition TorsoFurSide = Torso.addOrReplaceChild("TorsoFurSide", CubeListBuilder.create(), PartPose.offset(-4.45F, 6.5F, -1.1358F));

		PartDefinition FurLeft_r1 = TorsoFurSide.addOrReplaceChild("FurLeft_r1", CubeListBuilder.create().texOffs(90, 48).addBox(-1.0F, -4.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.4F, 5.0F, -1.3642F, 0.0F, 0.8419F, 0.0F));

		PartDefinition FurLeft_r2 = TorsoFurSide.addOrReplaceChild("FurLeft_r2", CubeListBuilder.create().texOffs(90, 45).addBox(0.0F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.4F, 0.0F, 0.0F, 0.1873F, 0.8129F, 0.0305F));

		PartDefinition FurRight_r1 = TorsoFurSide.addOrReplaceChild("FurRight_r1", CubeListBuilder.create().texOffs(84, 48).mirror().addBox(1.0F, -4.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 5.0F, -1.3642F, 0.0F, -0.8419F, 0.0F));

		PartDefinition FurRight_r2 = TorsoFurSide.addOrReplaceChild("FurRight_r2", CubeListBuilder.create().texOffs(84, 45).mirror().addBox(0.0F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.1873F, -0.8129F, -0.0305F));

		PartDefinition Muscles = Torso.addOrReplaceChild("Muscles", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.25F));

		PartDefinition cube_r7 = Muscles.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(54, 23).mirror().addBox(0.3F, -1.0F, 0.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-0.15F, 1.25F, -2.25F, -0.0436F, -0.0087F, 0.0004F));

		PartDefinition cube_r8 = Muscles.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(54, 23).addBox(-4.0F, -1.0F, 0.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-0.15F, 1.25F, -2.25F, -0.0436F, 0.0087F, -0.0004F));

		PartDefinition Abs = Muscles.addOrReplaceChild("Abs", CubeListBuilder.create(), PartPose.offset(0.0F, 4.3F, 0.2F));

		PartDefinition cube_r9 = Abs.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(31, 41).addBox(-2.25F, 1.0F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.175F)), PartPose.offsetAndRotation(-0.05F, 2.45F, -2.225F, 0.0F, 0.0087F, 0.0F));

		PartDefinition cube_r10 = Abs.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(34, 41).addBox(0.25F, 1.0F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.175F)), PartPose.offsetAndRotation(0.05F, 2.45F, -2.225F, 0.0F, -0.0087F, 0.0F));

		PartDefinition cube_r11 = Abs.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(60, 27).addBox(-3.25F, -0.5F, 0.2F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-0.05F, 2.45F, -2.2F, 0.0F, 0.0087F, 0.0F));

		PartDefinition cube_r12 = Abs.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(60, 27).mirror().addBox(0.25F, -0.5F, 0.2F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(0.05F, 2.45F, -2.2F, 0.0F, -0.0087F, 0.0F));

		PartDefinition cube_r13 = Abs.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(54, 24).addBox(-3.25F, -2.0F, 0.2F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.225F)), PartPose.offsetAndRotation(-0.05F, 2.35F, -2.45F, 0.0F, 0.0087F, 0.0F));

		PartDefinition cube_r14 = Abs.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(54, 24).mirror().addBox(0.25F, -2.0F, 0.2F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.225F)).mirror(false), PartPose.offsetAndRotation(0.05F, 2.35F, -2.45F, 0.0F, -0.0087F, 0.0F));

		PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(-5.0F, -22.5F, 0.0F));

		PartDefinition fur_r3 = RightArm.addOrReplaceChild("fur_r3", CubeListBuilder.create().texOffs(65, -3).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.15F, 0.7F, -0.4F, 0.1459F, -0.6959F, -0.0246F));

		PartDefinition RightArmPawBeans = RightArm.addOrReplaceChild("RightArmPawBeans", CubeListBuilder.create().texOffs(27, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(27, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(27, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(27, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition RightArmFur = RightArm.addOrReplaceChild("RightArmFur", CubeListBuilder.create(), PartPose.offset(-2.0F, 7.5F, -2.4F));

		PartDefinition RightHandFur2 = RightArmFur.addOrReplaceChild("RightHandFur2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.1F));

		PartDefinition RightArmFur_r1 = RightHandFur2.addOrReplaceChild("RightArmFur_r1", CubeListBuilder.create().texOffs(0, 91).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(3.3F, 0.0F, 3.3F, 0.0F, -1.5708F, 0.0873F));

		PartDefinition RightArmFur_r2 = RightHandFur2.addOrReplaceChild("RightArmFur_r2", CubeListBuilder.create().texOffs(0, 86).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-1.3F, 0.0F, 3.3F, 0.0F, 1.5708F, -0.0873F));

		PartDefinition RightArmFur_r3 = RightHandFur2.addOrReplaceChild("RightArmFur_r3", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.6F, -0.0873F, 0.0F, 0.0F));

		PartDefinition RightArmFur_r4 = RightHandFur2.addOrReplaceChild("RightArmFur_r4", CubeListBuilder.create().texOffs(0, 76).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, -22.5F, 0.0F));

		PartDefinition LeftArmFur_r3 = LeftArm.addOrReplaceChild("LeftArmFur_r3", CubeListBuilder.create().texOffs(65, -3).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.15F, 0.7F, -0.4F, 0.1459F, 0.6959F, 0.0246F));

		PartDefinition LeftArmPawBeans = LeftArm.addOrReplaceChild("LeftArmPawBeans", CubeListBuilder.create().texOffs(35, 93).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(35, 89).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(35, 87).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(35, 91).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition LeftArmFurLower = LeftArm.addOrReplaceChild("LeftArmFurLower", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftHandFur2 = LeftArmFurLower.addOrReplaceChild("LeftHandFur2", CubeListBuilder.create(), PartPose.offset(2.0F, 7.5F, -2.3F));

		PartDefinition LeftArmFur_r4 = LeftHandFur2.addOrReplaceChild("LeftArmFur_r4", CubeListBuilder.create().texOffs(0, 71).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-3.3F, 0.0F, 3.3F, 0.0F, 1.5708F, -0.0873F));

		PartDefinition LeftArmFur_r5 = LeftHandFur2.addOrReplaceChild("LeftArmFur_r5", CubeListBuilder.create().texOffs(0, 66).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(1.3F, 0.0F, 3.3F, 0.0F, -1.5708F, 0.0873F));

		PartDefinition LeftArmFur_r6 = LeftHandFur2.addOrReplaceChild("LeftArmFur_r6", CubeListBuilder.create().texOffs(0, 61).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.6F, -0.0873F, 0.0F, 0.0F));

		PartDefinition LeftArmFur_r7 = LeftHandFur2.addOrReplaceChild("LeftArmFur_r7", CubeListBuilder.create().texOffs(0, 56).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

	public ModelPart getArm(HumanoidArm side) {
		return side == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
	}

	public ModelPart getLeg(HumanoidArm side) {
		return side == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
	}

	public ModelPart getHead() {
		return this.Head;
	}

	public ModelPart getTorso() {
		return Torso;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public HumanoidAnimator<AzurebyssEntity, AzurebyssWolfModel> getAnimator(AzurebyssEntity entity) {
		return animator;
	}
}