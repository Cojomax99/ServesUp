package net.cojo.servesup.gui;

import net.cojo.servesup.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDirectionalButton extends GuiButton {

	/** Direction this button should be
	 * <li> 0 = up
	 * <li> 1 = right
	 * <li> 2 = down
	 * <li> 3 = left 
	 */
	public int direction;

	public GuiDirectionalButton(int par1, int par2, int par3, String par4Str, int direction) {
		super(par1, par2, par3, par4Str);
		this.direction = direction;
	}

	public GuiDirectionalButton(int par1, int par2, int par3, int par4, int par5, String par6Str, int direction) {
		super(par1, par2, par3, par4, par5, par6Str);
		this.direction = direction;
	}


    /**
     * Draws this button to the screen.
     */
	@Override
    public void drawButton(Minecraft mc, int x, int y) {
		RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        GL11.glPushMatrix();
        ModUtil.bindTextureModGui("icons");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
       // int k = getHoverState(flag);
        GL11.glScalef(1.35F, 1.35F, 1.35F);
     //TODO   drawTexturedModalRect((int)(xPosition/1.35), (int)(yPosition/1.35), direction *16 + 128 + (k -1)*8 , 240, 8, 16);
        
        drawTexturedModalRect((int)(xPosition/1.35), (int)(yPosition/1.35), getImageX() , getImageY(), getImageWidth(), getImageHeight());
        
        GL11.glPopMatrix();
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	}
	
	/**
	 * 
	 * @return u coordinate on image
	 */
	private int getImageX() {
		return direction;
	}
	
	/**
	 * 
	 * @return v coordinate on image
	 */
	private int getImageY() {
		return direction;
	}
	
	/**
	 * 
	 * @return width on image from u coordinate
	 */
	private int getImageWidth() {
		return direction;
	}
	
	/**
	 * 
	 * @return height on image from v coordinate
	 */
	private int getImageHeight() {
		return direction;
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int i, int j) {
		return enabled && drawButton && i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
	}
}
