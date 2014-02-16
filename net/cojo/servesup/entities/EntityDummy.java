package net.cojo.servesup.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityDummy extends EntityLiving {

	public EntityDummy(World par1World) {
		super(par1World);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
	}

}
