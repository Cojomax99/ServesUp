package net.cojo.servesup.entities;

import java.util.List;

import net.cojo.servesup.GameStates;
import net.cojo.servesup.physics.BallPhysicsHelper;
import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityVolleyball extends Entity implements IEntityAdditionalSpawnData, IProjectile {

	/** Maximum number of hits one team can have in a row before a point is given to the other team */
	public static final int MAX_SUCCESSIVE_HITS = 3;
	
	/** Current number of successive hits by one team */
	public int hitCount;
	
	/** Which team last hit the ball? */
	public int lastHitTeam;
	
	/** Player who hit the volleyball */
	public EntityLivingBase hitter;

	/** Name of the player who hit the volleyball */
	public String hitterName;

	/** Is this entity in contact with the ground? */
	public boolean inGround;

	/** Is this ball on a serve? */
	public boolean isServe;

	/** Number of ticks the ball has been on the ground */
	private int ticksInGround;

	/** Number of ticks the ball has been in the air */
	private int ticksInAir = 0;

	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;

	public int courtX = 0, courtY = 0, courtZ = 0;

	public EntityVolleyball(World par1World, EntityLivingBase player, boolean isServe, int courtX, int courtY, int courtZ) {
		super(par1World);
		this.setSize(0.25F, 0.25F);
		this.hitter = player;
		this.isServe = isServe;
		this.courtX = courtX;
		this.courtY = courtY;
		this.courtZ = courtZ;
		this.hitCount = 0;
	}

	public EntityVolleyball(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.hitCount = 0;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
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
		data.writeInt(courtX);
		data.writeInt(courtY);
		data.writeInt(courtZ);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		courtX = data.readInt();
		courtY = data.readInt();
		courtZ = data.readInt();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.hitterName = nbttagcompound.getString("ownerName");

		if (this.hitterName != null && this.hitterName.length() == 0) {
			this.hitterName = null;
		}

		this.courtX = nbttagcompound.getInteger("courtX");
		this.courtY = nbttagcompound.getInteger("courtY");
		this.courtZ = nbttagcompound.getInteger("courtZ");
		
		this.hitCount = nbttagcompound.getInteger("hitCount");
		this.lastHitTeam = nbttagcompound.getInteger("lastHitTeam");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		if ((this.hitterName == null || this.hitterName.length() == 0) && this.hitter != null && this.hitter instanceof EntityPlayer) {
			this.hitterName = this.hitter.getEntityName();
		}

		nbttagcompound.setString("ownerName", this.hitterName == null ? "" : this.hitterName);
		nbttagcompound.setInteger("courtX", courtX);
		nbttagcompound.setInteger("courtY", courtY);
		nbttagcompound.setInteger("courtZ", courtZ);
		nbttagcompound.setInteger("hitCount", hitCount);
		nbttagcompound.setInteger("lastHitTeam", lastHitTeam);
	}

	@Override
	public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
		float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
		par1 /= (double)f2;
		par3 /= (double)f2;
		par5 /= (double)f2;
		par1 += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
		par3 += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
		par5 += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
		par1 *= (double)par7;
		par3 *= (double)par7;
		par5 *= (double)par7;
		this.motionX = par1;
		this.motionY = par3;

		if (isServe)
			this.motionY = 2;

		System.out.println("set throwable heading");

		this.motionZ = par5;
		float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)f3) * 180.0D / Math.PI);
		//     this.ticksInGround = 0;
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity() {
		return 0.03F;
	}

	/**
	 * Get an instance of the player who last hit the ball
	 * @return
	 */
	public EntityLivingBase getHitter()	{
		if (this.hitter == null && this.hitterName != null && this.hitterName.length() > 0)	{
			this.hitter = this.worldObj.getPlayerEntityByName(this.hitterName);
		}

		return this.hitter;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;

		super.onUpdate();
		//System.out.println("return");

		Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
		Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.clip(vec3, vec31);
		vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
		vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);



		if (movingobjectposition != null) {
			vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}

		if (!this.worldObj.isRemote) {
			float triggerDist = 4.0F;
			Entity entity = null;
			// distance beyond entity to check
			double size = 1.0D;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(size, size, size));
			double d0 = 0.0D;
			EntityLivingBase entitylivingbase = this.getHitter();
			float swingTrigger = 0.2F;

			for (int j = 0; j < list.size(); ++j)
			{
				Entity entity1 = (Entity)list.get(j);

				if (entity1 != null && !entity1.isDead && entity1.canBeCollidedWith())
				{
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (entity1 instanceof EntityLivingBase) {
						EntityLivingBase player = (EntityLivingBase)entity1;

						if (isServe) {
							BallPhysicsHelper.hitEvent(this, player, isServe);
							isServe = false;
						}						

						if (player.swingProgress > 0.2F) {
							double speed = Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);

							// Player hits the ball!
							if (entity1.getDistanceToEntity(this) < triggerDist) {
								BallPhysicsHelper.hitEvent(this, player, isServe);
								this.updateHitCount(player);
								
								// Change the game state from "serving" to "in game"
								setCourtState(GameStates.IN_GAME);
							}
						}
					}

					if (movingobjectposition1 != null)
					{
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}
		}

		if (movingobjectposition != null)
		{
			if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE/* && this.worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Block.portal.blockID*/)
			{
				//	this.setInPortal();
				//		System.out.println("hit ground");
				//	this.onImpact(movingobjectposition);
			}
			else
			{
				//this.onImpact(movingobjectposition);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

		for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
		{
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
		{
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F)
		{
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
		{
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f2 = 0.99F;
		float f3 = this.getGravityVelocity();

		if (this.isInWater())
		{
			for (int k = 0; k < 4; ++k)
			{
				float f4 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ);
			}

			f2 = 0.8F;
		}

		this.motionX *= (double)f2;
		this.motionY *= (double)f2;
		this.motionZ *= (double)f2;
		this.motionY -= (double)f3;
		//this.setPosition(this.posX, this.posY, this.posZ);
		this.moveEntity(motionX, motionY, motionZ);

		boolean onGround2 = this.posY == MathHelper.floor_double(courtY);

		if (onGround) {
			int i = this.worldObj.getBlockId(this.xTile, this.yTile - 1, this.zTile);

			if (i == this.inTile) {
				++this.ticksInGround;

				if (!worldObj.isRemote) {
					TileEntity te = worldObj.getBlockTileEntity(courtX, courtY, courtZ);
					if (te != null && te instanceof TileEntityGameManager) {
						TileEntityGameManager court = (TileEntityGameManager)te;

						if (court.gameState != GameStates.PRE_GAME) {
							court.onBallImpact(this);
							setDead();
						}
					}
				}
				return;
			}
		} else {
			++this.ticksInAir;
		}		
	}

	/**
	 * returns the bounding box for this entity
	 */
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	//@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			if (!worldObj.isRemote) {
				TileEntity te = worldObj.getBlockTileEntity(courtX, courtY, courtZ);
				if (te != null && te instanceof TileEntityGameManager) {
					TileEntityGameManager court = (TileEntityGameManager)te;

					court.onBallImpact(this);
				}
			}
		}
	}
	
	/**
	 * Update the hit count and last team hit variables<br />
	 * Essentially manages the rules regarding which team last hit the ball
	 * And to make sure a team does not exceed 3 hits on the ball
	 * @param hitter Player who last hit the ball
	 */
	public void updateHitCount(EntityLivingBase hitter) {
		if (!worldObj.isRemote) {
			TileEntity te = worldObj.getBlockTileEntity(courtX, courtY, courtZ);
			if (te != null && te instanceof TileEntityGameManager) {
				TileEntityGameManager court = (TileEntityGameManager)te;

				// Get the team that just hit the ball
				int currentHitTeam = court.team1.contains(hitter.entityId) ? 1 : 2;
				
				// If it was the same team that last hit it, increment the hit counter
				// Otherwise set it to 0 and change the team
				if (this.lastHitTeam == currentHitTeam) {
					this.hitCount++;
				} else {
					this.hitCount = 0;
					this.lastHitTeam = currentHitTeam;
				}
				
				if (hitCount >= 3) {
					court.onScore(currentHitTeam, this, true);
					court.rotateTeam(currentHitTeam == 1 ? 2 : 1);
				}
			}
		}
	}
	
	/**
	 * Set the game state of the court tile entity
	 * @param state Game state to set it to
	 */
	public void setCourtState(byte state) {
		if (!worldObj.isRemote) {
			TileEntity te = worldObj.getBlockTileEntity(courtX, courtY, courtZ);
			if (te != null && te instanceof TileEntityGameManager) {
				TileEntityGameManager court = (TileEntityGameManager)te;

				court.setGameState(GameStates.IN_GAME, true);
			}
		}
	}

}
