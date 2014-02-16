package net.cojo.servesup.entities.render;

import net.cojo.servesup.ModUtil;
import net.cojo.servesup.entities.EntityDummy;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderDummy extends RenderBiped {

	public RenderDummy(ModelBiped par1ModelBiped, float par2) {
		super(par1ModelBiped, par2);
	}

	public RenderDummy(ModelBiped par1ModelBiped, float par2, float par3) {
		super(par1ModelBiped, par2, par3);
	}
	
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderLiving((EntityDummy)par1Entity, par2, par4, par6, par8, par9);
    }

    
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return ModUtil.bindTextureEntity("dummy");
	}
}
