package net.cojo.servesup.gui;

import java.util.ArrayList;
import java.util.List;

import net.cojo.servesup.packets.PacketHelper;
import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;

public class GuiGameSettings extends GuiScreen {

	/** Court orientation */
	int orientation;

	GuiButton start;

	GuiCheckbox check1;
	GuiCheckbox check2;

	GuiCheckbox score1;
	GuiCheckbox score2;

	GuiTextField customScore;

	GuiTextField tf_team1;
	GuiTextField tf_team2;

	List<String> team1;
	List<String> team2;

	//	GuiPlayerSlot team1Slot;
	//	GuiPlayerSlot team2Slot;

	String selectedName;
	
	TileEntityGameManager court;

	public GuiGameSettings(TileEntityGameManager court, int orientation, List<String> team1, List<String> team2) {
		this.orientation = orientation;
		this.team1 = new ArrayList<String>(team1);
		this.team2 = new ArrayList<String>(team2);
		this.court = court;
	}

	public void setSelectedName(String name) {
		selectedName = new String(name);
	}

	public String getSelectedName() {
		return this.selectedName;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui(){
		Keyboard.enableRepeatEvents(true);
		addButtons();
		addTextFields();
		/*	this.team1Slot = new GuiPlayerSlot(this, (int)(width / 2) - 50, 1);
		this.team1Slot.registerScrollButtons(7, 8);

		this.team2Slot = new GuiPlayerSlot(this, (int)(width / 2) + 50, 2);
		this.team2Slot.registerScrollButtons(17, 18);*/
	}


	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			NBTTagCompound nbt = new NBTTagCompound();
			try {
				nbt.setShort("FinalScore", score1.checked ? TileEntityGameManager.REGULATION_SCORE : Short.parseShort(this.customScore.getText()));
				nbt.setByte("GameMode", this.check1.checked ? (byte)0 : (byte)1);
				nbt.setString("Team1Name", tf_team1.getText().length() > 0 ? tf_team1.getText() : "Team 1");
				nbt.setString("Team2Name", tf_team2.getText().length() > 0 ? tf_team2.getText() : "Team 2");
				nbt.setBoolean("CourtBuilt", Boolean.valueOf(true));
				PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntCommand(court, nbt));
				System.out.println("Made it to send packet : D");
				this.mc.thePlayer.closeScreen();
			} catch (NumberFormatException nfe) {
				Minecraft.getMinecraft().thePlayer.addChatMessage("Game Score must be an integer!");
			}
		}
	}

	private void addTextFields() {
		customScore = new GuiTextField(this.fontRenderer, score2.xPosition + 13, score2.yPosition + 22, 40, 30);
		customScore.setVisible(true);
		this.customScore.setMaxStringLength(4);
		this.customScore.setEnableBackgroundDrawing(false);
		this.customScore.setFocused(false);
		this.customScore.setText("0");
		this.customScore.setCanLoseFocus(true);

		tf_team1 = new GuiTextField(this.fontRenderer, 42, score2.yPosition + 70, 80, 13);
		tf_team1.setVisible(true);
		this.tf_team1.setMaxStringLength(15);
		this.tf_team1.setEnableBackgroundDrawing(false);
		this.tf_team1.setFocused(false);
		this.tf_team1.setText("Team 1");
		this.tf_team1.setCanLoseFocus(true);

		tf_team2 = new GuiTextField(this.fontRenderer, width - 118, score2.yPosition + 70, 80, 13);
		tf_team2.setVisible(true);
		this.tf_team2.setMaxStringLength(15);
		this.tf_team2.setEnableBackgroundDrawing(false);
		this.tf_team2.setFocused(false);
		this.tf_team2.setText("Team 2");
		this.tf_team2.setCanLoseFocus(true);
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3) {
		this.customScore.mouseClicked(par1, par2, par3);
		this.tf_team1.mouseClicked(par1, par2, par3);
		this.tf_team2.mouseClicked(par1, par2, par3);

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

		if (check1.isHovered(par1, par2)) {
			check1.checked = true;
		} else {
			if (check2.isHovered(par1, par2)) {
				check2.checked = true;
			}
		}

		if (score1.isHovered(par1, par2)) {
			if (score1.checked)
				score2.checked = false;
		} else {
			if (score2.isHovered(par1, par2)) {
				if (score2.checked)
					score1.checked = false;
			}
		}

		if (score1.isHovered(par1, par2)) {
			score1.checked = true;
		} else {
			if (score2.isHovered(par1, par2)) {
				score2.checked = true;
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

		this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);

		try {
			//	this.team1Slot.drawScreen(i, j, f);
			//	this.team2Slot.drawScreen(i, j, f);

			// Draw border around textbox, then draw textbox
			this.drawRect(score2.xPosition + 11, score2.yPosition + 18, score2.xPosition + 43, score2.yPosition + 19, 0xffffffff);
			this.drawRect(score2.xPosition + 11, score2.yPosition + 33, score2.xPosition + 43, score2.yPosition + 34, 0xffffffff);
			this.drawRect(score2.xPosition + 10, score2.yPosition + 18, score2.xPosition + 11, score2.yPosition + 34, 0xffffffff);
			this.drawRect(score2.xPosition + 42, score2.yPosition + 18, score2.xPosition + 43, score2.yPosition + 34, 0xffffffff);
			this.drawRect(score2.xPosition + 12, score2.yPosition + 20, score2.xPosition + 42, score2.yPosition + 32, Integer.MIN_VALUE);

			// Draw border around textbox, then draw textbox
			this.drawRect(score2.xPosition + 11, score2.yPosition + 18, score2.xPosition + 43, score2.yPosition + 19, 0xffffffff);
			this.drawRect(score2.xPosition + 11, score2.yPosition + 33, score2.xPosition + 43, score2.yPosition + 34, 0xffffffff);
			this.drawRect(score2.xPosition + 10, score2.yPosition + 18, score2.xPosition + 11, score2.yPosition + 34, 0xffffffff);
			this.drawRect(score2.xPosition + 42, score2.yPosition + 18, score2.xPosition + 43, score2.yPosition + 34, 0xffffffff);
			this.drawRect(40, score2.yPosition + 67, 120, score2.yPosition + 80, Integer.MIN_VALUE);

			// Draw border around textbox, then draw textbox
			this.drawRect(score2.xPosition + 11, score2.yPosition + 18, score2.xPosition + 43, score2.yPosition + 19, 0xffffffff);
			this.drawRect(score2.xPosition + 11, score2.yPosition + 33, score2.xPosition + 43, score2.yPosition + 34, 0xffffffff);
			this.drawRect(score2.xPosition + 10, score2.yPosition + 18, score2.xPosition + 11, score2.yPosition + 34, 0xffffffff);
			this.drawRect(score2.xPosition + 42, score2.yPosition + 18, score2.xPosition + 43, score2.yPosition + 34, 0xffffffff);
			this.drawRect(width - 120, score2.yPosition + 67, width - 40, score2.yPosition + 80, Integer.MIN_VALUE);

			this.fontRenderer.drawStringWithShadow("Mode", check1.xPosition, check1.yPosition - 12, 0xffffff);
			this.fontRenderer.drawStringWithShadow("Regulation", check1.xPosition + 20, check1.yPosition + 3, 0xffffff);
			this.fontRenderer.drawStringWithShadow("Beach Volleyball", check2.xPosition + 20, check2.yPosition + 3, 0xffffff);
			this.fontRenderer.drawStringWithShadow("Score to play to:", check1.xPosition, check2.yPosition + 35, 0xffffff);
			this.fontRenderer.drawStringWithShadow("Regulation", score1.xPosition + 20, score1.yPosition + 3, 0xffffff);
			this.fontRenderer.drawStringWithShadow("Custom:", score2.xPosition + 20, score2.yPosition + 3, 0xffffff);

			this.fontRenderer.drawStringWithShadow("Team 1 Name:", 47, score2.yPosition + 50, 0xffffff);
			this.fontRenderer.drawStringWithShadow("Team 2 Name:", width - 113, score2.yPosition + 50, 0xffffff);

			this.fontRenderer.drawStringWithShadow("Team 1", (int)(width / 2) + 10, 20, 0xaa00aa);
			this.fontRenderer.drawStringWithShadow("Team 2", (int)(width / 2) + 90, 20, 0xffaa00);

			drawPlayerNames();

			this.customScore.drawTextBox();
			this.tf_team1.drawTextBox();
			this.tf_team2.drawTextBox();
		} catch (NullPointerException npe) {

		}

		super.drawScreen(i,j,f);
	}

	private void drawPlayerNames() {
		for (int i = 0; i < team1.size(); i++) {
			this.fontRenderer.drawStringWithShadow(team1.get(i), (int)(width / 2) + 10, 35 + 20 * i, 0xffffff);
		}

		for (int i = 0; i < team2.size(); i++) {
			this.fontRenderer.drawStringWithShadow(team2.get(i), (int)(width / 2) + 90, 35 + 20 * i, 0xffffff);
		}
	}

	private void addButtons() {
		buttonList.clear();

		buttonList.add(new GuiButton(2000, this.width / 2 - (int)(this.fontRenderer.getStringWidth("Start Game") / 1.7), this.height / 4 + 96 + 40, 70, 20, "Start Game"));
		check1 = new GuiCheckbox(2001, (int)(width / 24), (int)(height / 8));
		check1.checked = true;
		buttonList.add(check1);
		check2 = new GuiCheckbox(2002, (int)(width / 24), check1.yPosition + 15);
		buttonList.add(check2);

		score1 = new GuiCheckbox(2003, (int)(width / 24), check2.yPosition + 47);
		score1.checked = true;
		buttonList.add(score1);
		score2 = new GuiCheckbox(2004, (int)(width / 24), score1.yPosition + 15);
		buttonList.add(score2);

	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	public void keyTyped(char par1, int par2) {    	
		if (par2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
		} else if (par2 != 28 && par2 != 156) {
			this.customScore.textboxKeyTyped(par1, par2);
			this.tf_team1.textboxKeyTyped(par1, par2);
			this.tf_team2.textboxKeyTyped(par1, par2);
		} else {
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

	public Minecraft getMC() {
		return this.mc;
	}

	public FontRenderer getFR() {
		return this.fontRenderer;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		this.customScore.updateCursorCounter();
		this.tf_team1.updateCursorCounter();
		this.tf_team2.updateCursorCounter();
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		this.team1.clear();
		this.team2.clear();
	}

	public List<String> getTeam1List() {
		return this.team1;
	}

	public List<String> getTeam2List() {
		return this.team2;
	}

}
