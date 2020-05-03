package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import asatsuki256.germplasm.core.capability.handler.FluidTankWithType;
import asatsuki256.germplasm.core.energy.EnergyWrapper;
import asatsuki256.germplasm.core.fluid.GermplasmFluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileFuelCell extends TileEntity implements ITickable {
	
	public static final int energyPerTick = 240;
	public static final int maxExtract = 1000;
	public static final int tickPerMilliBucket = 40;
	public static final FluidStack fuel = new FluidStack(GermplasmFluids.bioethanol, 1);
	
	FluidTank fluidTank;
	EnergyWrapper energy;
	int left;
	
	public TileFuelCell() {
		fluidTank = new FluidTankWithType(GermplasmFluids.bioethanol, 4000);
		energy = new EnergyWrapper(1000000, maxExtract, maxExtract);
		left = 0;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
			if (energy.getMaxEnergyStored() - energy.getEnergyStored() >= energyPerTick) {
				process();
			}
			output();
		}
	}
	
	private void process() {
		if (left <= 0) {
			left = 0;
			FluidStack drained = fluidTank.drainInternal(fuel, true);
			if (drained == null || drained.amount <= 0) return;
			left = tickPerMilliBucket;
		}
		left--;
		energy.receiveEnergy(energyPerTick, false);
	}
	
	private void output() {
		for (EnumFacing facing : EnumFacing.values()) {
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
				IEnergyStorage destination = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				int extract = energy.extractEnergy(maxExtract, true);
				if (destination.canReceive()) {
					int receive = destination.receiveEnergy(extract, false);
					energy.extractEnergy(receive, false);
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		fluidTank.readFromNBT(nbt.getCompoundTag(NBT_PREFIX + "fluidTank"));
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		left = nbt.getInteger(NBT_PREFIX + "left");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
		nbt.setTag(NBT_PREFIX + "energy", energy.serializeNBT());
		nbt.setInteger(NBT_PREFIX + "left", left);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = new NBTTagCompound();
		return this.writeToNBT(nbt);
    }
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
	    NBTTagCompound nbtTag = new NBTTagCompound();
	    writeToNBT(nbtTag);
	    return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
	    readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		} else if (capability == CapabilityEnergy.ENERGY) {
		    return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) fluidTank;
		} else if (capability == CapabilityEnergy.ENERGY) {
			return (T) energy;
		}
		return super.getCapability(capability, facing);
	}
	
	public FluidTank getTank() {
		return fluidTank;
	}
	
	public EnergyWrapper getEnergy() {
		return energy;
	}
	
	public float getProgress() {
		if (left <= 0) return 0f;
		return 1f - ((float)left / (float)tickPerMilliBucket);
	}

}
