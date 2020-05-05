package asatsuki256.germplasm.core.machine;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInventoryValid extends Slot {

	public SlotInventoryValid(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
    {
        return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
    }

}
