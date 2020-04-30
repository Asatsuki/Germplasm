package asatsuki256.germplasm.api.recipe;

import net.minecraft.item.ItemStack;

public class ItemStackWithChance {
	
	public ItemStack itemstack;
	public float chance;
	
	public ItemStackWithChance(ItemStack itemstack, float chance) {
		this.itemstack = itemstack;
		this.chance = chance;
	}

	public ItemStackWithChance copy() {
		return new ItemStackWithChance(itemstack, chance);
	}
	
}