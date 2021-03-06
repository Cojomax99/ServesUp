package net.cojo.servesup.entities.render;

import net.cojo.servesup.ModUtil;
import net.cojo.servesup.entities.EntityVolleyball;
import net.cojo.servesup.entities.model.ModelVolleyball;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderVolleyball extends Render {

	/** Volleyball model */
	public ModelVolleyball model;
	
	public RenderVolleyball(ModelVolleyball model) {
		this.model = model;
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		EntityVolleyball vball = (EntityVolleyball)entity;
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) d0, (float) d1 + .3125F, (float) d2);
		ModUtil.bindTextureEntity("volleyball");
		this.model.render(vball, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return ModUtil.bindTextureEntity("volleyball");
	}

}
