package net.cojo.servesup.items;

import net.cojo.servesup.ModInfo;
import net.cojo.servesup.entities.EntityVolleyball;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
        	EntityVolleyball ball = new EntityVolleyball(world, player, true);
        	
        	float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw);
        	System.out.println(yaw);
        	ball.setLocationAndAngles(player.posX, player.posY, player.posZ, 0, 0);
            world.spawnEntityInWorld(ball);
        }

        return stack;
    }

}
