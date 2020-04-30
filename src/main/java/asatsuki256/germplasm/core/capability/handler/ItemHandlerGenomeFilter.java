package asatsuki256.germplasm.core.capability.handler;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerGenomeFilter extends ItemStackHandler implements ICapabilityProvider {

	public static final String INVENTORY_NBT_KEY = NBT_PREFIX + "inventory";
	
	@Nonnull
    protected ItemStack container;
	
	public ItemHandlerGenomeFilter(ItemStack item, int size) {
		super(size);
		this.container = item;
		updateNBT();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)this;
		}
		return null;
	}
	
	@Override
	public void onContentsChanged(int slot) {
		updateNBT();
	}
	
	public void updateNBT() {
		NBTTagCompound tag = container.getTagCompound();
		if (tag == null) tag = new NBTTagCompound();
		tag.setTag(INVENTORY_NBT_KEY, this.serializeNBT());
		container.setTagCompound(tag);
	}

}
