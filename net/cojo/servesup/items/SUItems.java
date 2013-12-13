package net.cojo.servesup.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class SUItems {
	
	public static ItemVolleyball volleyball;
	
	public static void init() {
		volleyball = (ItemVolleyball) new ItemVolleyball(4000, "volleyball").setUnlocalizedName("suvolleyball").setCreativeTab(CreativeTabs.tabMisc);
		
		registerItem(volleyball, "Volleyball");
	}
	
	private static void registerItem(Item item, String name) {
        GameRegistry.registerItem(item, name);
        LanguageRegistry.addName(item, name);
    }
}
