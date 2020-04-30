package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGenome;
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
import net.minecraftforge.items.ItemStackHandler;

public class TileDnaExtractor extends TileEntity implements ITickable {
	
	public static final int ethanolPerExtract = 10;
	public static final int energyPerExtract = 8000;
	public static final int energyPerUpdate = 100;
	
	ItemStackHandler inventory;
	FluidTank fluidTank;
	EnergyWrapperReceiver energy;
	int progress;
	
	int timer;
	
	public TileDnaExtractor(){
		inventory = new ItemHandlerProcessing(2, 2);
		fluidTank = new FluidTankWithType(GermplasmFluids.bioethanol, 2000);
		energy = new EnergyWrapperReceiver(100000, 500, 200);
		progress = 0;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if(++timer >= 5) {
				timer = 0;
				onContentsChanged();
			}
			transferFluidItem(1, 3, fluidTank);
			processRecipe();
		}
	}
	
	private void processRecipe() {
		FluidStack ethanol = fluidTank.drain(ethanolPerExtract, false);
		if (inventory.getStackInSlot(0).isEmpty() || ethanol == null || ethanolPerExtract < ethanol.amount) {
			progress = 0;
			return;
		}
		
		ItemStack ingredient = inventory.getStackInSlot(0).copy();
		NBTTagCompound ingredientTag = ingredient.getTagCompound();
		if (ingredientTag == null) {
			progress = 0;
			return;
		}
		IGenome gene = GeneAPI.nbtHelper.getGenomeFromIndividualNBT(ingredientTag);
		if (gene == null) {
			progress = 0;
			return;
		}
		
		ItemStack genomeSample = new ItemStack(GermplasmItems.genome_sample);
		NBTTagCompound nbt = new NBTTagCompound();
		GeneAPI.nbtHelper.setGenomeToIndividualNBT(nbt, gene);
		genomeSample.setTagCompound(nbt);
		
		if (!GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(2), genomeSample).getSuccess()) {
			progress = 0;
			return;
		}
		int maxExtract = Math.min(energyPerUpdate, energyPerExtract - progress);
		
		progress += GermplasmConfig.CONFIG_CORE.requireEnergy ? energy.extractInternal(maxExtract, false) : maxExtract;
		
		if (progress >= energyPerExtract) {
			progress = 0;
			ItemStack destination = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(2), genomeSample).getDestination();
			ingredient.shrink(1);
			inventory.setStackInSlot(0, ingredient);
			inventory.setStackInSlot(2, destination);
			fluidTank.drain(ethanolPerExtract, true);
		}
	}
	
	private void transferFluidItem(int slotContainer, int slotEmpty, FluidTank tank) {
		if(inventory.getStackInSlot(slotContainer).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			ItemStack container = inventory.getStackInSlot(slotContainer).copy();
			container.setCount(1);
			
			FluidActionResult testFluidResult = FluidUtil.tryEmptyContainer(container, tank, tank.getCapacity(), null, false);
			MergeResult testMergeResult = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), testFluidResult.result);
			if(testFluidResult.success && testMergeResult.getSource().isEmpty()) {
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
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "inventory"));
		fluidTank.readFromNBT(nbt.getCompoundTag(NBT_PREFIX + "fluidTank"));
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		progress = nbt.getInteger(NBT_PREFIX + "progress");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "inventory", inventory.serializeNBT());
		nbt.setTag(NBT_PREFIX + "fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
		nbt.setTag(NBT_PREFIX + "energy", energy.serializeNBT());
		nbt.setInteger(NBT_PREFIX + "progress", progress);
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
			return (T)fluidTank;
		} else if (capability == CapabilityEnergy.ENERGY) {
			return (T) energy;
		}
		return super.getCapability(capability, facing);
	}
	
	public IFluidTank getTank() {
		return fluidTank;
	}
	
	public IEnergyStorage getEnergy() {
		return energy;
	}
	
	public float getProgress() {
		return (float)progress / (float)energyPerExtract;
	}
	
	public void onContentsChanged() {
		this.markDirty();
		this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
	}
}
