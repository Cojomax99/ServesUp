package net.cojo.servesup.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.cojo.servesup.court.CourtData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import com.google.common.primitives.Ints;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityGameManager extends TileEntity {

	/** Court orientation */
	public int orientation;

	/** Is there currently a game going on? */
	public boolean isGameActive;

	/** Has the court been built yet? */
	public boolean isCourtBuilt;
	
	/** List of entity ids of active players */
	public List<Integer> activeIDs;
	
	/** Map of entity id to team number */
	public Map<Integer, Integer> playerTeamMap;

	/** Number of blocks away from the net the front zone is located */
	private static final int FRONT_ZONE_OFFSET = 4;

	/** Height offset for rendering */
	private static final double HEIGHT_OFFSET = 1.01;

	public int minX = Integer.MIN_VALUE, minZ = Integer.MIN_VALUE, maxX = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

	public TileEntityGameManager() {
		activeIDs = new ArrayList<Integer>();
		playerTeamMap = new HashMap<Integer, Integer>();
	}
	
	/**
	 * Perform all operations required to start the game
	 */
	public void startGame() {
		//List ents = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getCourtActualBounds());
		List team1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getTeamOneBounds());
		List team2 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getTeamTwoBounds());
		
		int numPreActiveIDs = activeIDs.size();
		
		for (Object obj : team1) {
			if (obj instanceof Entity) {
				System.out.println("Adding to team 1");
				activeIDs.add(((Entity) obj).entityId);
				this.playerTeamMap.put(((Entity) obj).entityId, 1);
			} else
				throw new IllegalArgumentException("Somehow a non-entity entity ended up on the volleyball court at game start in team 1!");
		}
		
		for (Object obj : team2) {
			if (obj instanceof Entity) {
				System.out.println("Adding to team 2");
				activeIDs.add(((Entity) obj).entityId);
				this.playerTeamMap.put(((Entity) obj).entityId, 2);
			} else
				throw new IllegalArgumentException("Somehow a non-entity entity ended up on the volleyball court at game startin team 2!");
		}
		
		// If there are new ids in the list, sync the list
		if (numPreActiveIDs < activeIDs.size()) {
			sync();
		}
	}
	
	public int getTeam(Integer entID) {
		return playerTeamMap.get(entID);
	}

	/**
	 * @return Gets AABBs for the front zone lines for rendering (slightly adjusted min vals)
	 */
	public AxisAlignedBB[] getFrontZoneLineBounds() {
		if (minX == Integer.MIN_VALUE)
			return null;

		double midX = (maxX + minX) / 2 + 0.5D;
		double midZ = (maxZ + minZ) / 2 + 0.5D;

		if (orientation > 1)
			return toAABBArray(AxisAlignedBB.getAABBPool().getAABB(minX + 1, y(), midZ + FRONT_ZONE_OFFSET, maxX, y(), midZ + FRONT_ZONE_OFFSET), 
					AxisAlignedBB.getAABBPool().getAABB(minX + 1, y(), midZ - FRONT_ZONE_OFFSET, maxX, y(), midZ - FRONT_ZONE_OFFSET));
		else
			return toAABBArray(AxisAlignedBB.getAABBPool().getAABB(midX + FRONT_ZONE_OFFSET, y(), minZ + 1, midX + FRONT_ZONE_OFFSET, y(), maxZ),
					AxisAlignedBB.getAABBPool().getAABB(midX - FRONT_ZONE_OFFSET, y(), minZ + 1, midX - FRONT_ZONE_OFFSET, y(), maxZ));
	}

	/**
	 * Takes a parameter array and converts it to a regular array, just saves me a little typing
	 * @param bbs A collection of bounding boxes
	 * @return The same collection of bounding boxes, in array form!
	 */
	public AxisAlignedBB[] toAABBArray(AxisAlignedBB...bbs) {
		return bbs;
	}

	/**
	 * @return Gets an AABB for the center line for rendering (slightly adjusted min vals)
	 */
	public AxisAlignedBB getCenterLineBounds() {
		if (minX == Integer.MIN_VALUE)
			return null;

		double midX = (maxX + minX) / 2 + 0.5D;
		double midZ = (maxZ + minZ) / 2 + 0.5D;

		if (orientation > 1)
			return AxisAlignedBB.getAABBPool().getAABB(minX + 1, y(), midZ, maxX, y(), midZ);
		else
			return AxisAlignedBB.getAABBPool().getAABB(midX, y(), minZ + 1, midX, y(), maxZ);
	}

	/**
	 * @return Gets an AABB for the entire court for rendering (slightly adjusted min vals)
	 */
	public AxisAlignedBB getCourtRenderBounds() {		
		return minX > Integer.MIN_VALUE ? AxisAlignedBB.getAABBPool().getAABB(minX + 1, y(), minZ + 1, maxX, y(), maxZ) : null;
	}
	
	/**
	 * @return Gets an AABB for the entire court for full use (slightly adjusted min vals)
	 */
	public AxisAlignedBB getCourtActualBounds() {		
		return minX > Integer.MIN_VALUE ? AxisAlignedBB.getAABBPool().getAABB(minX, y(), minZ, maxX, y() + 4, maxZ) : null;
	}

	/**
	 * Simplification method for getting the height to render the lines at
	 * @return
	 */
	private double y() {
		return yCoord + HEIGHT_OFFSET;
	}

	/**
	 * Allows the player to view the TE even when they aren't looking at the block
	 * TODO: Feed this class a Court and use its bounds
	 * @return Bounding box for the player to look at that allows this TE to render
	 */
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return AxisAlignedBB.getAABBPool().getAABB(xCoord - 12, yCoord, zCoord - 12, xCoord + 12, yCoord + 10, zCoord + 12);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		orientation = nbt.getInteger("Orientation");
		isGameActive = nbt.getBoolean("GameActive");
		isCourtBuilt = nbt.getBoolean("CourtBuilt");
		minX = nbt.getInteger("minX");
		maxX = nbt.getInteger("maxX");
		minZ = nbt.getInteger("minZ");
		maxZ = nbt.getInteger("maxZ");
		
	//	if (isGameActive)	
			activeIDs = getList(nbt.getIntArray("ActiveIDs"));
	//	else
	//		activeIDs = new ArrayList<Integer>();
		
		int count = 0;
		
		Iterator it = nbt.getCompoundTag("playerMapCompound").getTags().iterator();
		while (it.hasNext()) {
			count++;
			NBTTagInt data = (NBTTagInt)it.next();
			playerTeamMap.put(Integer.valueOf(data.getName()), data.data);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Orientation", orientation);
		nbt.setBoolean("GameActive", isGameActive);
		nbt.setBoolean("CourtBuilt", isCourtBuilt);
		nbt.setInteger("minX", minX);
		nbt.setInteger("maxX", maxX);
		nbt.setInteger("minZ", minZ);
		nbt.setInteger("maxZ", maxZ);
		nbt.setIntArray("ActiveIDs", Ints.toArray(activeIDs));
		
		NBTTagCompound playerMapCompound = new NBTTagCompound();
		
		int count = 0;
		Iterator it = this.playerTeamMap.entrySet().iterator();
		while (it.hasNext()) {
			count++;
			Map.Entry pairs = (Map.Entry)it.next();
			int key = (Integer)pairs.getKey();
			int value = (Integer)pairs.getValue();
			playerMapCompound.setInteger("" + key, value);
		}
		
		nbt.setCompoundTag("playerMapCompound", playerMapCompound);	
	}
	
	/**
	 * Converts an int array to a List<Integer>
	 * @param data int array
	 * @return a List of Integer objects containing the same data as the array
	 */
	private List<Integer> getList(int[] data) {
		List<Integer> temp = new ArrayList<Integer>(data.length);
		
		for (int d : data) {
			temp.add(d);
		}
		
		return temp;
	}

	public void setCourtData(AxisAlignedBB data) {
		this.minX = MathHelper.floor_double(data.minX);
		this.maxX = MathHelper.floor_double(data.maxX);
		this.minZ = MathHelper.floor_double(data.minZ);
		this.maxZ = MathHelper.floor_double(data.maxZ);
		this.isCourtBuilt = true;
		sync();
	}

	public CourtData getCourtData() {
		return new CourtData(minX, maxX, this.yCoord, minZ, maxZ);
	}

	/**
	 * Has the court been built?
	 * @return Has the court been built?
	 */
	public boolean isCourtBuilt() {
		return this.isCourtBuilt;
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		this.readFromNBT(pkt.data);
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound var1 = new NBTTagCompound();
		this.writeToNBT(var1);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
	}

	/**
	 * Change the orientation
	 */
	public void rotate() {
		orientation = orientation < 3 ? orientation + 1 : 0;
		sync();		
	}

	/**
	 * Sync the data so all clients get the same data
	 */
	public void sync() {
		PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
	}

	public int getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Runs the game loop
	 */
	@Override
	public void updateEntity() {
		Iterator<Integer> it = activeIDs.iterator();
		
		while (it.hasNext()) {
			Integer id = it.next();
			
			Entity ent = this.worldObj.getEntityByID(id.intValue());
			
			if (ent instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)ent;
			}
		}
	}
	
	/**
	 * @return Gets a bounding box for team one
	 */
	private AxisAlignedBB getTeamOneBounds() {
		if (minX == Integer.MIN_VALUE)
			return null;

		double midX = (maxX + minX) / 2 + 0.5D;
		double midZ = (maxZ + minZ) / 2 + 0.5D;

		if (orientation > 1)
			return AxisAlignedBB.getAABBPool().getAABB(minX + 1, y(), midZ, maxX, y(), maxZ);
		else
			return AxisAlignedBB.getAABBPool().getAABB(midX, y(), minZ + 1, maxX, y(), maxZ);
	}
	
	/**
	 * @return Gets a bounding box for team two
	 */
	private AxisAlignedBB getTeamTwoBounds() {
		if (minX == Integer.MIN_VALUE)
			return null;

		double midX = (maxX + minX) / 2 + 0.5D;
		double midZ = (maxZ + minZ) / 2 + 0.5D;

		if (orientation > 1)
			return AxisAlignedBB.getAABBPool().getAABB(minX + 1, y(), minZ, maxX, y(), midZ);
		else
			return AxisAlignedBB.getAABBPool().getAABB(minX, y(), minZ + 1, midX, y(), maxZ);
	}
	
}
