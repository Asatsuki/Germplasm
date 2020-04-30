package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;
import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileMicroscope extends TileEntity implements IInventory {
	
	private ItemStack analyzingItem = ItemStack.EMPTY;
	
	public TileMicroscope() {
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		NBTTagCompound nbt0 = nbt.getCompoundTag(NBT_PREFIX + "analyzingItem");
		analyzingItem = new ItemStack(nbt0);
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		NBTTagCompound nbt0 = new NBTTagCompound();
		nbt0 = analyzingItem.writeToNBT(nbt0);
		nbt.setTag(NBT_PREFIX + "analyzingItem", nbt0);
		return nbt;
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
		return UNLOC_PREFIX + "gui.microscope";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return analyzingItem == ItemStack.EMPTY;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index == 0) {
			return analyzingItem;
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
	}

}
