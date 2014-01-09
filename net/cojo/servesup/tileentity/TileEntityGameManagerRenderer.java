package net.cojo.servesup.tileentity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityGameManagerRenderer extends TileEntitySpecialRenderer {

	public TileEntityGameManagerRenderer() {

	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileEntityGameManager court = (TileEntityGameManager)tileentity;

		// No point rendering the lines if the court isn't there yet!
		if (court.isCourtBuilt()) {
			AxisAlignedBB bounds = court.getCourtRenderBounds();
			if (bounds != null) {
				GL11.glPushMatrix();
				GL11.glTranslated(-court.xCoord + d0, -court.yCoord + d1, -court.zCoord + d2);
				
				// Draw outer border
				drawOutlinedBoundingBox(bounds, 10F);
				
				// Draw middle line
				drawLine(court.getCenterLineBounds(), 10F);
				
				// Draw front zone lines
				drawLine(court.getFrontZoneLineBounds()[0], 10F);
				drawLine(court.getFrontZoneLineBounds()[1], 10F);
				
				GL11.glTranslated(court.xCoord + d0, court.yCoord + d1, court.zCoord + d2);
				GL11.glPopMatrix();
			}
		}
	}

	/**
	 * Draws a line around the given AABB with a specified line width
	 * @param aabb AABB to render about
	 * @param width Line width
	 */
	private void drawLine(AxisAlignedBB aabb, float width) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		GL11.glLineWidth(width);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		tessellator.startDrawing(3);
		tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * Draws lines for the edges of the bounding box.
	 */
	private void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB, float width) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		GL11.glLineWidth(width);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		//GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		tessellator.startDrawing(3);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		tessellator.startDrawing(3);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		/*        tessellator.startDrawing(1);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);*/
		/*        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);*/
		//   tessellator.draw();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
