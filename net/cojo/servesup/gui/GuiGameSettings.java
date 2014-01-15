package net.cojo.servesup.gui;

import net.cojo.servesup.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

public class GuiGameSettings extends GuiScreen {

	/** Court orientation */
	int orientation;

	GuiButton start;
	
	GuiCheckbox check1;
	GuiCheckbox check2;

	public GuiGameSettings(int orientation) {
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
	public void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		
		if (check1.isHovered(par1, par2)) {
			if (check1.checked)
				check2.checked = false;
		} else {
			if (check2.isHovered(par1, par2)) {
				if (check2.checked)
					check1.checked = false;
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

		this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		
		this.fontRenderer.drawStringWithShadow("Mode", check1.xPosition, check1.yPosition - 12, 0xffffff);
		this.fontRenderer.drawStringWithShadow("Regulation", check1.xPosition + 20, check1.yPosition + 3, 0xffffff);
		this.fontRenderer.drawStringWithShadow("Beach Volleyball", check2.xPosition + 20, check2.yPosition + 3, 0xffffff);

		super.drawScreen(i,j,f);
	}

	private void addButtons() {
		buttonList.clear();

		int dx = (int)(mc.displayWidth / 4) - (int)(this.fontRenderer.getStringWidth("Build Court") / 1.5);
		int dy = (int)(mc.displayHeight / 2.5);
		buttonList.add(new GuiButton(2000, dx, dy, 70, 20, "Build Court"));
		check1 = new GuiCheckbox(2001, (int)(mc.displayWidth / 24), (int)(mc.displayHeight / 18));
		buttonList.add(check1);
		check2 = new GuiCheckbox(2002, (int)(mc.displayWidth / 24), (int)(mc.displayHeight / 12));
		buttonList.add(check2);
		//check2.addMutex(check1);
	}
}
