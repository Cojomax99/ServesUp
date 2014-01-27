package net.cojo.servesup.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.cojo.servesup.GameStates;
import net.cojo.servesup.court.CourtBuilder;
import net.cojo.servesup.court.CourtData;
import net.cojo.servesup.court.PositionHelper;
import net.cojo.servesup.entities.EntityVolleyball;
import net.cojo.servesup.items.SUItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import com.google.common.primitives.Ints;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityGameManager extends TileEntity {

	/**
	 * 
	 * 
	 * 
	 * http://www.fivb.org/en/volleyball/Basic_Rules.asp
	 * 
	 * 
	 * http://iml.jou.ufl.edu/projects/Fall08/Devine/rules.html
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */




	/** Court orientation */
	public int orientation;

	/** Has the court been built yet? */
	public boolean isCourtBuilt;

	/** Current state of the game. See GameStates.java for details */
	public byte gameState;

	/** Team 1's score */
	public short team1Score;

	/** Team 2's score */
	public short team2Score;
	
	/** Is either a team number (1, 2) if that team needs to get rotated, or -1 otherwise */
	public byte rotateTeamFlag;

	/** 1 if team 1 is serving, 2 if team 2 is serving */
	public byte teamServing;

	/** List of entity ids of active players */
	public List<Integer> activeIDs;

	/** List of entity ids of players on team 2 */
	public List<Integer> team1;

	/** List of entity ids of players on team 1 */
	public List<Integer> team2;

	/** Map of entity id to team number */
	public Map<Integer, Integer> playerTeamMap;

	/** Map of a position on the court (0-5) to coordinates that represent that position */
	public Map<Integer, ChunkCoordinates> positionCoordsMap;

	/** Map of a position on the court (0-5) to hard-coded offsets */
	public Map<Integer, List<Vec3>> positionOffsetsMap;

	/** Map of entity ids of players on team 1 to their positions */
	public Map<Integer, Integer> team1PositionMap;

	/** Map of entity ids of players on team 2 to their positions */
	public Map<Integer, Integer> team2PositionMap;

	/** Number of blocks away from the net the front zone is located */
	private static final int FRONT_ZONE_OFFSET = 4;

	/** Height offset for rendering */
	private static final double HEIGHT_OFFSET = 1.01;

	public int minX = Integer.MIN_VALUE, minZ = Integer.MIN_VALUE, maxX = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

	public TileEntityGameManager() {
		activeIDs = new ArrayList<Integer>();
		team1 = new ArrayList<Integer>();
		team2 = new ArrayList<Integer>();
		playerTeamMap = new HashMap<Integer, Integer>();
		positionCoordsMap = new HashMap<Integer, ChunkCoordinates>();
		positionOffsetsMap = new HashMap<Integer, List<Vec3>>();
		team1PositionMap = new HashMap<Integer, Integer>();
		team2PositionMap = new HashMap<Integer, Integer>();
		rotateTeamFlag = -1;
		team1Score = team2Score = 0;

		// Set the offsets
		positionOffsetsMap = PositionHelper.setOffsets();
	}

	/**
	 * Get the spawn position for a player at a certain position on a team
	 * @param pos Player position
	 * @param team Team 1 or team 2
	 * @return Spawn position for a player
	 */
	public Vec3 getSpawnPosition(int pos, int team) {
		Vec3 vec = Vec3.createVectorHelper(this.xCoord, this.yCoord, this.zCoord + 0.5);
		Vec3 vecRel = Vec3.createVectorHelper(MathHelper.floor_double(CourtBuilder.WIDTH / 2) + 2.5, 0, 0);

		vecRel.rotateAroundY((float) (Math.toRadians(getDegreeOrientation())));
		Vec3 vecNet = Vec3.createVectorHelper(vec.xCoord + vecRel.xCoord, vec.yCoord + vecRel.yCoord, vec.zCoord + vecRel.zCoord);
		Vec3 gridPos = positionOffsetsMap.get(/*team == 1 ? team1.size() + 3 : team2.size() + 3*/6).get(pos);
		Vec3 vecRelPlayer1 = Vec3.createVectorHelper(gridPos.xCoord * ((CourtBuilder.LENGTH / 2) + 0.5), 0, gridPos.zCoord * (MathHelper.floor_double(CourtBuilder.WIDTH / 2) + 2.5));
		if (team == 1)
			vecRelPlayer1.rotateAroundY((float) Math.toRadians(getDegreeOrientation() - 90));
		else
			vecRelPlayer1.rotateAroundY((float) Math.toRadians(getDegreeOrientation() + 90));

		Vec3 vecPl = Vec3.createVectorHelper(vecNet.xCoord + vecRelPlayer1.xCoord, vecNet.yCoord + vecRelPlayer1.yCoord, vecNet.zCoord + vecRelPlayer1.zCoord);

		return vecPl;
	}

	/**
	 * 
	 * @return Get a degree value for a numbered orientation (0-3)
	 */
	public int getDegreeOrientation() {
		return orientation == 0 ? 90 : orientation == 1 ? -90 : orientation == 2 ? 180 : 0;
	}

	/**
	 * Simplification method for printing out the values of a vector, for debug only
	 * @param vec Vector
	 */
	private void printVec(Vec3 vec) {
		System.out.println(vec.xCoord + " " + vec.yCoord + " " + vec.zCoord);
	}

	/**
	 * Turn the list of entity ids in team2 to names
	 * @return A list of names of players on team 2
	 */
	public List<String> getTeam1Names() {
		List<String> names = new ArrayList<String>();

		Iterator<Integer> it = team1.iterator();

		while (it.hasNext()) {
			Integer id = it.next();
			Entity e = this.worldObj.getEntityByID(id.intValue());
			try {
				names.add(e.getEntityName());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return names;
	}

	/**
	 * Turn the list of entity ids in team1 to names
	 * @return A list of names of players on team 1
	 */
	public List<String> getTeam2Names() {
		List<String> names = new ArrayList<String>();

		Iterator<Integer> it = team2.iterator();

		while (it.hasNext()) {
			Integer id = it.next();
			Entity e = this.worldObj.getEntityByID(id.intValue());
			try {
				names.add(e.getEntityName());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return names;
	}

	/**
	 * Perform all operations required to start the game
	 */
	public void startGame() {
		team1.clear();
		team2.clear();
		activeIDs.clear();
		List e_team1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getTeamOneBounds());
		List e_team2 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getTeamTwoBounds());

		int numPreActiveIDs = activeIDs.size();

		for (Object obj : e_team1) {
			if (obj instanceof Entity) {
				System.out.printf("Adding %d to team 1\n", ((Entity) obj).entityId);
				activeIDs.add(((Entity) obj).entityId);
				team1.add(((Entity) obj).entityId);
				this.playerTeamMap.put(((Entity) obj).entityId, 1);
			} else
				throw new IllegalArgumentException("Somehow a non-entity entity ended up on the volleyball court at game start in team 1!");
		}

		for (Object obj : e_team2) {
			if (obj instanceof Entity) {
				System.out.printf("Adding %d to team 2\n", ((Entity) obj).entityId);
				activeIDs.add(((Entity) obj).entityId);
				team2.add(((Entity) obj).entityId);
				this.playerTeamMap.put(((Entity) obj).entityId, 2);
			} else
				throw new IllegalArgumentException("Somehow a non-entity entity ended up on the volleyball court at game startin team 2!");
		}

		// If there are new ids in the list, sync the list
		if (numPreActiveIDs < activeIDs.size()) {
			sync();
		}
	}

	/**
	 *
	 * @param entID Entity id
	 * @return Get the team of a player with a given entity id
	 */
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
		nbt.setBoolean("CourtBuilt", isCourtBuilt);
		nbt.setInteger("minX", minX);
		nbt.setInteger("maxX", maxX);
		nbt.setInteger("minZ", minZ);
		nbt.setInteger("maxZ", maxZ);
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
		this.readFromNBTPacket(pkt.data);
	}

	/**
	 * A packet for syncing data client <-> server
	 */
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound var1 = new NBTTagCompound();
		this.writeToNBTPacket(var1);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
	}	

	/**
	 * Sync the data so all clients get the same data
	 */
	public void sync() {
		PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
	}

	/**
	 * NBT loading while in-game, for syncing purposes. DOES NOT READ FROM DISK ON LOAD.
	 * @param nbt tag compound
	 */
	public void readFromNBTPacket(NBTTagCompound nbt) {
		gameState = nbt.getByte("GameState");
		activeIDs = getList(nbt.getIntArray("ActiveIDs"));
		team1 = getList(nbt.getIntArray("Team1IDs"));
		team2 = getList(nbt.getIntArray("Team2IDs"));
		team1Score = nbt.getShort("Team1Score");
		team2Score = nbt.getShort("Team2Score");
		rotateTeamFlag = nbt.getByte("RotateTeamFlag");

		int count = 0;
		Iterator it = nbt.getCompoundTag("playerMapCompound").getTags().iterator();

		while (it.hasNext()) {
			count++;
			NBTTagInt data = (NBTTagInt)it.next();
			playerTeamMap.put(Integer.valueOf(data.getName()), data.data);
		}
	}

	/**
	 * NBT saving while in-game, for syncing purposes. DOES NOT SAVE TO DISK.
	 * @param nbt tag compound
	 */
	public void writeToNBTPacket(NBTTagCompound nbt) {
		nbt.setIntArray("ActiveIDs", Ints.toArray(activeIDs));
		nbt.setIntArray("Team1IDs", Ints.toArray(team1));
		nbt.setIntArray("Team2IDs", Ints.toArray(team2));
		nbt.setByte("GameState", gameState);
		nbt.setShort("Team1Score", team1Score);
		nbt.setShort("Team2Score", team2Score);
		nbt.setByte("RotateTeamFlag", rotateTeamFlag);
		
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
	 * Change the orientation
	 */
	public void rotate() {
		orientation = orientation < 3 ? orientation + 1 : 0;
		sync();		
	}

	/**
	 * Set the orientation of the court to a specified value, and round down to 3 and up to 0 if necessary
	 * @param val Value of orientation to set for the court
	 */
	public void setOrientation(int val) {
		orientation = val < 0 ? 0 : val > 3 ? 3 : val;
		sync();
	}

	/**
	 * 
	 * @return Get the orientation of the court
	 */
	public int getOrientation() {
		return this.orientation;
	}

	/**
	 * @return Gets a bounding box for team one
	 */
	public AxisAlignedBB getTeamOneBounds() {
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
	public AxisAlignedBB getTeamTwoBounds() {
		if (minX == Integer.MIN_VALUE)
			return null;

		double midX = (maxX + minX) / 2 + 0.5D;
		double midZ = (maxZ + minZ) / 2 + 0.5D;

		if (orientation > 1)
			return AxisAlignedBB.getAABBPool().getAABB(minX + 1, y(), minZ, maxX, y(), midZ);
		else
			return AxisAlignedBB.getAABBPool().getAABB(minX, y(), minZ + 1, midX, y(), maxZ);
	}

	/**
	 * Quick method for checking if the current game state is a given state
	 * @param state Value of a game state
	 * @return Whether the given game state is the current game state
	 */
	private boolean isGameState(byte state) {
		return this.gameState == state;
	}

	/**
	 * Returns the side of the court this entity is on, or -1 if it is not on either
	 * @param e Entity
	 * @return Which side of the court an entity is on, or -1 if not on either
	 */
	public int getCurrentCourtSide(Entity e) {
		return this.getTeamOneBounds().intersectsWith(e.getBoundingBox()) ? 1 :
			this.getTeamTwoBounds().intersectsWith(e.getBoundingBox()) ? 2 : -1;
	}

	/**
	 * Triggered when a ball lands on the ground. If it is within bounds and
	 * a game is currently going on, appropriate action is taken with regards
	 * to scoring and rotating players around if necessary.
	 * 
	 * @param ball volleyball entity
	 */
	public void onBallImpact(EntityVolleyball ball) {
		int side = getCurrentCourtSide(ball);

		//TODO debug, remove
		outputDebugData(side, ball);

		//TODO all game logic here!
		this.onRoundEnd(side, ball);
	}

	/**
	 * Called when the round is over, triggered by onBallImpact
	 * @param side Side the ball landed on
	 * @param ball Volleyball entity reference
	 */
	public void onRoundEnd(int side, EntityVolleyball ball) {
		// Update the game state to END_ROUND, do not sync
		this.updateGameState(GameStates.END_ROUND, false);
		
		// Update the game score, do not sync
		this.onScore(side, ball, false);
		
		// Update the game state to PRE_SERVE, effectively preparing for next round, DO sync
		this.updateGameState(GameStates.PRE_SERVE, true);
	}

	/**
	 * Update the game score
	 * @param side Side of the court the ball landed on
	 */
	public void onScore(int side, EntityVolleyball ball, boolean shouldSync) {
		if (side == 1) {
			this.team2Score++;
			if (this.getTeam(ball.getHitter().entityId) == 1) {
				updateRotateFlag((byte)2, false);
			}
		} else
			if (side == 2) {
				this.team1Score++;
				if (this.getTeam(ball.getHitter().entityId) == 2) {
					updateRotateFlag((byte)1, false);
				}
			} else {
				if (this.getTeam(ball.getHitter().entityId) == 1) {
					this.team2Score++;
				} else
					this.team1Score++;
			}
	}
	
	/**
	 * Update the flag that determines which team should be rotated, if any
	 * @param flag new flag to set
	 * @param shouldSync Should this method sync with the client?
	 */
	public void updateRotateFlag(byte flag, boolean shouldSync) {
		this.rotateTeamFlag = flag;
		
		if (shouldSync)
			this.sync();
	}

	/**
	 * Updates the game state variable
	 * @param state New state for the game to be
	 * @param shouldSync Whether this should sync with the client or not. Usually false when called in other methods that update variables
	 */
	public void updateGameState(byte state, boolean shouldSync) {
		this.gameState = state;

		if (shouldSync)
			this.sync();
	}

	private void outputDebugData(int side, EntityVolleyball ball) {
		if (side != -1) {			
			EntityLivingBase server = ball.getHitter();
			if (server != null) {
				String line = String.format("Ball landed on side %d. Served by: %s who is on team %d",
						side, server.getEntityName(), getTeam(server.entityId));
				System.out.println(line);
			}
		} else {
			System.err.println("Ball is off the court :(");
		}
	}
	
	/**
	 * Gets a volleyball for serving, injects the coords hash into it for later use
	 * @return A volleyball item with injected NBT
	 */
	public ItemStack getVolleyballItem() {
		ItemStack vball = new ItemStack(SUItems.volleyball);
		vball.stackTagCompound = new NBTTagCompound();
		vball.stackTagCompound.setInteger("courtX", xCoord);
		vball.stackTagCompound.setInteger("courtY", yCoord);
		vball.stackTagCompound.setInteger("courtZ", zCoord);

		return vball;
	}
	
	/**
	 * Rotate all members of a team to their next position(s)
	 * @param team Team to rotate the members of
	 */
	public void rotateTeam(byte team) {
		if (team == -1)
			return;
		
		Iterator<Integer> it = null;
		
		if (team == 1)
			it = this.team1PositionMap.keySet().iterator();
		else
			if (team == 2) {
				it = this.team2PositionMap.keySet().iterator();
			}
		
		if (it == null)
			return;

		while (it.hasNext()) {
			Integer id = it.next();

			if (team == 1) {
				int value = this.team1PositionMap.get(id);
				this.team1PositionMap.put(id, (value + 1) % this.team1.size());
			} else
				if (team == 2) {
					int value = this.team2PositionMap.get(id);
					this.team2PositionMap.put(id, (value + 1) % this.team2.size());
				}
		}		
	}
	
	/**
	 * Update the player positions based on the current values in the team position maps.
	 */
	public void updatePlayerPositions() {
		
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

			if (ent instanceof EntityLivingBase) {
				EntityLivingBase el = (EntityLivingBase)ent;
				if (el.getCurrentItemOrArmor(0) == null || el.getCurrentItemOrArmor(0).getItem().itemID != SUItems.volleyball.itemID)
					el.setCurrentItemOrArmor(0, getVolleyballItem());
			}

			//if (isGameState(GameStates.PRE_SERVE)) {

			//}
		}
		
		if (isGameState(GameStates.PRE_SERVE)) {
			if (rotateTeamFlag != -1) {
				rotateTeam(rotateTeamFlag);
				updatePlayerPositions();
			}
		}
	}

}
