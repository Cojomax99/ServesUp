package net.cojo.servesup.physics;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BallPhysicsHelper {

	public static void hitEvent(Entity ball, Entity player, boolean isServe) {

		float hitPower = 0.5F;

		double vecX = ball.posX - player.posX;
		double vecY = ball.posY - player.posY;
		double vecZ = ball.posZ - player.posZ;

		Vec3 vecBallToPlayer = Vec3.createVectorHelper(vecX, vecY, vecZ).normalize();
		Vec3 vecThrust = getHitAim(player).normalize();

		//invert z to fix things
		vecThrust.zCoord *= -1;

		vecThrust.xCoord *= hitPower;
		vecThrust.yCoord *= hitPower;
		vecThrust.zCoord *= hitPower;

		// If it is a serve, don't worry about x and z, just make it go straight up first
		if (!isServe) {
			ball.motionX = vecThrust.xCoord;
			ball.motionZ = vecThrust.zCoord;
		}
		
		ball.motionY = vecThrust.yCoord;

		//more adjustments
		ball.motionY += 0.3F;
	}

	/**
	 * Returns a vector for where the player is looking
	 * @param looker Entity that is looking at the ball
	 * @return Vector of player looking at the ball
	 */
	public static Vec3 getHitAim(Entity looker) {
		float f1 = MathHelper.cos(looker.rotationYaw * 0.017453292F/* - (float)Math.PI*/);
		float f2 = MathHelper.sin(looker.rotationYaw * 0.017453292F/* - (float)Math.PI*/);
		float f3 = -MathHelper.cos(-looker.rotationPitch * 0.017453292F);
		float f4 = MathHelper.sin(-looker.rotationPitch * 0.017453292F);
		return Vec3.createVectorHelper((double)(f2 * f3), (double)f4, (double)(f1 * f3));
	}
}
