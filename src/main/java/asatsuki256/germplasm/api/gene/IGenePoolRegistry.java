package asatsuki256.germplasm.api.gene;

import net.minecraft.item.ItemStack;

public interface IGenePoolRegistry {
	
	public boolean registerGenePool(GenePool pool, String id);
	
	public GenePool pool(String id);
	
	public GenePool getPoolFromItem(ItemStack itemstack);

}
