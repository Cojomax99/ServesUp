package net.cojo.servesup.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityVolleyball extends Entity implements
		IEntityAdditionalSpawnData, IProjectile {

	public EntityVolleyball(World par1World) {
		super(par1World);
	    this.setSize(0.25F, 0.25F);
	}
	
	@Override
	protected void entityInit() {

	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {

	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

	}

	@Override
	public void setThrowableHeading(double dx, double dy, double dz, float f, float f1) {
		
		
	}

}
