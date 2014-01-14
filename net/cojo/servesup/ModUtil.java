package net.cojo.servesup;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ModUtil {

	public ModUtil() {
		
	}
	
	/**
     * Bind resource
     * @param texLoc Texture location (eg blocks/chest.png)
     */
    public static ResourceLocation bindTextureMod(String texLoc) {
        ResourceLocation res = new ResourceLocation(ModInfo.MODID, String.format("textures/%s.png", texLoc));
        Minecraft.getMinecraft().renderEngine.bindTexture(res);

        return res;
    }
	
	/**
     * Bind resource (specially crafted for entities)
     * @param texName Texture location (eg chest.png)
     * @return
     */
    public static ResourceLocation bindTextureEntity(String texName) {
        return bindTextureMod(String.format("entities/%s", texName));
    }
    
    /**
     * Bind resource (specially crafted for gui overlays)
     * @param texName Texture location (eg chest.png)
     * @return
     */
    public static ResourceLocation bindTextureModGui(String texName) {
        ResourceLocation res = new ResourceLocation(ModInfo.MODID, String.format("gui/%s.png", texName));
        Minecraft.getMinecraft().renderEngine.bindTexture(res);

        return res;
    }

}
