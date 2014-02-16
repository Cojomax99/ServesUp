package net.cojo.servesup.entities.render;

import net.cojo.servesup.entities.EntityDummy;
import net.cojo.servesup.entities.EntityVolleyball;
import net.cojo.servesup.entities.model.ModelDummy;
import net.cojo.servesup.entities.model.ModelVolleyball;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class SURenderRegistry {

	public static void init() {
		addMapping(EntityVolleyball.class, new RenderVolleyball(new ModelVolleyball()));
		addMapping(EntityDummy.class, new RenderDummy(new ModelDummy(), 0.75F));
	}
	
	private static void addMapping(Class<? extends Entity> entityClass, Render render) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
    }

}
