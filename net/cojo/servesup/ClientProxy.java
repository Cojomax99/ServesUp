package net.cojo.servesup;

import net.cojo.servesup.entities.render.SURenderRegistry;

public class ClientProxy extends CommonProxy {
	/**
	 * Registers all needed renders
	 */
	@Override
	public void initRenderRegistry() {
		SURenderRegistry.init();
	}
}
