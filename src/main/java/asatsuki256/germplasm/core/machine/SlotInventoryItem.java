package asatsuki256.germplasm.core.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;

public class SlotInventoryItem extends Slot{
	
	Item inventoryItem;
	
	public SlotInventoryItem(IInventory inventoryIn, int index, int xPosition, int yPosition, Item inventoryItem)
    {
        super(inventoryIn, index, xPosition, yPosition);
        this.inventoryItem = inventoryItem;
    }

    /*
        このアイテムは動かせない、つかめないようにする。
     */
    @Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
        return !(getHasStack() && getStack().getItem() == inventoryItem);
    }

}
