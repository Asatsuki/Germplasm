package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.tileentity.TileGenomeAnalyzer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MHandlerGenomeAnalyzer implements IMessageHandler<MessageGenomeAnalyzer, IMessage>{
	
	@Override
	public IMessage onMessage(MessageGenomeAnalyzer message, MessageContext ctx) {
		EntityPlayer player = GermplasmCore.proxy.getClientPlayer();
		int x = message.x;
		int y = message.y;
		int z = message.z;
		TileEntity tile = player.world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null && tile instanceof TileGenomeAnalyzer) {
			((TileGenomeAnalyzer)tile).progress = message.progress;
			((TileGenomeAnalyzer)tile).maxProgress = message.maxProgress;
		}
		return null;
	}

}
