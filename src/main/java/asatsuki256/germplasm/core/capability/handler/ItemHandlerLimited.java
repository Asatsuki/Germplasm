package asatsuki256.germplasm.core.capability.handler;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerLimited extends ItemStackHandler {
	
	public ItemHandlerLimited(int size) {
		super(size);
	}
	
	public ItemStack insertInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return super.insertItem(slot, stack, simulate);
	}
	
	public ItemStack extractInternal(int slot, int amount, boolean simulate) {
		return super.extractItem(slot, amount, simulate);
	}

}
