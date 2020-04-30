package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmConfig;
import asatsuki256.germplasm.core.capability.handler.FluidTankWithType;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerProcessing;
import asatsuki256.germplasm.core.energy.EnergyWrapperReceiver;
import asatsuki256.germplasm.core.fluid.GermplasmFluids;
import asatsuki256.germplasm.core.item.GermplasmItems;
import asatsuki256.germplasm.core.util.GmpmItemUtil;
import asatsuki256.germplasm.core.util.GmpmItemUtil.MergeResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class TilePCR extends TileEntity implements ITickable {
	
	public static final int energyPerTick = 50;
	
	public static final int denatureTick = 150;
	public static final int annealingTick = 300;
	public static final int extensionTick = 300;
	
	//flavor text
	public static final int denatureTemp = 94;
	public static final int annealingTemp = 60;
	public static final int extensionTemp = 72;
	
	ItemStackHandler inventory;
	ItemStackHandler fluidItem;
	FluidTank fluidTank;
	EnergyWrapperReceiver energy;
	int progressTick;
	boolean stopped;
	
	public TilePCR(){
		inventory = new ItemStackHandler(16);
		fluidItem = new ItemHandlerProcessing(1, 1);
		fluidTank = new FluidTankWithType(GermplasmFluids.nucleotide, 2000);
		energy = new EnergyWrapperReceiver(100000, 500, 200);
		progressTick = 0;
		stopped = true;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
			transferFluidItem(0, 1, fluidTank, fluidItem);
			process();
		}
	}
	
	private void process() {
		if (energy.extractInternal(energyPerTick, true) >= energyPerTick || !GermplasmConfig.CONFIG_CORE.requireEnergy) {
			energy.extractInternal(energyPerTick, false);
			progressTick++;
			stopped = false;
		} else {
			progressTick = 0;
			stopped = true;
		}
		
		NonNullList<ItemStack> sampleList = NonNullList.create();
		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			if (item.getItem() == GermplasmItems.genome_sample || item.getItem() == GermplasmItems.gene_sample) {
				sampleList.add(item);
			}
		}
		if (sampleList.isEmpty()) {
			progressTick = 0;
			stopped = true;
		}
		
		//クローン
		if (progressTick >= denatureTick + annealingTick + extensionTick) {
			for (ItemStack sampleItem : sampleList) {
				IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(sampleItem.getTagCompound());
				if (unit == null) {
					insert(sampleItem);
				} else {
					int requiredDNTP = 1 * sampleItem.getCount();
					FluidStack drainDNTP = fluidTank.drain(requiredDNTP, true);
					if (drainDNTP != null) {
						int amountDNTP = drainDNTP.amount;
						ItemStack insertItem = sampleItem.copy();
						if (requiredDNTP > amountDNTP) {
							insertItem.setCount(amountDNTP / 1);
						}
						insert(insertItem);
					}
				}
			}
			progressTick = 0;
		}
	}
	
	private void transferFluidItem(int slotContainer, int slotEmpty, FluidTank tank, ItemStackHandler inventory) {
		if(inventory.getStackInSlot(slotContainer).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			ItemStack container = inventory.getStackInSlot(slotContainer).copy();
			container.setCount(1);
			
			FluidActionResult testFluidResult = FluidUtil.tryEmptyContainer(container, tank, tank.getCapacity(), null, false);
			MergeResult testMergeResult = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), testFluidResult.result);
			if(testFluidResult.success && testMergeResult.getSource().isEmpty()) {
				System.out.println("transfer");
				FluidActionResult fluidResult = FluidUtil.tryEmptyContainer(container, tank, tank.getCapacity(), null, true);
				MergeResult mergeResult = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), fluidResult.result);
				inventory.setStackInSlot(slotContainer, mergeResult.getSource());
				inventory.setStackInSlot(slotEmpty, mergeResult.getDestination());
				return;
			}
			FluidActionResult testFluidExport = FluidUtil.tryFillContainer(container, tank, tank.getCapacity(), null, false);
			MergeResult testMergeExport = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), testFluidExport.result);
			if (testFluidExport.success && testMergeExport.getSource().isEmpty()) {
				FluidActionResult fluidResult = FluidUtil.tryFillContainer(container, tank, tank.getCapacity(), null, true);
				MergeResult mergeResult = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), fluidResult.result);
				inventory.setStackInSlot(slotContainer, mergeResult.getSource());
				inventory.setStackInSlot(slotEmpty, mergeResult.getDestination());
				return;
			}
			
		}
	}
	
	private void insert(ItemStack item) {
		ItemStack insertItem = item.copy();
		for (int i = 0; i < inventory.getSlots(); i++) {
			insertItem = inventory.insertItem(i, insertItem, false);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "inventory"));
		fluidItem.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "fluidItem"));
		fluidTank.readFromNBT(nbt.getCompoundTag(NBT_PREFIX + "fluidTank"));
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		progressTick = nbt.getInteger(NBT_PREFIX + "progress");
		stopped = nbt.getBoolean(NBT_PREFIX + "stopped");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "inventory", inventory.serializeNBT());
		nbt.setTag(NBT_PREFIX + "fluidItem", fluidItem.serializeNBT());
		nbt.setTag(NBT_PREFIX + "fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
		nbt.setTag(NBT_PREFIX + "energy", energy.serializeNBT());
		nbt.setInteger(NBT_PREFIX + "progress", progressTick);
		nbt.setBoolean(NBT_PREFIX + "stopped", stopped);
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
		} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
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
		} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) fluidTank;
		} else if (capability == CapabilityEnergy.ENERGY) {
			return (T) energy;
		}
		return super.getCapability(capability, facing);
	}
	
	public float getProgress() {
		return (float)progressTick / (float)(denatureTick + annealingTick + extensionTick);
	}
	
	public int getTemperture() {
		if (progressTick < denatureTick) {
			return Math.min(extensionTemp + progressTick, denatureTemp);
		} else if (progressTick < denatureTick + annealingTick) {
			return Math.max(denatureTemp - (progressTick - denatureTick), annealingTemp);
		} else {
			return Math.min(annealingTemp + (progressTick - denatureTick - annealingTick), extensionTemp);
		}
	}
	
	public String getStateKey() {
		if (stopped) {
			return "stop";
		} else if (progressTick < denatureTick) {
			return "denature";
		} else if (progressTick < denatureTick + annealingTick) {
			return "annealing";
		} else {
			return "extension";
		}
	}
	
	public IFluidTank getFluidTank() {
		return fluidTank;
	}
	
	public IEnergyStorage getEnergy() {
		return energy;
	}
	
	public IItemHandler getInventory() {
		return inventory;
	}
	
	public IItemHandlerModifiable getFluidItem() {
		return fluidItem;
	}

}
