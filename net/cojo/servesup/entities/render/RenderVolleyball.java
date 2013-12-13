package net.cojo.servesup.entities.render;

import net.cojo.servesup.ModUtil;
import net.cojo.servesup.entities.model.ModelVolleyball;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderVolleyball extends Render {

	/** Volleyball model */
	public ModelVolleyball model;
	
	public RenderVolleyball(ModelVolleyball model) {
		this.model = model;
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {

	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return ModUtil.bindTextureEntity("volleyball");
	}

}
