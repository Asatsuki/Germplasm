package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import asatsuki256.germplasm.core.energy.EnergyWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileSolidFuelGenerator extends TileEntity implements ITickable {
	
	public static final int energyPerTick = 80;
	public static final int timePerTick = 4;
	public static final int maxExtract = 1000;
	
	ItemStackHandler inventory;
	EnergyWrapper energy;
	int maxBurnTime;
	int burnTime;
	
	public TileSolidFuelGenerator() {
		inventory = new ItemStackHandler(1);
		energy = new EnergyWrapper(100000, maxExtract, maxExtract);
		maxBurnTime = 0;
		burnTime = 0;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
			if (energy.getMaxEnergyStored() - energy.getEnergyStored() >= energyPerTick) {
				process();
				output();
			}
		}
	}
	
	private void process() {
		if (burnTime >= maxBurnTime) {
			burnTime = 0;
			maxBurnTime = 0;
			if (inventory.getStackInSlot(0).isEmpty()) return;
			ItemStack fuelSlot = inventory.getStackInSlot(0).copy();
			ItemStack fuelItem = fuelSlot.splitStack(1);
			int itemBurnTime = TileEntityFurnace.getItemBurnTime(fuelItem);
			if (itemBurnTime <= 0) return;
			maxBurnTime = itemBurnTime;
			inventory.setStackInSlot(0, fuelSlot);
		}
		burnTime += timePerTick;
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
		inventory.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "inventory"));
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		maxBurnTime = nbt.getInteger(NBT_PREFIX + "maxBurnTime");
		burnTime = nbt.getInteger(NBT_PREFIX + "burnTime");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "inventory", inventory.serializeNBT());
		nbt.setTag(NBT_PREFIX + "energy", energy.serializeNBT());
		nbt.setInteger(NBT_PREFIX + "maxBurnTime", maxBurnTime);
		nbt.setInteger(NBT_PREFIX + "burnTime", burnTime);
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
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		} else if (capability == CapabilityEnergy.ENERGY) {
		    return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)inventory;
		} else if (capability == CapabilityEnergy.ENERGY) {
			return (T) energy;
		}
		return super.getCapability(capability, facing);
	}
	
	public EnergyWrapper getEnergy() {
		return energy;
	}
	
	public float getRemaining() {
		if (maxBurnTime <= 0) return 0f;
		return 1f - ((float)burnTime / (float)maxBurnTime);
	}

}
