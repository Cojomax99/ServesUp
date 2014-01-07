package net.cojo.servesup.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;

public class SUBlocks {
	
	public static Block net;
	
	public static void init() {
		net = (new BlockNet(3000, "iron_bars", "iron_bars", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setUnlocalizedName("net");
		
		GameRegistry.registerBlock(net, "Net");
		LanguageRegistry.addName(net, "Vball net");
	}
}
