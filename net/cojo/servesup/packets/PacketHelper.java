package net.cojo.servesup.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Utility class aimed at sending packets with ease, taken from CoroAI with permission
 * @author Cojo
 *
 */
public class PacketHelper {

	@SideOnly(Side.CLIENT)
	public static void sendClientPacket(Packet packet) {
		FMLClientHandler.instance().getClient().thePlayer.sendQueue.addToSendQueue(packet);
	}

	public static Packet250CustomPayload createPacketForTEntCommand(TileEntity tEnt, NBTTagCompound data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); //was ...(140);
		DataOutputStream dos = new DataOutputStream(bos);

		try
		{
			dos.writeInt(tEnt.worldObj.provider.dimensionId);
			dos.writeInt(tEnt.xCoord);
			dos.writeInt(tEnt.yCoord);
			dos.writeInt(tEnt.zCoord);
			writeNBTTagCompound(data, dos);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "VBall_GameSettings";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();

		return pkt;
	}

	public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException
	{
		if (par0NBTTagCompound == null)
		{
			par1DataOutputStream.writeShort(-1);
		}
		else
		{
			byte[] abyte = CompressedStreamTools.compress(par0NBTTagCompound);
			par1DataOutputStream.writeShort((short)abyte.length);
			par1DataOutputStream.write(abyte);
		}
	}

}
