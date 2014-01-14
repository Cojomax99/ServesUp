package net.cojo.servesup.gui;

import net.minecraft.client.gui.GuiScreen;

public class GuiBuildCourt extends GuiScreen {

	public GuiBuildCourt() {
		
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui(){
        addButtons();
    }
    
    private void addButtons() {
    	buttonList.clear();
    	
    	
    }

}
