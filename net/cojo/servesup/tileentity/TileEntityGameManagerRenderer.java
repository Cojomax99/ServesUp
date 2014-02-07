package net.cojo.servesup.tileentity;

import net.cojo.servesup.court.Overlays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

public class TileEntityGameManagerRenderer extends TileEntitySpecialRenderer {

	public TileEntityGameManagerRenderer() {

	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileEntityGameManager court = (TileEntityGameManager)tileentity;

		// No point rendering the lines if the court isn't there yet!
		if (court.isCourtBuilt()) {
			AxisAlignedBB bounds = court.getCourtRenderBounds();
			//	ChunkCoordinates bb = PositionHelper.getStartPosition(court.getTeamOneBounds(), court.getOrientation(), 0, 1);
			//	System.out.println(bounds == null);
			if (bounds != null) {
				GL11.glPushMatrix();
				GL11.glTranslated(-court.xCoord + d0, -court.yCoord + d1, -court.zCoord + d2);

				// Draw outer border
				drawOutlinedBoundingBox2(bounds, 10F);

				// Draw middle line
				drawLine(court.getCenterLineBounds(), 10F);

				// Draw front zone lines
				drawLine(court.getFrontZoneLineBounds()[0], 10F);
				drawLine(court.getFrontZoneLineBounds()[1], 10F);

				// DEBUG
				drawOutlinedBoundingBox(court.getCourtActualBounds(), 10F);

				if (!court.activeIDs.isEmpty()) {
					Entity e = court.worldObj.getEntityByID(court.activeIDs.get(0));
					if (e != null) {
			//			this.renderLivingLabel(court, "Team " + court.getTeam(e.entityId), court.xCoord, court.yCoord, court.zCoord, 0xff0000);
					}
				}

				for (int i = 0; i < 5/*court.team2.size()*/; i++) {
					Vec3 vecPl = court.getSpawnPosition(i, 2);
					
					if (vecPl == null)
						continue;

					this.renderLivingLabel(court, "Player "  + i, vecPl.xCoord, vecPl.yCoord, vecPl.zCoord, 0xff0000);
				}

			/*	for (int i = 0; i < court.team1.size(); i++) {
					Vec3 vecPl = court.getSpawnPosition(i, 1);
					
					if (vecPl == null)
						continue;

					this.renderLivingLabel(court, "Player "  + i, vecPl.xCoord, vecPl.yCoord, vecPl.zCoord, 0xff0000);
				}*/

				//Overlays.renderLineFromToBlock(vecPl.xCoord, vecPl.yCoord, vecPl.zCoord, vecPl.xCoord, vecPl.yCoord+3, vecPl.zCoord, 0xFFFFFF);
				//System.out.println(vecPl.xCoord + " " + vecPl.yCoord + " " + vecPl.zCoord);


				if (court.team1Name != null) {
					this.renderLivingLabel(court, String.format("Team 1 Name: %s, Team 2 name: %s", court.team1Name, court.team2Name), court.xCoord, court.yCoord, court.zCoord, 0xff00ff);
				}

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
	private void drawOutlinedBoundingBox2(AxisAlignedBB par1AxisAlignedBB, float width) {
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
		tessellator.startDrawing(1);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.draw();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * Draws the debug or playername text above a living
	 */
	private void renderLivingLabel(TileEntityGameManager court, String par2Str, double par3, double par5, double par7, int par9) {
		if (true) {
			FontRenderer fontrenderer = this.getFontRenderer();
			RenderManager rm = RenderManager.instance;
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.5F, (float)par7);
			
			//      GL11.glRotatef(-court.ge, 0.0F, 1.0F, 0.0F);
			//     GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		//	GL11.glRotatef(-Minecraft.getMinecraft().thePlayer.cameraPitch, 0.0F, 1.0F, 0.0F);
		//	GL11.glRotatef(Minecraft.getMinecraft().thePlayer.cameraYaw, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-rm.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rm.playerViewX, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glScalef(-f1, -f1, f1);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator = Tessellator.instance;
			byte b0 = 0;
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator.startDrawingQuads();
			int j = fontrenderer.getStringWidth(par2Str) / 2;
			tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
			tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
			tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
			tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
			tessellator.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, b0, 553648127);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, b0, -1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

}
