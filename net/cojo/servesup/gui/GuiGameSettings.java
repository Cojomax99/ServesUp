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
	public void drawScreen(int i, int j, float f) {
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

		this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		
		this.fontRenderer.drawStringWithShadow("Mode", 10, 10, 0xffffff);

		super.drawScreen(i,j,f);
	}

	private void addButtons() {
		buttonList.clear();

		int dx = (int)(mc.displayWidth / 4) - (int)(this.fontRenderer.getStringWidth("Build Court") / 1.5);
		int dy = (int)(mc.displayHeight / 2.5);
		buttonList.add(new GuiButton(2001, dx, dy, 70, 20, "Build Court"));
		buttonList.add(new GuiCheckbox(2000, 100, 100));
	}
}
