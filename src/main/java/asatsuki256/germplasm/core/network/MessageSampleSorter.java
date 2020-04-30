package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.tileentity.TileSampleSorter;
import asatsuki256.germplasm.core.util.GmpmUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSampleSorter implements IMessage {
	
	public int x;
	public int y;
	public int z;
	public MessageType type;
	
	public MessageSampleSorter() {}
	
	public MessageSampleSorter(BlockPos pos, MessageType type) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.type = type;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.type = MessageType.values()[buf.readByte()];
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeByte(type.ordinal());
	}
	
	public enum MessageType {
		AUTO_INPUT, AUTO_OUTPUT, SOURCE_FACING, MATCHED_FACING, REMAINING_FACING
	}
	
	public static class Handler implements IMessageHandler<MessageSampleSorter, IMessage> {

        @Override
        public IMessage onMessage(final MessageSampleSorter message, MessageContext ctx) {
        	EntityPlayer player = ctx.getServerHandler().player;
    		int x = message.x;
    		int y = message.y;
    		int z = message.z;
    		MessageType type = message.type;
    		TileEntity tile = player.world.getTileEntity(new BlockPos(x, y, z));
			if(tile != null && tile instanceof TileSampleSorter) {
				TileSampleSorter sorter = (TileSampleSorter) tile;
				switch (type) {
				case AUTO_INPUT:
					sorter.autoInput = !sorter.autoInput;
					break;
				case AUTO_OUTPUT:
					sorter.autoOutput = !sorter.autoOutput;
					break;
				case MATCHED_FACING:
					sorter.matchedFacing = GmpmUtil.toggleFacing(sorter.matchedFacing, 1);
					break;
				case REMAINING_FACING:
					sorter.remainingFacing = GmpmUtil.toggleFacing(sorter.remainingFacing, 1);
					break;
				case SOURCE_FACING:
					sorter.sourceFacing = GmpmUtil.toggleFacing(sorter.sourceFacing, 1);
					break;
				}
			}
    		return null;
        }
    }

}
