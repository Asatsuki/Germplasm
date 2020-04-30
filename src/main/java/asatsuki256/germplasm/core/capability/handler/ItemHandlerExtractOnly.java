package asatsuki256.germplasm.core.capability.handler;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public class ItemHandlerExtractOnly extends ItemHandlerLimited {
	
	public ItemHandlerExtractOnly(int size) {
		super(size);
	}
	
	@Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return ItemStack.EMPTY;
	}
	
	@Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return super.extractItem(slot, amount, simulate);
	}

}
