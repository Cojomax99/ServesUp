package net.cojo.servesup;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.cojo.servesup.entities.render.SURenderRegistry;
import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.cojo.servesup.tileentity.TileEntityGameManagerRenderer;

public class ClientProxy extends CommonProxy {
	
	/**
	 * Register all tile entity special renderers
	 */
	@Override
	public void registerTESRs() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGameManager.class, new TileEntityGameManagerRenderer());
	}
	
	/**
	 * Registers all needed renders
	 */
	@Override
	public void initRenderRegistry() {
		SURenderRegistry.init();
	}
}
