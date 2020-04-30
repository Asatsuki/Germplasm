package asatsuki256.germplasm.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictContainer {

	private final String oreName;
	private final int count;

	public OreDictContainer (String oreName) {
		this(oreName, 1);
	}

	public OreDictContainer (String oreName, int count) {
		this.oreName = oreName;
		this.count = count;
	}

	public NonNullList<ItemStack> getItems() {
		NonNullList<ItemStack> items = NonNullList.create();
		NonNullList<ItemStack> ores = OreDictionary.getOres(oreName);
		for (ItemStack ore : ores) {
			ItemStack ore2 = ore.copy();
			ore2.setCount(count);
			items.add(ore2);
		}
		return items;
	}

}