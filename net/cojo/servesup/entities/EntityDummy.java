package net.cojo.servesup.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityDummy extends EntityLivingBase {

	public EntityDummy(World par1World) {
		super(par1World);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack getHeldItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getCurrentItemOrArmor(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack[] getLastActiveItems() {
		// TODO Auto-generated method stub
		return null;
	}

}
