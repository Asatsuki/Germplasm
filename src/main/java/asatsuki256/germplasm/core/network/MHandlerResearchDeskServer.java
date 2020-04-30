package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.tileentity.TileResearchDesk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MHandlerResearchDeskServer implements IMessageHandler<MessageResearchDeskServer, IMessage> {
	
	@Override
	public IMessage onMessage(MessageResearchDeskServer message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		int x = message.x;
		int y = message.y;
		int z = message.z;
		TileEntity tile = player.world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null && tile instanceof TileResearchDesk) {
			ItemStack seedStack = ((TileResearchDesk)tile).getStackInSlot(0);
			NBTTagCompound nbt = seedStack.getTagCompound();
			IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
			if(message.type == MessageResearchDeskServer.TYPE_NAME) {
				String name = message.name;
				if(name == null || name.isEmpty()) {
					unit.setName(null);
				} else {
					unit.setName(name);
				}
			} else if (message.type == MessageResearchDeskServer.TYPE_GENERATION_RESET) {
				if(unit instanceof IGenome) {
					((IGenome) unit).resetGeneration();
				}
			}
			GeneAPI.nbtHelper.setUnitToindividualNBT(nbt, unit);
			seedStack.setTagCompound(nbt);
		}
		return null;
	}

}
