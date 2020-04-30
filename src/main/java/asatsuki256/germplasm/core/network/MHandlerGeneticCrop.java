package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MHandlerGeneticCrop implements IMessageHandler<MessageGeneticCrop, IMessage> {
	
	@Override
	public IMessage onMessage(MessageGeneticCrop message, MessageContext ctx) {
		EntityPlayer player = GermplasmCore.proxy.getClientPlayer();
		int x = message.x;
		int y = message.y;
		int z = message.z;
		if(player != null) {
			TileEntity tile = player.world.getTileEntity(new BlockPos(x, y, z));
			if(tile != null && tile instanceof TileGeneticCrop) {
				((TileGeneticCrop)tile).setGrowthStage(message.growthStage);
			}
		}
		
		return null;
	}

}
