package net.cojo.servesup;

import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class SUCommands extends CommandBase {
	
	@Override
	public String getCommandName() {
		return "su";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] msg) {
		try {
			EntityPlayer player = getCommandSenderAsPlayer(sender);
			
			if (msg.length > 0) {
				if (msg[0].equalsIgnoreCase("rotate")) {
					int x = MathHelper.floor_double(player.posX);
					int y = MathHelper.floor_double(player.posY - 1);
					int z = MathHelper.floor_double(player.posZ);
					
					if (player.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityGameManager) {
						TileEntityGameManager court = (TileEntityGameManager)player.worldObj.getBlockTileEntity(x, y, z);
						court.rotateTeam(1);
						System.out.println("le rotation!");
					} else {
						System.out.println(x + " " + y + " " + z);
					}
				}
			}
			
		} catch (Exception ex) {
			System.err.println("Exception handling Serves Up! command");
			ex.printStackTrace();
		}

	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		
		//TODO CHANGE DIS!!!
		return true;
	}

}
