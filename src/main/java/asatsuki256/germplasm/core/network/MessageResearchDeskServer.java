package asatsuki256.germplasm.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageResearchDeskServer implements IMessage {
	
	public int x;
	public int y;
	public int z;
	public int type;
	public String name;
	
	public static final int TYPE_NAME = 1;
	public static final int TYPE_GENERATION_RESET = 2;
	
	public MessageResearchDeskServer() {}
	
	public MessageResearchDeskServer(BlockPos pos, int type, String name) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.type = type;
		this.name = name;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeByte(type);
		int length = this.name.length();
		if(length > 40)length = 40;
		buf.writeByte((byte)length);
		for(int i = 0; i < length; i++){
			buf.writeChar(this.name.charAt(i));
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.type = buf.readByte();
		int length = buf.readByte();
		if(length > 40)length = 40;
		String bufText = "";
		for(int i = 0; i < length; i++){
			bufText += buf.readChar();
		}
		this.name = bufText;
	}
	
}
