package net.cojo.servesup.tileentity;

import net.cojo.servesup.court.CourtData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
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

	/** Number of blocks away from the net the front zone is located */
	private static final int FRONT_ZONE_OFFSET = 4;

	/** Height offset for rendering */
	private static final double HEIGHT_OFFSET = 1.01;

	public int minX = Integer.MIN_VALUE, minZ = Integer.MIN_VALUE, maxX = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

	public TileEntityGameManager() {

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

	@Override
	public void updateEntity() {

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

}
