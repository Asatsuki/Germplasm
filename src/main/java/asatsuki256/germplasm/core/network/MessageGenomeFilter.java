package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.core.item.ItemFilterGenome;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGenomeFilter implements IMessage {
	
	public int pairNum, chromosomeNum, geneNum;
	
	public MessageGenomeFilter() {}
	
	public MessageGenomeFilter(int pairNum, int chromosomeNum, int geneNum) {
		this.pairNum = pairNum;
		this.chromosomeNum = chromosomeNum;
		this.geneNum = geneNum;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pairNum = buf.readInt();
		this.chromosomeNum = buf.readInt();
		this.geneNum = buf.readInt();
	}
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pairNum);
		buf.writeInt(chromosomeNum);
		buf.writeInt(geneNum);
	}
	
	public static class Handler implements IMessageHandler<MessageGenomeFilter, IMessage> {

        @Override
        public IMessage onMessage(final MessageGenomeFilter message, MessageContext ctx) {
        	EntityPlayer player = ctx.getServerHandler().player;
    		int pairNum = message.pairNum;
    		int chromosomeNum = message.chromosomeNum;
    		int geneNum = message.geneNum;
    		ItemStack item = player.getHeldItem(EnumHand.MAIN_HAND);
    		if (item.getItem() instanceof ItemFilterGenome) {
    			ItemStack newItem = ItemFilterGenome.setStats(item, pairNum, chromosomeNum, geneNum);
    			player.setHeldItem(EnumHand.MAIN_HAND, newItem);
    		}
    		return null;
        }
    }

}
