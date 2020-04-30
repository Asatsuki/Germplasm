package asatsuki256.germplasm.core.capability.handler;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public class ItemHandlerInsertOnly extends ItemHandlerLimited {
	
	public ItemHandlerInsertOnly(int size) {
		super(size);
	}
	
	@Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return super.insertItem(slot, stack, simulate);
	}
	
	@Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}
	
	public ItemStack extractInternal(int slot, int amount, boolean simulate) {
		return super.extractItem(slot, amount, simulate);
	}

}
