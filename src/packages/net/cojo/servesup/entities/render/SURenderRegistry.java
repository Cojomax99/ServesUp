package src.packages.net.cojo.servesup.entities.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import src.packages.net.cojo.servesup.entities.EntityVolleyball;
import src.packages.net.cojo.servesup.entities.model.ModelVolleyball;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class SURenderRegistry {

	public SURenderRegistry() {
		addMapping(EntityVolleyball.class, new RenderVolleyball(new ModelVolleyball()));
	}
	
	private static void addMapping(Class<? extends Entity> entityClass, Render render) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
    }

}
