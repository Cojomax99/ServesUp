package net.cojo.servesup.items;

import net.cojo.servesup.ModInfo;
import net.cojo.servesup.entities.EntityVolleyball;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote) {
        	EntityVolleyball ball = new EntityVolleyball(par2World, par3EntityPlayer);
        	ball.setLocationAndAngles(par3EntityPlayer.posX, par3EntityPlayer.posY + 10, par3EntityPlayer.posZ, 0, 0);
            par2World.spawnEntityInWorld(ball);
        }

        return par1ItemStack;
    }

}
