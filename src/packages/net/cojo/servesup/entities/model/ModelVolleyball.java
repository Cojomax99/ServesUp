package src.packages.net.cojo.servesup.entities.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVolleyball extends ModelBase
{
	//fields
	ModelRenderer Ball;

	public ModelVolleyball()
	{
		textureWidth = 64;
		textureHeight = 32;

		Ball = new ModelRenderer(this, 0, 0);
		Ball.addBox(-3F, -3F, -3F, 6, 6, 6);
		Ball.setRotationPoint(0F, 0F, 0F);
		Ball.setTextureSize(64, 32);
		Ball.mirror = true;
		setRotation(Ball, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Ball.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}

}
