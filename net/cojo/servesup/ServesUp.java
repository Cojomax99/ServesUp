package net.cojo.servesup;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Main Mod class
 * @author Cojo
 *
 */
@NetworkMod(channels = { "ServesUp" }, clientSideRequired = true, serverSideRequired = false)
@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class ServesUp {

}
