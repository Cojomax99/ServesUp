package src.packages.net.cojo.servesup.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import src.packages.net.cojo.servesup.ModInfo;

public class ItemVolleyball extends Item {

	/** Name of the image for the volleyball item */
	private String imageName;
	
	public ItemVolleyball(int id) {
		super(id);
	}
	
	public ItemVolleyball(int id, String name) {
		super(id);
		this.imageName = name;
	}
	
    /**
     * Register all Icons used in this item
     */
    @Override
    public void registerIcons(IconRegister iconRegistry) {
        this.itemIcon = iconRegistry.registerIcon(ModInfo.ICONLOCATION + imageName);
    }

}
