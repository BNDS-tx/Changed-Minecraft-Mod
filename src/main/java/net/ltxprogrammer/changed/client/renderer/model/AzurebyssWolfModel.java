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

public class AzurebyssWolfModel extends AdvancedHumanoidModel<AzurebyssEntity> implements AdvancedHumanoidModelInterface<AzurebyssEntity, AzurebyssWolfModel>, AzurebyssCreate {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Changed.modResource("textures/azurebyss_entity/customized_entity.png"), "main");
	private final ModelPart Head;
	private final ModelPart RightEar;
	private final ModelPart RightEarPivot2;
	private final ModelPart LeftEar;
	private final ModelPart LeftEarPivot2;
	private final ModelPart Hair;
	private final ModelPart CheekFur;
	private final ModelPart RightCheek;
	private final ModelPart LeftCheek;
	private final ModelPart Torso;
	private final ModelPart NeckFur;
	private final ModelPart Tail;
	private final ModelPart TailPrimary;
	private final ModelPart TailSecondary;
	private final ModelPart TailTertiary;
	private final ModelPart TailQuaternary;
	private final ModelPart TorsoFurSide;
	private final ModelPart Muscles;
	private final ModelPart Abs;
	private final ModelPart RightArm;
	private final ModelPart RightArmPawBeans;
	private final ModelPart RightArmFur;
	private final ModelPart RightHandFur;
	private final ModelPart LeftArm;
	private final ModelPart LeftArmPawBeans;
	private final ModelPart LeftArmFurLower;
	private final ModelPart LeftHandFur;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLowerLeg;
	private final ModelPart LeftFoot;
	private final ModelPart LeftPad;
	private final ModelPart LeftPawBeans;
	private final ModelPart RightLeg;
	private final ModelPart RightLowerLeg;
	private final ModelPart RightFoot;
	private final ModelPart RightPad;
	private final ModelPart RightPawBeans;
	private final HumanoidAnimator<AzurebyssEntity, AzurebyssWolfModel> animator;

	public AzurebyssWolfModel(ModelPart root) {
		super(root);
		this.Head = root.getChild("Head");
		this.RightEar = this.Head.getChild("RightEar");
		this.RightEarPivot2 = this.RightEar.getChild("RightEarPivot2");
		this.LeftEar = this.Head.getChild("LeftEar");
		this.LeftEarPivot2 = this.LeftEar.getChild("LeftEarPivot2");
		this.Hair = this.Head.getChild("Hair");
		this.CheekFur = this.Head.getChild("CheekFur");
		this.RightCheek = this.CheekFur.getChild("RightCheek");
		this.LeftCheek = this.CheekFur.getChild("LeftCheek");
		this.Torso = root.getChild("Torso");
		this.NeckFur = this.Torso.getChild("NeckFur");
		this.Tail = this.Torso.getChild("Tail");
		this.TailPrimary = this.Tail.getChild("TailPrimary");
		this.TailSecondary = this.TailPrimary.getChild("TailSecondary");
		this.TailTertiary = this.TailSecondary.getChild("TailTertiary");
		this.TailQuaternary = this.TailTertiary.getChild("TailQuaternary");
		this.TorsoFurSide = this.Torso.getChild("TorsoFurSide");
		this.Muscles = this.Torso.getChild("Muscles");
		this.Abs = this.Muscles.getChild("Abs");
		this.RightArm = root.getChild("RightArm");
		this.RightArmPawBeans = this.RightArm.getChild("RightArmPawBeans");
		this.RightArmFur = this.RightArm.getChild("RightArmFur");
		this.RightHandFur = this.RightArmFur.getChild("RightHandFur");
		this.LeftArm = root.getChild("LeftArm");
		this.LeftArmPawBeans = this.LeftArm.getChild("LeftArmPawBeans");
		this.LeftArmFurLower = this.LeftArm.getChild("LeftArmFurLower");
		this.LeftHandFur = this.LeftArmFurLower.getChild("LeftHandFur");
		this.LeftLeg = root.getChild("LeftLeg");
		this.LeftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
		this.LeftFoot = this.LeftLowerLeg.getChild("LeftFoot");
		this.LeftPad = this.LeftFoot.getChild("LeftPad");
		this.LeftPawBeans = this.LeftPad.getChild("LeftPawBeans");
		this.RightLeg = root.getChild("RightLeg");
		this.RightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
		this.RightFoot = this.RightLowerLeg.getChild("RightFoot");
		this.RightPad = this.RightFoot.getChild("RightPad");
		this.RightPawBeans = this.RightPad.getChild("RightPawBeans");

		animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
				.addPreset(AnimatorPresets.wolfLike(
						Head, LeftEar, RightEar,
						Torso, LeftArm, RightArm,
						Tail, List.of(TailPrimary, TailSecondary, TailTertiary),
						LeftLeg, LeftLowerLeg, LeftFoot, LeftFoot.getChild("LeftPad"), RightLeg, RightLowerLeg, RightFoot, RightFoot.getChild("RightPad")));
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(15, 32).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(24, 22).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -7.7F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition RightEarFur_r1 = RightEar.addOrReplaceChild("RightEarFur_r1", CubeListBuilder.create().texOffs(86, 35).mirror().addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1F, -0.1F, -1.25F, -0.0027F, -0.0526F, -0.6979F));

		PartDefinition RightEarPivot2 = RightEar.addOrReplaceChild("RightEarPivot2", CubeListBuilder.create().texOffs(0, 4).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
				.texOffs(0, 16).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
				.texOffs(32, 22).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
				.texOffs(24, 0).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0501F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -7.7F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition LeftEarFur_r1 = LeftEar.addOrReplaceChild("LeftEarFur_r1", CubeListBuilder.create().texOffs(92, 35).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, -0.1F, -1.25F, -0.0027F, 0.0526F, 0.6979F));

		PartDefinition LeftEarPivot2 = LeftEar.addOrReplaceChild("LeftEarPivot2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
				.texOffs(0, 20).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
				.texOffs(32, 24).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
				.texOffs(0, 32).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0501F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
				.texOffs(24, 8).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition CheekFur = Head.addOrReplaceChild("CheekFur", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -2.0F));

		PartDefinition RightCheek = CheekFur.addOrReplaceChild("RightCheek", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = RightCheek.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(84, 36).mirror().addBox(-0.45F, -1.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.85F, -5.5F, -0.8F, 0.5995F, -0.2317F, -0.3404F));

		PartDefinition cube_r2 = RightCheek.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(84, 42).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.85F, -2.0F, -0.8F, -0.1047F, -0.4451F, 0.0F));

		PartDefinition cube_r3 = RightCheek.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(84, 40).mirror().addBox(-0.45F, -3.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.95F, -1.9F, 0.5F, 0.1309F, -0.3403F, 0.0F));

		PartDefinition LeftCheek = CheekFur.addOrReplaceChild("LeftCheek", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = LeftCheek.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(90, 36).addBox(0.45F, -1.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.85F, -5.5F, -0.8F, 0.5995F, 0.2317F, 0.3404F));

		PartDefinition cube_r5 = LeftCheek.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(90, 40).addBox(0.45F, -3.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.95F, -1.9F, 0.5F, 0.1309F, 0.3403F, 0.0F));

		PartDefinition cube_r6 = LeftCheek.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(90, 42).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.85F, -2.0F, -0.8F, -0.1047F, 0.4451F, 0.0F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 33).addBox(-3.5F, 4.5F, -2.0F, 7.0F, 7.5F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(64, 55).addBox(-3.5F, 5.6F, -2.0F, 7.0F, 6.4F, 4.0F, new CubeDeformation(0.1F))
				.texOffs(45, 67).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.4F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition NeckFur = Torso.addOrReplaceChild("NeckFur", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.5F));

		PartDefinition NeckFur_r1 = NeckFur.addOrReplaceChild("NeckFur_r1", CubeListBuilder.create().texOffs(11, 71).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.75F, -1.9F, 0.2618F, 0.0F, 0.0F));

		PartDefinition NeckFur_r2 = NeckFur.addOrReplaceChild("NeckFur_r2", CubeListBuilder.create().texOffs(11, 66).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.5F, -2.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.4F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(48, 50).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F))
				.texOffs(10, 76).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

		PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(27, 55).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.7F))
				.texOffs(43, 83).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

		PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F))
				.texOffs(63, 88).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

		PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

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

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(80, 0).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(-5.0F, 1.5F, 0.0F));

		PartDefinition fur_r1 = RightArm.addOrReplaceChild("fur_r1", CubeListBuilder.create().texOffs(65, -3).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.15F, 0.7F, -0.4F, 0.1459F, -0.6959F, -0.0246F));

		PartDefinition RightArmPawBeans = RightArm.addOrReplaceChild("RightArmPawBeans", CubeListBuilder.create().texOffs(27, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
				.texOffs(27, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
				.texOffs(27, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
				.texOffs(27, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition RightArmFur = RightArm.addOrReplaceChild("RightArmFur", CubeListBuilder.create(), PartPose.offset(-2.0F, 7.5F, -2.4F));

		PartDefinition RightHandFur = RightArmFur.addOrReplaceChild("RightHandFur", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.1F));

		PartDefinition RightArmFur_r1 = RightHandFur.addOrReplaceChild("RightArmFur_r1", CubeListBuilder.create().texOffs(0, 91).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(3.3F, 0.0F, 3.3F, 0.0F, -1.5708F, 0.0873F));

		PartDefinition RightArmFur_r2 = RightHandFur.addOrReplaceChild("RightArmFur_r2", CubeListBuilder.create().texOffs(0, 86).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-1.3F, 0.0F, 3.3F, 0.0F, 1.5708F, -0.0873F));

		PartDefinition RightArmFur_r3 = RightHandFur.addOrReplaceChild("RightArmFur_r3", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.6F, -0.0873F, 0.0F, 0.0F));

		PartDefinition RightArmFur_r4 = RightHandFur.addOrReplaceChild("RightArmFur_r4", CubeListBuilder.create().texOffs(0, 76).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(80, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, 1.5F, 0.0F));

		PartDefinition LeftArmFur_r1 = LeftArm.addOrReplaceChild("LeftArmFur_r1", CubeListBuilder.create().texOffs(65, -3).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.15F, 0.7F, -0.4F, 0.1459F, 0.6959F, 0.0246F));

		PartDefinition LeftArmPawBeans = LeftArm.addOrReplaceChild("LeftArmPawBeans", CubeListBuilder.create().texOffs(35, 93).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
				.texOffs(35, 89).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
				.texOffs(35, 87).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
				.texOffs(35, 91).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition LeftArmFurLower = LeftArm.addOrReplaceChild("LeftArmFurLower", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftHandFur = LeftArmFurLower.addOrReplaceChild("LeftHandFur", CubeListBuilder.create(), PartPose.offset(2.0F, 7.5F, -2.3F));

		PartDefinition LeftArmFur_r2 = LeftHandFur.addOrReplaceChild("LeftArmFur_r2", CubeListBuilder.create().texOffs(0, 71).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-3.3F, 0.0F, 3.3F, 0.0F, 1.5708F, -0.0873F));

		PartDefinition LeftArmFur_r3 = LeftHandFur.addOrReplaceChild("LeftArmFur_r3", CubeListBuilder.create().texOffs(0, 66).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(1.3F, 0.0F, 3.3F, 0.0F, -1.5708F, 0.0873F));

		PartDefinition LeftArmFur_r4 = LeftHandFur.addOrReplaceChild("LeftArmFur_r4", CubeListBuilder.create().texOffs(0, 61).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.6F, -0.0873F, 0.0F, 0.0F));

		PartDefinition LeftArmFur_r5 = LeftHandFur.addOrReplaceChild("LeftArmFur_r5", CubeListBuilder.create().texOffs(0, 56).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

		PartDefinition LeftArmFur_r6 = LeftLeg.addOrReplaceChild("LeftArmFur_r6", CubeListBuilder.create().texOffs(65, -3).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.65F, 10.7F, 1.6F, -0.2031F, 0.505F, -0.0727F));

		PartDefinition LeftArmFur_r7 = LeftLeg.addOrReplaceChild("LeftArmFur_r7", CubeListBuilder.create().texOffs(65, -3).addBox(0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.65F, 5.7F, -0.4F, 0.1459F, 0.6959F, 0.0246F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(32, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(56, 11).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftPawBeans = LeftPad.addOrReplaceChild("LeftPawBeans", CubeListBuilder.create().texOffs(11, 93).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
				.texOffs(11, 89).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
				.texOffs(11, 87).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
				.texOffs(11, 91).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(-1.0F, -8.5F, -0.05F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

		PartDefinition fur_r2 = RightLeg.addOrReplaceChild("fur_r2", CubeListBuilder.create().texOffs(65, -3).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.65F, 10.7F, 1.6F, -0.2477F, -0.5839F, 0.1131F));

		PartDefinition fur_r3 = RightLeg.addOrReplaceChild("fur_r3", CubeListBuilder.create().texOffs(65, -3).mirror().addBox(-0.45F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.65F, 5.7F, -0.4F, 0.1459F, -0.6959F, -0.0246F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(32, 44).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(13, 57).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(52, 32).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition RightPawBeans = RightPad.addOrReplaceChild("RightPawBeans", CubeListBuilder.create().texOffs(19, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
				.texOffs(19, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
				.texOffs(19, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
				.texOffs(19, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(1.0F, -8.5F, -0.05F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

	@Override
	public void prepareMobModel(@NotNull AzurebyssEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		// Let the animator handle prepareMobModel()
		this.prepareMobModel(animator, entity, limbSwing, limbSwingAmount, partialTicks);
	}

	public void setupHand(AzurebyssEntity entity) {
		animator.setupHand();
	}

	@Override
	public void setupAnim(@NotNull AzurebyssEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
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