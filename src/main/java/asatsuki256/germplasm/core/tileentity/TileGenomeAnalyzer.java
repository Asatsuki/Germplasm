package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;
import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmConfig;
import asatsuki256.germplasm.core.energy.EnergyWrapperReceiver;
import asatsuki256.germplasm.core.network.GermplasmPacketHandler;
import asatsuki256.germplasm.core.network.MessageEnergyStorage;
import asatsuki256.germplasm.core.network.MessageGenomeAnalyzer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class TileGenomeAnalyzer extends TileEntity implements IInventory, ITickable {
	
	private static final int ENERGY_PER_ANALYZE = 120;
	
	public int timer = 0;
	public int progress = 0;
	public int maxProgress = 0;
	
	private ItemStack analyzingItem = ItemStack.EMPTY;
	private ItemStack analyzedItem = ItemStack.EMPTY;
	
	private EnergyWrapperReceiver energy = new EnergyWrapperReceiver(50000, 500, 500);
	
	public TileGenomeAnalyzer() {
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		NBTTagCompound nbt0 = nbt.getCompoundTag(NBT_PREFIX + "analyzingItem");
		analyzingItem = new ItemStack(nbt0);
		NBTTagCompound nbt1 = nbt.getCompoundTag(NBT_PREFIX + "analyzedItem");
		analyzedItem = new ItemStack(nbt1);
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		timer = nbt.getInteger(NBT_PREFIX + "timer");
		progress = nbt.getInteger(NBT_PREFIX + "progress");
		maxProgress = nbt.getInteger(NBT_PREFIX + "maxProgress");
	}

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		NBTTagCompound nbt0 = new NBTTagCompound();
		nbt0 = analyzingItem.writeToNBT(nbt0);
		nbt.setTag(NBT_PREFIX + "analyzingItem", nbt0);
		NBTTagCompound nbt1 = new NBTTagCompound();
		nbt1 = analyzedItem.writeToNBT(nbt1);
		nbt.setTag(NBT_PREFIX + "analyzedItem", nbt1);
		nbt.setTag(NBT_PREFIX + "energy", energy.serializeNBT());
		nbt.setInteger(NBT_PREFIX + "timer", timer);
		nbt.setInteger(NBT_PREFIX + "progress", progress);
		nbt.setInteger(NBT_PREFIX + "maxProgress", maxProgress);
		return nbt;
	}
	
	@Override
	public void update(){
		if(++timer >= 5) {
			timer = 0;
			if (!world.isRemote && world.isBlockLoaded(pos)) {
				GermplasmPacketHandler.INSTANCE.sendToAllTracking(new MessageEnergyStorage(pos, energy), new TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 1f));
				GermplasmPacketHandler.INSTANCE.sendToDimension(new MessageGenomeAnalyzer(pos, progress, maxProgress), this.world.provider.getDimension());
			}
		}
		NBTTagCompound nbt = analyzingItem.getTagCompound();
		IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
		if(unit != null) {
			if(!unit.isAnalyzed()) {
				int analyzed = unit.analyze(true);
				int energyConsume = analyzed * ENERGY_PER_ANALYZE;
				
				ItemStack resultStack = analyzingItem.copy();
				NBTTagCompound resultNbt = resultStack.getTagCompound();
				GeneAPI.nbtHelper.setUnitToindividualNBT(resultNbt, unit);
				
				if(analyzed > 0 && analyzedItem.isEmpty()) {
					maxProgress = energyConsume;
					if(progress >= maxProgress) {
						unit.analyze(false);
						GeneAPI.nbtHelper.setUnitToindividualNBT(nbt, unit);
						analyzedItem = analyzingItem.copy();
						analyzingItem = ItemStack.EMPTY;
					} else {
						if (GermplasmConfig.CONFIG_CORE.requireEnergy) {
							progress += energy.extractInternal(ENERGY_PER_ANALYZE, false);
						} else {
							progress += ENERGY_PER_ANALYZE;
						}
						
					}
					
				}
				
			} else {
				if(analyzedItem.isEmpty()) {
					unit.analyze(false);
					GeneAPI.nbtHelper.setUnitToindividualNBT(nbt, unit);
					analyzedItem = analyzingItem.copy();
					analyzingItem = ItemStack.EMPTY;
				}
			}
			
		} else {
			progress = 0;
		}
	}
	
	public float getProgressBar() {
		if(maxProgress == 0) return 0f;
		return (float)progress / (float)maxProgress;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new SPacketUpdateTileEntity(pos, 1, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = new NBTTagCompound();
		return this.writeToNBT(nbt);
    }
	
	@Override
	public String getName() {
		return UNLOC_PREFIX + "gui.genome_analyzer";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		if (analyzingItem.isEmpty() && analyzedItem.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index == 0) {
			return analyzingItem;
		}else if(index == 1) {
			return analyzedItem;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if(index >= 0 && index < getSizeInventory()) {
			ItemStack item = getStackInSlot(index).splitStack(count);
			if (item.getCount() <= 0) {
				item = ItemStack.EMPTY;
			}
			return item;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if(index == 0) {
			ItemStack item = getStackInSlot(index).copy();
			analyzingItem = ItemStack.EMPTY;
			return item;
		}else if(index == 1) {
			ItemStack item = getStackInSlot(index).copy();
			analyzedItem = ItemStack.EMPTY;
			return item;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index >= 0 && index < getSizeInventory()) {
			if (stack == null) {
				stack = ItemStack.EMPTY;
			}

			if(index == 0) {
				analyzingItem = stack;
			}else if(index == 1) {
				analyzedItem = stack;
			}
			

			if (stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
				stack.setCount(this.getInventoryStackLimit());
			}

			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		analyzingItem = ItemStack.EMPTY;
		analyzedItem = ItemStack.EMPTY;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
	  if (capability == CapabilityEnergy.ENERGY) {
	    return true;
	  }
	  return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
		    return (T) energy;
		  }
		  return super.getCapability(capability, facing);
	}
	
}
