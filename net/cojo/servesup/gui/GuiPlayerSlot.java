package net.cojo.servesup.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class GuiPlayerSlot extends GuiSlot {

	GuiGameSettings gooey;
	int teamNum;
	
	public GuiPlayerSlot(GuiGameSettings gui, int teamNumber) {
		super(gui.getMC(), gui.width, gui.height, 32, gui.height - 65 + 4, 18);
		this.gooey = gui;
		this.teamNum = teamNumber;
	}

	@Override
	protected int getSize() {
		return teamNum == 1 ? gooey.getTeam1List().size() + 1 : gooey.getTeam2List().size() + 1;
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
		gooey.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		gooey.drawCenteredString(gooey.getFR(), "TEST", gooey.width / 2, k + 1, 16777215);
	}

}
