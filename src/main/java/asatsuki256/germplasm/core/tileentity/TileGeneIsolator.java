package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.Random;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmConfig;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerProcessing;
import asatsuki256.germplasm.core.energy.EnergyWrapperReceiver;
import asatsuki256.germplasm.core.item.GermplasmItems;
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

public class TileGeneIsolator extends TileEntity implements ITickable {
	
	public static final int energyPerIsolate = 1000;
	public static final int tickPerIsolate = 40;
	
	public static final Random random = new Random();
	
	ItemHandlerProcessing inventory;
	EnergyWrapperReceiver energy;
	int progress;
	
	public TileGeneIsolator(){
		inventory = new ItemHandlerProcessing(1, 9);
		energy = new EnergyWrapperReceiver(100000, 500, 2000);
		progress = 0;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
			process();
		}
	}
	
	private void process(){
		if ((energy.extractInternal(energyPerIsolate, true) < energyPerIsolate) && GermplasmConfig.CONFIG_CORE.requireEnergy) return;
		ItemStack ingredient = inventory.getStackInSlot(0);
		if (ingredient.getItem() != GermplasmItems.genome_sample) return;
		IGermplasmUnitBase unitIngredient = GeneAPI.nbtHelper.getUnitFromIndividualNBT(ingredient.getTagCompound());
		if (!(unitIngredient instanceof IGenome)) return;
		if (++progress < tickPerIsolate) return;
		progress = tickPerIsolate;
		int emptySlot = -1;
		for (int i = inventory.ingredientSize; i < inventory.getSlots(); i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				emptySlot = i;
				break;
			}
		}
		if (emptySlot < 0) return;
		//単離
		IGene gene = GeneAPI.geneHelper.getRandomGeneFromGenome(((IGenome) unitIngredient), random);
		NBTTagCompound nbtResult = new NBTTagCompound();
		GeneAPI.nbtHelper.setUnitToindividualNBT(nbtResult, gene);
		ItemStack result = new ItemStack(GermplasmItems.gene_sample);
		result.setTagCompound(nbtResult);
		inventory.setStackInSlot(emptySlot, result);
		ItemStack newIngredient = ingredient.copy();
		newIngredient.shrink(1);
		inventory.setStackInSlot(0, newIngredient);
		progress = 0;
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
	
	public float getProgress() {
		return (float) progress / (float) tickPerIsolate;
	}
	
	public IEnergyStorage getEnergy() {
		return energy;
	}

}
