package net.cojo.servesup;

/**
 * A class for holding game state constants
 * @author Cojo
 *
 */
public class GameStates {

	/** Represents the time before the game has started */
	public static final byte PRE_GAME = 0;
	
	/** 
	 * Represents the time when the game is setting players in their correct positions, 
	 * rotating them and whatnot, and preparing the game for the next round 
	 */
	public static final byte PRE_SERVE = 1;

	/** Represents the time when a player has the ball and the game is waiting on a serve */
	public static final byte SERVING = 2;
	
	/** Represents the time when the ball is in play */
	public static final byte IN_GAME = 3;
	
	/** Represents the time when all mid-game operations should be performed */
	public static final byte END_ROUND = 4;
	
	/** Represents the time after a game has finished and a new one has not been started */
	public static final byte POST_GAME = 5;
	
	/**
	 * Get the verbose name for a game state
	 * @param state Game state
	 * @return verbose name
	 */
	public static String stateName(byte state) {
		switch (state) {
		case 0:
			return "PRE_GAME";
		case 1:
			return "PRE_SERVE";
		case 2:
			return "SERVING";
		case 3:
			return "IN_GAME";
		case 4:
			return "END_ROUND";
		case 5:
			return "POST_GAME";
		default:
			return null;
		}
	}
}
