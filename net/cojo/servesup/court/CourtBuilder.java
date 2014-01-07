package net.cojo.servesup.court;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CourtBuilder {

	/** Number of blocks long and wide a side of a court is, excluding border */
	private static final int INNER_SIZE = 11;

	/**
	 * The method that performs the actual building of the court, given a direction to build it in
	 * @param world World instance
	 * @param x x coord of the court block
	 * @param y y coord of the court block
	 * @param z z coord of the court block
	 * @param direction Cardinal direction to build the court in
	 */
	public static void buildCourt(World world, int x, int y, int z, int direction) {
		CourtData data = buildBorder(world, x, y, z, direction);
		fillCourt(world, data);
	}
	
	/**
	 * Fill in the court based on the data returned by buildBorder
	 * @param world World instance
	 * @param data Data of the court
	 */
	private static void fillCourt(World world, CourtData data) {
		for (int i = data.minX + 1; i < data.maxX; i++) {
			placeCourt(world, i, data.y, data.minZ);
			placeCourt(world, i, data.y, data.maxZ);
		}
		
		for (int i = data.minZ + 1; i < data.maxZ; i++) {
			placeCourt(world, data.minX, data.y, i);
			placeCourt(world, data.maxZ, data.y, i);
		}
	}

	/**
	 * Build the court's border, return data for the court
	 * @param world World instance
	 * @param x xCoord of the court block
	 * @param y yCoord of the court block
	 * @param z zCoord of the court block
	 * @param direction Determines court orientation
	 * @return a CourtData object containing the minx, maxX, y, minZ, and maxZ values of the court
	 */
	private static CourtData buildBorder(World world, int x, int y, int z, int direction) {
		int minX=0, minZ=0, maxX=0, maxZ=0;

		// Default direction to 0 if it is not in [0,3] inclusive
		direction = direction < 0 || direction > 3 ? 0 : direction;

		// Determine the min/max vals based on direction
		switch (direction) {
		case 0:
			minX = x - INNER_SIZE - 1;
			maxX = x + INNER_SIZE + 1;
			minZ = z - 1 - INNER_SIZE - 1;
			maxZ = z - 1;
			break;
		case 1:
			minX = x - INNER_SIZE - 1;
			maxX = x + INNER_SIZE + 1;
			minZ = z + 1;
			maxZ = z + 1 + INNER_SIZE + 1;
			break;
		case 2:
			minX = x - 1 - INNER_SIZE - 1;
			maxX = x - 1;
			minZ = z - INNER_SIZE - 1;
			maxZ = z + INNER_SIZE + 1;
			break;
		case 3:
			minX = x + 1;
			maxX = x + 1 + INNER_SIZE + 1;
			minZ = z - INNER_SIZE - 1;
			maxZ = z + INNER_SIZE + 1;
			break;
		}

		// Place x-length border
		for (int i = minX; i <= maxX; i++) {
			placeBorder(world, i, y, minZ);
			placeBorder(world, i, y, maxZ);
		}

		// Place z-length border
		for (int i = minZ; i <= maxZ; i++) {
			placeBorder(world, minX, y, i);
			placeBorder(world, maxX, y, i);
		}
		
		return new CourtData(minX, maxX, y, minZ, maxZ);
	}

	/**
	 * Lazy method that just calls place, but for whatever block should be used as the border
	 */
	public static void placeBorder(World world, int x, int y, int z) {
		//TODO: configurize border block here
		place(world, x, y, z, Block.planks, 4);
	}
	
	/**
	 * Lazy method that just calls place, but for whatever block should be used as the court
	 */
	public static void placeCourt(World world, int x, int y, int z) {
		//TODO: configurize court block here
		place(world, x, y, z, Block.sand);
	}

	public static void place(World world, int x, int y, int z, Block block) {
		world.setBlock(x, y, z, block.blockID);
	}

	public static void place(World world, int x, int y, int z, Block block, int meta) {
		place(world, x, y, z, block.blockID, meta);
	}

	public static void place(World world, int x, int y, int z, int id, int meta) {
		world.setBlock(x, y, z, id, meta, 3);
	}
}
