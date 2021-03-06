package net.cojo.servesup.gui;

import java.util.ArrayList;
import java.util.List;

import net.cojo.servesup.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

public class GuiCheckbox extends GuiButton {

	/** Is this checkbox checked currently? */
	boolean checked;
	
	/** List of checkboxes to maintain as mutex to this one */
	List<GuiCheckbox> mutexList;

	public GuiCheckbox(int par1, int par2, int par3) {
		this(par1, par2, par3, 12, 12);
	}

	public GuiCheckbox(int i, int x, int y, int w, int h) {
		super(i, x, y, w, h, "");
		mutexList = new ArrayList<GuiCheckbox>();
		checked = false;
	}
	
	public void addMutex(GuiCheckbox checkbox) {
		if (mutexList == null)
			mutexList = new ArrayList<GuiCheckbox>();
		mutexList.add(checkbox);
	}

	public int getHoverState(boolean flag) {
		return enabled ? 0 : flag ? 2 : 1;
	}

	@Override
	public void drawButton(Minecraft minecraft, int i, int j) {
		if (!drawButton)
			return;

		ModUtil.bindTextureModGui("loadingscreenguibuttons");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k = getHoverState(isHovered(i, j));
		drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);

		if (!enabled || checked)
			drawTexturedModalRect(xPosition, yPosition, 12, 12, width, height);
		else 
			if (isHovered(i, j))
				drawTexturedModalRect(xPosition, yPosition, 12, 0, width, height);
	}
	
	/**
	 * Is the mouse hovering over this checkbox?
	 * @param i x coord of mouse
	 * @param j y coord of mouse
	 * @return Returns whether the mouse is hovering over this checkbox
	 */
	public boolean isHovered(int i, int j) {
		return i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int i, int j) {		
		if (isHovered(i, j)) {
			checked = !checked;
		}
		
		if (checked) {
			for (GuiCheckbox checkbox : mutexList) {
				checkbox.checked = false;
			}
		}
		
		return enabled && drawButton && isHovered(i, j);
	}

}
