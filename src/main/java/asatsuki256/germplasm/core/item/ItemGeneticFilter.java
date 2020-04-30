package asatsuki256.germplasm.core.item;

import asatsuki256.germplasm.api.gene.filter.IGeneticFilter;
import net.minecraft.item.ItemStack;

public abstract class ItemGeneticFilter extends ItemBase {
	
	public ItemGeneticFilter() {
		setMaxStackSize(1);
	}
	
	public abstract IGeneticFilter getFilter(ItemStack item);

}
