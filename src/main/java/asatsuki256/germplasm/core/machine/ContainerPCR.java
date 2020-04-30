package asatsuki256.germplasm.core.machine;

import asatsuki256.germplasm.core.tileentity.TilePCR;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPCR extends Container {
	
	private final IItemHandler inventory;
	private final IItemHandlerModifiable fluidItem;
	int xCoord, yCoord, zCoord;
	
	public ContainerPCR(int x, int y, int z, InventoryPlayer inventoryPlayer, TilePCR tile) {
		this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.inventory = tile.getInventory();
        this.fluidItem = tile.getFluidItem();
        
        int i = 34;
        
        for (int j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, k+j*9+9, 17+k*18, 103+j*18+i));
            }
        }

        for (int j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 17+j*18, 161+i));
        }
        
        for (int j = 0; j < 4; ++j) {
        	for (int k = 0; k < 4; ++k) {
        		this.addSlotToContainer(new SlotItemHandler(inventory, j*4+k, 50+k*18, 41+j*18));
        	}
        }
        
        this.addSlotToContainer(new SlotItemHandlerIngredient(fluidItem, 0, 9, 44));
        this.addSlotToContainer(new SlotItemHandler(fluidItem, 1, 9, 92));
        
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int stackCount)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(stackCount);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (stackCount < this.inventory.getSlots())
            {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSlots(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSlots(), false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
