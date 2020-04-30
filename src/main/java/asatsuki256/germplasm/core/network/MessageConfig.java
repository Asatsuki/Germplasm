package asatsuki256.germplasm.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageConfig implements IMessage {
	
	public boolean requireEnergy;
	
	public MessageConfig() {}
	
	public MessageConfig(boolean requireEnergy) {
		this.requireEnergy = requireEnergy;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(requireEnergy);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.requireEnergy = buf.readBoolean();
	}

}
