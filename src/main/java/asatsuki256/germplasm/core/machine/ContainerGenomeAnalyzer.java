package asatsuki256.germplasm.core.machine;

import asatsuki256.germplasm.core.tileentity.TileGenomeAnalyzer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerGenomeAnalyzer extends Container{

	public static final Item SequencerItem = null;
	private final IInventory inventory;
	
	//座標でGUIを開くか判定するためのもの。
    int xCoord, yCoord, zCoord;
    
    public ContainerGenomeAnalyzer(int x, int y, int z, InventoryPlayer inventoryPlayer, TileGenomeAnalyzer tile) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.inventory = tile;
        
        this.addSlotToContainer(new Slot(tile, 0, 185, 106));
        this.addSlotToContainer(new Slot(tile, 1, 227, 106));
        
        int i = 34;
        
        for (int j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new SlotInventoryItem(inventoryPlayer, k+j*9+9, 184+k*18, 103+j*18+i, SequencerItem));
            }
        }

        for (int j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new SlotInventoryItem(inventoryPlayer, j, 184+j*18, 161+i, SequencerItem));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        //もし、ブロックとの位置関係でGUI制御したいなら、こちらを使う
//        return player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64D;
        return true;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.inventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
    
}
