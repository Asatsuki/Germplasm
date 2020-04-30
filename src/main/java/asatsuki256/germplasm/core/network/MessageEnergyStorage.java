package asatsuki256.germplasm.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageEnergyStorage implements IMessage{

	public int x;
	public int y;
	public int z;
	public int energy;
	public int capacity;
	
	public MessageEnergyStorage() {}
	
	public MessageEnergyStorage(BlockPos pos, IEnergyStorage energyStorage) {
		this(pos.getX(), pos.getY(), pos.getZ(), energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored());
	}
	
	public MessageEnergyStorage(int x, int y, int z, int energy, int capacity) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.energy = energy;
		this.capacity = capacity;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(energy);
		buf.writeInt(capacity);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.energy = buf.readInt();
		this.capacity = buf.readInt();
	}

}
