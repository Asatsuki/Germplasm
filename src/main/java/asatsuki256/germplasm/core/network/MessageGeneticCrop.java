package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageGeneticCrop implements IMessage {
	
	public int x;
	public int y;
	public int z;
	public int growthStage;
	
	public MessageGeneticCrop(){}
	
	public MessageGeneticCrop(TileGeneticCrop tile){
		this.x = tile.getPos().getX();
		this.y = tile.getPos().getY();
		this.z = tile.getPos().getZ();
		this.growthStage = tile.getGrowthStage();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(growthStage);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.growthStage = buf.readInt();
	}
	

}
