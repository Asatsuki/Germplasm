package asatsuki256.germplasm.core.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyWrapper extends EnergyStorage implements INBTSerializable<NBTTagCompound> {

	public EnergyWrapper(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}
	
	public EnergyWrapper(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("energy", energy);
		nbt.setInteger("capacity", capacity);
		nbt.setInteger("maxReceive", maxReceive);
		nbt.setInteger("maxExtract", maxExtract);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		energy = nbt.getInteger("energy");
		capacity = nbt.getInteger("capacity");
		maxReceive = nbt.getInteger("maxReceive");
		maxExtract = nbt.getInteger("maxExtract");
	}

	public void setStats(int energy, int capacity) {
		this.energy = energy;
		this.capacity = capacity;
	}

	

}
