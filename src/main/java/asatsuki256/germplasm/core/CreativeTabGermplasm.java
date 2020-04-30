package asatsuki256.germplasm.core;

import asatsuki256.germplasm.core.item.GermplasmItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabGermplasm extends CreativeTabs{

	public CreativeTabGermplasm(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(GermplasmItems.genome_sample);
	}

}
