package asatsuki256.germplasm.core.capability.handler;

import javax.annotation.Nonnull;

import asatsuki256.germplasm.core.tileentity.TileReactor.SlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerProcessing extends ItemStackHandler {
	
	public int ingredientSize;
	public int resultSize;
	
	public ItemHandlerProcessing(int ingredientSize, int resultSize) {
		super(ingredientSize + resultSize);
		this.ingredientSize = ingredientSize;
		this.resultSize = resultSize;
	}
	
	@Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if(getSlotType(slot) == SlotType.INGREDIENT) {
			return super.insertItem(slot, stack, simulate);
		}
		return stack;
	}
	
	@Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
		if(getSlotType(slot) == SlotType.RESULT) {
			return super.extractItem(slot, amount, simulate);
		}
		return ItemStack.EMPTY.copy();
    }
	
	private SlotType getSlotType(int slot) {
		if(slot >= 0 && slot < ingredientSize) return SlotType.INGREDIENT;
		if(slot >= ingredientSize && slot < resultSize + ingredientSize) return SlotType.RESULT;
		return SlotType.INVALID;
	}
	
	
	
}