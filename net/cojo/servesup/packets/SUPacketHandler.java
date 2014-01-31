package net.cojo.servesup.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class SUPacketHandler implements IPacketHandler {

	public SUPacketHandler() {

	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		System.out.println("Received packet!");
		
		if ("VBall_Settings".equals(packet.channel)) {
			try {
				System.out.println("Made it to the packet handler : D");
				int dimID = dis.readInt();
				int xCoord = dis.readInt();
				int yCoord = dis.readInt();
				int zCoord = dis.readInt();
				NBTTagCompound nbt = Packet.readNBTTagCompound(dis);
				World world = DimensionManager.getWorld(dimID);
				
				if (world != null) {
					TileEntity tEnt = world.getBlockTileEntity(xCoord, yCoord, zCoord);
					if (tEnt instanceof TileEntityGameManager) {
						((TileEntityGameManager) tEnt).handleClientSentNBT(((EntityPlayer)player).username, nbt);
					}
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}
