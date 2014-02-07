package net.cojo.servesup.court;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class PositionHelper {

	public static ChunkCoordinates getStartPosition(ChunkCoordinates blockPos, AxisAlignedBB courtBB, int orientation, int position, int numPlayers) {
		ChunkCoordinates temp = new ChunkCoordinates((int)courtBB.minX, (int)courtBB.minY, (int)courtBB.minZ);

		// Location of the court block
		Vec3 vecBlock = Vec3.createVectorHelper(blockPos.posX, blockPos.posY, blockPos.posZ);

		// Find the middle of the court, width-wise
		Vec3 vecRel = Vec3.createVectorHelper(MathHelper.floor_double(CourtBuilder.WIDTH / 2), 0, 0);

		int degrees = orientation == 0 ? 0 : orientation == 1 ? 180 : orientation == 2 ? 90 : -90;

		// Rotate the vec to be the right side
		vecRel.rotateAroundY((float)Math.toRadians(degrees));

		// Location of the middle of the court
		Vec3 vecNet = Vec3.createVectorHelper(vecBlock.xCoord + vecRel.xCoord, vecBlock.yCoord + vecRel.yCoord, vecBlock.zCoord + vecRel.zCoord);

		return temp;
	}

	/**
	 * Set the offsets then return a map to be used later
	 * @return A map of number of players to pre-defined offsets
	 */
	public static Map<Integer, List<Vec3>> setOffsets() {
		Map<Integer, List<Vec3>> positionOffsetsMap = new HashMap<Integer, List<Vec3>>();
		
		List team = new ArrayList<Vec3>();
		team.add(Vec3.createVectorHelper(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE));
		positionOffsetsMap.put(0, team);
		
		team = new ArrayList<Vec3>();
		team.add(Vec3.createVectorHelper(0.5, 0, 0));
		positionOffsetsMap.put(1, team);

		team = new ArrayList<Vec3>();
		team.add(Vec3.createVectorHelper(0.75, 0, 0));
		team.add(Vec3.createVectorHelper(0.25, 0, 0));
		positionOffsetsMap.put(2, team);

		team = new ArrayList<Vec3>();
		team.add(Vec3.createVectorHelper(0.75, 0, 0));
		team.add(Vec3.createVectorHelper(0.25, 0, -0.5));
		team.add(Vec3.createVectorHelper(0.25, 0, 0.5));
		positionOffsetsMap.put(3, team);

		team = new ArrayList<Vec3>();
		team.add(Vec3.createVectorHelper(0.75, 0, -0.5));
		team.add(Vec3.createVectorHelper(0.75, 0, 0.5));
		team.add(Vec3.createVectorHelper(0.25, 0, -0.5));
		team.add(Vec3.createVectorHelper(0.25, 0, 0.5));
		positionOffsetsMap.put(4, team);

		team = new ArrayList<Vec3>();
		team.add(Vec3.createVectorHelper(0.75, 0, -0.5));
		team.add(Vec3.createVectorHelper(0.75, 0, 0.5));
		team.add(Vec3.createVectorHelper(0.25, 0, -0.67));
		team.add(Vec3.createVectorHelper(0.25, 0, 0));
		team.add(Vec3.createVectorHelper(0.25, 0, 0.67));
		positionOffsetsMap.put(5, team);

		team = new ArrayList<Vec3>();
		team.add(Vec3.createVectorHelper(0.75, 0, -0.67));
		team.add(Vec3.createVectorHelper(0.75, 0, 0));
		team.add(Vec3.createVectorHelper(0.75, 0, 0.67));
		team.add(Vec3.createVectorHelper(0.25, 0, -0.67));
		team.add(Vec3.createVectorHelper(0.25, 0, 0));
		team.add(Vec3.createVectorHelper(0.25, 0, 0.67));
		positionOffsetsMap.put(6, team);
		
		return positionOffsetsMap;
	}

	private static int average(double val1, double val2) {
		return MathHelper.floor_double((val1 + val2) / 2.0D);
	}

	private static int average(int val1, int val2) {
		return MathHelper.floor_double((val1 + val2) / 2.0D);
	}

	private static ChunkCoordinates cc(double x, double y, double z) {
		return new ChunkCoordinates(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
	}

	private static ChunkCoordinates cc(int x, int y, int z) {
		return new ChunkCoordinates(x, y, z);
	}
}
