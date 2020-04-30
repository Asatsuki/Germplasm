package asatsuki256.germplasm.core.machine;

import asatsuki256.germplasm.core.tileentity.TileFuelCell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFuelCell extends Container {
	
	int xCoord, yCoord, zCoord;
	
	public ContainerFuelCell(int x, int y, int z, InventoryPlayer inventoryPlayer, TileFuelCell tile) {
		this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        
        int i = 34;
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryPlayer, k+j*9+9, 9+k*18, 103+j*18+i));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 9+j*18, 161+i));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int stackCount) {
       return ItemStack.EMPTY;
    }

}
