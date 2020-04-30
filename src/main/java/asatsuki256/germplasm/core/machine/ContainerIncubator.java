package asatsuki256.germplasm.core.machine;

import asatsuki256.germplasm.core.tileentity.TileIncubator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerIncubator extends Container {
	
	private final IItemHandler inventory;
	int xCoord, yCoord, zCoord;
	
	public ContainerIncubator(int x, int y, int z, InventoryPlayer inventoryPlayer, TileIncubator tile) {
		this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        
        //ingredient
        this.addSlotToContainer(new SlotItemHandlerIngredient((IItemHandlerModifiable)inventory, 0, 46, 58));
        //medium
        this.addSlotToContainer(new SlotItemHandlerIngredient((IItemHandlerModifiable)inventory, 1, 81, 58));
        //result
        this.addSlotToContainer(new SlotItemHandler(inventory, 2, 116, 58));
        
        int i = 34;
        
        for (int j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, k+j*9+9, 9+k*18, 103+j*18+i));
            }
        }

        for (int j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 9+j*18, 161+i));
        }
        
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
