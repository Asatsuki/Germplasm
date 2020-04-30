package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.Random;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmConfig;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerProcessing;
import asatsuki256.germplasm.core.energy.EnergyWrapper;
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
import net.minecraftforge.items.ItemStackHandler;

public class TileElectroporator extends TileEntity implements ITickable {
	
	public static final int energyPerTick = 256;
	public static final int energyPerPulse = 65536;
	public static final int tickPerPulse = 10;
	
	ItemStackHandler inventory;
	EnergyWrapperReceiver energy;
	EnergyWrapper energyBuffer;
	int progress;
	
	Random random;
	
	public TileElectroporator(){
		inventory = new ItemHandlerProcessing(2, 1);
		energy = new EnergyWrapperReceiver(100000, 2000, 2000);
		energyBuffer = new EnergyWrapper(energyPerPulse, 2000, 2000);
		progress = 0;
		
		random = new Random();
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
			charge();
			if (energyBuffer.getEnergyStored() >= energyBuffer.getMaxEnergyStored()) {
				process();
			}
		}
	}
	
	private void charge() {
		int extract = GermplasmConfig.CONFIG_CORE.requireEnergy ? energy.extractInternal(energyPerTick, false) : energyPerTick;
		int insert = energyBuffer.receiveEnergy(extract, false);
		energy.receiveEnergy(extract - insert, false);
	}
	
	private void process() {
		ItemStack base = inventory.getStackInSlot(0);
		ItemStack ins = inventory.getStackInSlot(1);
		ItemStack resultSlot = inventory.getStackInSlot(2);
		if (!resultSlot.isEmpty()) return;
		if (base.getItem() != GermplasmItems.callus) return;
		
		if (ins.getItem() == GermplasmItems.gene_sample) {
			NBTTagCompound nbtBase = base.getTagCompound();
			NBTTagCompound nbtIns = ins.getTagCompound();
			IGermplasmUnitBase unitBase = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbtBase);
			IGermplasmUnitBase unitIns = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbtIns);
			if (!(unitBase instanceof IGenome) || !(unitIns instanceof IGene)) return;
			IGenome genome = (IGenome) unitBase;
			IGene gene = (IGene) unitIns;
			//パルス
			progress += 1;
			if (progress < tickPerPulse) return;
			progress = 0;
			//挿入
			genome.insertGene(gene, random);
			NBTTagCompound nbtResult = new NBTTagCompound();
			GeneAPI.nbtHelper.setUnitToindividualNBT(nbtResult, genome);
			ItemStack resultItem = new ItemStack(GermplasmItems.callus);
			resultItem.setTagCompound(nbtResult);
			inventory.setStackInSlot(2, resultItem);
			ItemStack newIngredient = base.copy();
			newIngredient.shrink(1);
			inventory.setStackInSlot(0, newIngredient);
			ItemStack newIns = ins.copy();
			newIns.shrink(1);
			inventory.setStackInSlot(1, newIns);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "inventory"));
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		energyBuffer.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energyBuffer"));
		progress = nbt.getInteger(NBT_PREFIX + "progress");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "inventory", inventory.serializeNBT());
		nbt.setTag(NBT_PREFIX + "energy", energy.serializeNBT());
		nbt.setTag(NBT_PREFIX + "energyBuffer", energyBuffer.serializeNBT());
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
		return (float) progress / (float) tickPerPulse;
	}
	
	public IEnergyStorage getEnergy() {
		return energy;
	}
	
	public IEnergyStorage getEnergyBuffer() {
		return energyBuffer;
	}

}
