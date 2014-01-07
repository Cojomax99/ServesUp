package net.cojo.servesup.court;

/**
 * Simple class to hold the min and max coordinate data points
 * @author Cojo
 *
 */
public class CourtData {
	
	/** Smallest x coord on the court */
	public int minX;
	
	/** Largest x coord on the court */
	public int maxX;
	
	/** y-level of the court */
	public int y;
	
	/** Smallest z coord on the court */
	public int minZ;
	
	/** Largest z coord on the court */
	public int maxZ;
	
	public CourtData(int minX, int maxX, int y, int minZ, int maxZ) {
		this.minX = minX;
		this.maxX = maxX;
		this.y = y;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}
	
}
