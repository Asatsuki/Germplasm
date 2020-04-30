package asatsuki256.germplasm.core.machine;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerIngredient extends SlotItemHandler{
	
	protected IItemHandlerModifiable itemHandlerModifieable;
	protected int index;

	public SlotItemHandlerIngredient(IItemHandlerModifiable itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.index = index;
		this.itemHandlerModifieable = itemHandler;
	}
	
	@Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
        return true;
    }
	
	@Override
    @Nonnull
    public ItemStack decrStackSize(int amount)
    {
		if (itemHandlerModifieable != null) {
			ItemStack stack = itemHandlerModifieable.getStackInSlot(index);
			ItemStack split = stack.splitStack(amount);
			return split;
		}
		return super.decrStackSize(amount);
    }

}
