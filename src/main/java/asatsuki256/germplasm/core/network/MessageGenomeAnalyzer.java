package asatsuki256.germplasm.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageGenomeAnalyzer implements IMessage{
	
	public int x;
	public int y;
	public int z;
	public int progress;
	public int maxProgress;
	
	public MessageGenomeAnalyzer() {}
	
	public MessageGenomeAnalyzer(BlockPos pos, int progress, int maxProgress) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.progress = progress;
		this.maxProgress = maxProgress;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(progress);
		buf.writeInt(maxProgress);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.progress = buf.readInt();
		this.maxProgress = buf.readInt();
	}

}
