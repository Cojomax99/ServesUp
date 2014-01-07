package net.cojo.servesup.court;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CourtBuilder {

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
		buildBorder(world, x, y, z, direction);
	}

	private static void buildBorder(World world, int x, int y, int z, int direction) {
		int minX=0, minZ=0, maxX=0, maxZ=0;

		minX = x - INNER_SIZE - 1;
		maxX = x + INNER_SIZE + 1;
		minZ = z - 1;
		maxZ = z - 1 - INNER_SIZE - 1;

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

		// To prevent edge cases in the X direction
		for (int i = minX; i <= maxX; i++) {
			placeBorder(world, i, y, minZ);
			placeBorder(world, i, y, maxZ);
		}

		// To prevent edge cases in the Z direction
		for (int i = minZ; i <= maxZ; i++) {
			placeBorder(world, minX, y, i);
			placeBorder(world, maxX, y, i);
		}
	}

	public static void placeBorder(World world, int x, int y, int z) {
		place(world, x, y, z, Block.planks, 4);
	}	

	/**
	 * Rotates a block to a new position given an angle 
	 * @param coords ChunkCoordinates of the block to move
	 * @param direction Angle to rotate, clamped to closest 90 degree value
	 * @param start Minimum x,y,z block of structure
	 * @param size Size of the structure
	 * @return The new location of the block that used to be located at coords
	 */
	public static ChunkCoordinates rotateNew(ChunkCoordinates coords, int direction, Vec3 start, Vec3 size) {

		System.out.println("direction: " + direction);

		//center is not offsetted
		//coords should be offset 0.5 before rotate math, guarantees no strange offset issues, flooring cleans it up afterwards perfectly

		double rotation = (direction * Math.PI/2D);
		double centerX = start.xCoord+(size.xCoord/2D);
		double centerZ = start.zCoord+(size.zCoord/2D);
		double vecX = (coords.posX+0.5D) - centerX;
		double vecZ = (coords.posZ+0.5D) - centerZ;

		Vec3 vec = Vec3.createVectorHelper(vecX, 0, vecZ);
		vec.rotateAroundY((float)rotation);
		return new ChunkCoordinates((int)Math.floor(start.xCoord+vec.xCoord), coords.posY, (int)Math.floor(start.zCoord+vec.zCoord));
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
