package net.cojo.servesup;
import net.cojo.servesup.blocks.SUBlocks;
import net.cojo.servesup.entities.SUEntityRegistry;
import net.cojo.servesup.items.SUItems;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Main Mod class
 * @author Cojo
 *
 */
@NetworkMod(channels = { "ServesUp" }, clientSideRequired = true, serverSideRequired = false)
@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class ServesUp {
	
	@Instance(ModInfo.MODID)
    public static ServesUp instance;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
     /*   try {
            //config stuff
        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, "Tropicraft has a problem loading it's configuration");
        }*/
    }

    @EventHandler
    public void load(FMLInitializationEvent evt) {
        SUBlocks.init();
        SUItems.init();

        //register entities
        SUEntityRegistry.init();

        //register tick handlers
        //proxy.initTickers();

        //register block render ids
        //proxy.registerBlockRenderIds();

        //register renders for entities
    //    proxy.initRenderRegistry();

        //register renders for tile entities
  //      proxy.registerTESRs();

        //register block render handlers
   //     proxy.registerBlockRenderHandlers();

        //schedule this class for event callbacks
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent evt) {

    }
}
