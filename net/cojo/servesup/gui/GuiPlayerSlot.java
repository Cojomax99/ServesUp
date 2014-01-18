package net.cojo.servesup.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class GuiPlayerSlot extends GuiCustomSlot {

	GuiGameSettings gooey;
	int teamNum;
	
	public GuiPlayerSlot(GuiGameSettings gui, int left, int teamNumber) {
		super(gui.getMC(), left, gui.width, gui.height, 32, 150, 15);
		this.gooey = gui;
		this.teamNum = teamNumber;
	}
	
	protected void overlayBackground(int par1, int par2, int par3, int par4) {
		
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected int getSize() {
		return teamNum == 1 ? gooey.getTeam1List().size() + 4 : gooey.getTeam2List().size() + 4;
	}

	@Override
	protected void elementClicked(int i, boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isSelected(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void drawBackground() {
	//	gooey.drawDefaultBackground();
	}
	
    protected void drawContainerBackground(Tessellator tess) {
        this.gooey.getMC().getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float height = 32.0F;
        tess.startDrawingQuads();
        tess.setColorOpaque_I(2105376);
    //    int left = 200;
   //     int right = 250;
        tess.addVertexWithUV((double)left,  (double)bottom, 0.0D, (double)(left  / height), (double)((bottom + 0) / height));
        tess.addVertexWithUV((double)right, (double)bottom, 0.0D, (double)(right / height), (double)((bottom + 0) / height));
        tess.addVertexWithUV((double)right, (double)top,    0.0D, (double)(right / height), (double)((top    + 0) / height));
        tess.addVertexWithUV((double)left,  (double)top,    0.0D, (double)(left  / height), (double)((top    + 0) / height));
        tess.draw();
    }

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		gooey.drawCenteredString(gooey.getFR(), "Player", (int)((right + left) / 2), k + 1, 16777215);
	}

}
