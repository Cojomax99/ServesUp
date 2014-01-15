package net.cojo.servesup.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

public class GuiBuildCourt extends GuiScreen {

	/** Court orientation */
	int orientation;
	
	GuiButton start;
	
	public GuiBuildCourt(int orientation) {
		this.orientation = orientation;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui(){
		addButtons();
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		
		//this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xff, 0xff);
		this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		
		GL11.glPushMatrix();
		GL11.glScalef(0.3F, 0.3F, 1.0F);
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		
	//	if (orientation == 0 || orientation == 2)
			
		//else
		//	GL11.glScalef(0.4F, 0.5F, 1.0F);
		
		for (int a = 1; a < 12; a++) {
			for (int b = 1; b < 26; b++) {
				Icon icon = a == 1 || b == 1 || a == 11 || b == 25 ? Block.wood.getBlockTextureFromSide(1) : Block.sand.getBlockTextureFromSide(1);
				
				if (orientation == 0 || orientation == 2) {
					this.drawTexturedModelRectFromIcon((int)(sr.getScaledWidth() - 50 * 6.5) + 16 * a, (int)(sr.getScaledHeight() / 5.5) + 16 * b, icon, 16, 16);
				} else {
					this.drawTexturedModelRectFromIcon((int)(sr.getScaledWidth() + 30 * 6.5) + 16 * b, (int)(sr.getScaledHeight() / 5.5) + 16 * a, icon, 16, 16);
				}
			}
		}
		
		GL11.glPopMatrix();
		
		super.drawScreen(i,j,f);
	}

	private void addButtons() {
		buttonList.clear();

		int dx = (int)(mc.displayWidth / 4) - (int)(this.fontRenderer.getStringWidth("Build Court") / 1.5);
		int dy = (int)(mc.displayHeight / 2.5);
		buttonList.add(new GuiButton(2001, dx, dy, 70, 20, "Build Court"));
		//buttonList.add(new GuiDirectionalButton(2000, 0, 0, width, height, "", 3));
	}

}
