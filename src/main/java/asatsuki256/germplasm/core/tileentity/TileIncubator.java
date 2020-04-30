package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmConfig;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerProcessing;
import asatsuki256.germplasm.core.energy.EnergyWrapperReceiver;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileIncubator extends TileEntity implements ITickable {
	
	public static final int energyPerTick = 50;
	
	public static final int incubateTick = 320;

	ItemStackHandler inventory;
	EnergyWrapperReceiver energy;
	int progress = 0;
	
	public TileIncubator() {
		this.inventory = new ItemHandlerProcessing(2, 1);
		energy = new EnergyWrapperReceiver(100000, 500, 200);
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
			if (!process()) progress = 0;
		}
	}
	
	private boolean process() {
		ItemStack medium = inventory.getStackInSlot(1);
		ItemStack ingredient = inventory.getStackInSlot(0);
		ItemStack resultSlot = inventory.getStackInSlot(2);
		NBTTagCompound nbt = ingredient.getTagCompound();
		
		
		if (medium.getItem() == GermplasmItems.solid_medium) {
			if (ingredient.getItem() == GermplasmItems.callus) {
				//カルスを種に戻す
				IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
				if (unit != null && unit instanceof IGenome) {
					IGenome genome = (IGenome) unit;
					ItemStack result = new ItemStack(GermplasmItems.seed_sample);
					NBTTagCompound resultNBT = new NBTTagCompound();
					GeneAPI.nbtHelper.setUnitToindividualNBT(resultNBT, genome);
					result.setTagCompound(resultNBT);
					MergeResult merge = GmpmItemUtil.tryMergeItemStack(resultSlot, result);
					if (!merge.getSource().isEmpty()) return false;
					if (!checkProgress()) return true;
					inventory.setStackInSlot(2, merge.getDestination());
					ItemStack ingredientAfter = ingredient.copy();
					ingredientAfter.shrink(1);
					inventory.setStackInSlot(0, ingredientAfter);
					ItemStack mediumAfter = medium.copy();
					mediumAfter.shrink(1);
					inventory.setStackInSlot(1, mediumAfter);
					return true;
				}
			}
		} else if (medium.getItem() == GermplasmItems.solid_medium_auxin) {
			if (ingredient.getItem() == GermplasmItems.seed_sample) {
				//種をカルスに変える
				IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
				if (unit != null && unit instanceof IGenome) {
					IGenome genome = (IGenome) unit;
					ItemStack result = new ItemStack(GermplasmItems.callus);
					NBTTagCompound resultNBT = new NBTTagCompound();
					GeneAPI.nbtHelper.setUnitToindividualNBT(resultNBT, genome);
					result.setTagCompound(resultNBT);
					MergeResult merge = GmpmItemUtil.tryMergeItemStack(resultSlot, result);
					if (!merge.getSource().isEmpty()) return false;
					if (!checkProgress()) return true;
					inventory.setStackInSlot(2, merge.getDestination());
					ItemStack ingredientAfter = ingredient.copy();
					ingredientAfter.shrink(1);
					inventory.setStackInSlot(0, ingredientAfter);
					ItemStack mediumAfter = medium.copy();
					mediumAfter.shrink(1);
					inventory.setStackInSlot(1, mediumAfter);
					return true;
				}
			} else if (ingredient.getItem() == GermplasmItems.callus) {
				//カルスを2つに増やす
				IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
				if (unit != null && unit instanceof IGenome) {
					IGenome genome = (IGenome) unit;
					ItemStack result = new ItemStack(GermplasmItems.callus, 2);
					NBTTagCompound resultNBT = new NBTTagCompound();
					GeneAPI.nbtHelper.setUnitToindividualNBT(resultNBT, genome);
					result.setTagCompound(resultNBT);
					MergeResult merge = GmpmItemUtil.tryMergeItemStack(resultSlot, result);
					if (!merge.getSource().isEmpty()) return false;
					if (!checkProgress()) return true;
					inventory.setStackInSlot(2, merge.getDestination());
					ItemStack ingredientAfter = ingredient.copy();
					ingredientAfter.shrink(1);
					inventory.setStackInSlot(0, ingredientAfter);
					ItemStack mediumAfter = medium.copy();
					mediumAfter.shrink(1);
					inventory.setStackInSlot(1, mediumAfter);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean checkProgress() {
		if (energy.extractInternal(energyPerTick, true) >= energyPerTick || !GermplasmConfig.CONFIG_CORE.requireEnergy) {
			energy.extractInternal(energyPerTick, false);
			progress += 1;
		}
		if (progress >= incubateTick) {
			progress = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "inventory"));
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		progress = nbt.getInteger(NBT_PREFIX + "progress");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "inventory", inventory.serializeNBT());
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
	
	public IEnergyStorage getEnergy() {
		return energy;
	}
	
	public float getProgress() {
		return (float) progress / (float) incubateTick;
	}

}
