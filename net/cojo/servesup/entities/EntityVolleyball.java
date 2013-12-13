package net.cojo.servesup.entities;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityVolleyball extends Entity implements
		IEntityAdditionalSpawnData {

	public EntityVolleyball(World par1World) {
		super(par1World);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub

	}

}
