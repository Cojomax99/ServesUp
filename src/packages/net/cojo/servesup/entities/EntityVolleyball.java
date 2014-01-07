package src.packages.net.cojo.servesup.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityVolleyball extends Entity implements
IEntityAdditionalSpawnData, IProjectile {

	/** Player who hit the volleyball */
	public EntityLivingBase hitter;

	/** Name of the player who hit the volleyball */
	public String hitterName;

	public EntityVolleyball(World par1World, EntityLivingBase player) {
		super(par1World);
		this.setSize(0.25F, 0.25F);
		this.hitter = player;
	}
	
	public EntityVolleyball(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	/**
	 * Set the player who most previously hit the ball
	 * @param nPlayer Player who hit the ball
	 */
	public void setPlayer(EntityLivingBase nPlayer) {
		this.hitter = nPlayer;
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
		this.hitterName = nbttagcompound.getString("ownerName");

		if (this.hitterName != null && this.hitterName.length() == 0)
		{
			this.hitterName = null;
		}	
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		if ((this.hitterName == null || this.hitterName.length() == 0) && this.hitter != null && this.hitter instanceof EntityPlayer)
		{
			this.hitterName = this.hitter.getEntityName();
		}

		nbttagcompound.setString("ownerName", this.hitterName == null ? "" : this.hitterName);
	}

	@Override
	public void setThrowableHeading(double dx, double dy, double dz, float f, float f1) {

	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity()
	{
		return 0.03F;
	}

	/**
	 * Get an instance of the player who last hit the ball
	 * @return
	 */
	public EntityLivingBase getHitter()
	{
		if (this.hitter == null && this.hitterName != null && this.hitterName.length() > 0)
		{
			this.hitter = this.worldObj.getPlayerEntityByName(this.hitterName);
		}

		return this.hitter;
	}

}
