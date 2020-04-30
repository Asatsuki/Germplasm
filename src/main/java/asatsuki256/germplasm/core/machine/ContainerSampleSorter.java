package asatsuki256.germplasm.core.machine;

import asatsuki256.germplasm.core.tileentity.TileSampleSorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSampleSorter extends Container {
	
	private final TileSampleSorter tile;
	int xCoord, yCoord, zCoord;
	int machineSlots;
	
	public ContainerSampleSorter(int x, int y, int z, InventoryPlayer inventoryPlayer, TileSampleSorter tile) {
		this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.tile = tile;
        this.machineSlots = tile.getSource().getSlots() + tile.getMatched().getSlots() + tile.getRemaining().getSlots();
        
        for (int i = 0; i < tile.getSource().getSlots(); i++) {
        	this.addSlotToContainer(new SlotItemHandlerIngredient(tile.getSource(), i, 26, 25 + i * 18));
        }
        for (int i = 0; i < tile.getMatched().getSlots(); i++) {
        	this.addSlotToContainer(new SlotItemHandler(tile.getMatched(), i, 97 + i * 18, 25));
        }
        for (int i = 0; i < tile.getRemaining().getSlots(); i++) {
        	this.addSlotToContainer(new SlotItemHandler(tile.getRemaining(), i, 97 + i * 18, 61));
        }
        this.addSlotToContainer(new SlotItemHandler(tile.getFilter(), 0, 61, 25));
        
        int high = 34;
        
        for (int j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, k+j*9+9, 8+k*18, 103+j*18+high));
            }
        }

        for (int j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 8+j*18, 161+high));
        }
        
	}
	
	@Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
    
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()){
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < machineSlots){
                if (!this.mergeItemStack(itemstack1, machineSlots, this.inventorySlots.size(), true)){
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, tile.getSource().getSlots(), false)){
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0){
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

}
