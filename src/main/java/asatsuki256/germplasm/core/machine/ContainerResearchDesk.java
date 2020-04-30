package asatsuki256.germplasm.core.machine;

import asatsuki256.germplasm.core.tileentity.TileResearchDesk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerResearchDesk extends Container {
	
	private final IInventory inventory;
	int xCoord, yCoord, zCoord;
	
	public ContainerResearchDesk(int x, int y, int z, InventoryPlayer inventoryPlayer, TileResearchDesk tile) {
		this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.inventory = tile;
        
        this.addSlotToContainer(new Slot(tile, 0, 80, 103));
        
        int i = 34;
        
        for (int j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, k+j*9+9, 8+k*18, 103+j*18+i));
            }
        }

        for (int j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 8+j*18, 161+i));
        }
        
	}
	
	@Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
    
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int stackCount)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(stackCount);

        if (slot != null && slot.getHasStack()){
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (stackCount < this.inventory.getSizeInventory()){
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)){
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false)){
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0){
                slot.putStack(ItemStack.EMPTY);
            }else{
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

}
