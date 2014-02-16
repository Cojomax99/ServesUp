package net.cojo.servesup.entities;

import net.cojo.servesup.ModInfo;
import net.cojo.servesup.ServesUp;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Class for registering entities
 * @author Cojo
 *
 */
public class SUEntityRegistry {

	/** ID for the entity */
	private static int entityId = 0;
	
	/**
	 * Init the volleyball entity
	 */
	public static void init() {
		EntityRegistry.registerModEntity(EntityVolleyball.class, "Volleyball", entityId++, ServesUp.instance, 64, 3, true);
		LanguageRegistry.instance().addStringLocalization(String.format("entity.%s." + "Volleyball" + ".name", ModInfo.MODID), "en_US", "Volleyball");
		
		EntityRegistry.registerModEntity(EntityDummy.class, "Dummy", entityId++, ServesUp.instance, 64, 3, true);
		LanguageRegistry.instance().addStringLocalization(String.format("entity.%s." + "Dummy" + ".name", ModInfo.MODID), "en_US", "Dummy");
	}
}
