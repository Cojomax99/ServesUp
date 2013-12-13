package src.packages.net.cojo.servesup.entities.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import src.packages.net.cojo.servesup.ModUtil;
import src.packages.net.cojo.servesup.entities.model.ModelVolleyball;

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
