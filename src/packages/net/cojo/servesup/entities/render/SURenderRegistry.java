package net.cojo.servesup.entities.render;

import net.cojo.servesup.entities.EntityVolleyball;
import net.cojo.servesup.entities.model.ModelVolleyball;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class SURenderRegistry {

	public SURenderRegistry() {
		addMapping(EntityVolleyball.class, new RenderVolleyball(new ModelVolleyball()));
	}
	
	private static void addMapping(Class<? extends Entity> entityClass, Render render) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
    }

}
