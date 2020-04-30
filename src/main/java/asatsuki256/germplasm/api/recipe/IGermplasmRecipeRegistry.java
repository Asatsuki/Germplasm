package asatsuki256.germplasm.api.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public interface IGermplasmRecipeRegistry {
	
	public boolean register(IReactorRecipe recipe);
	
	public IReactorRecipe getReactorRecipe(NonNullList<ItemStack> items, List<FluidStack> fluids);
	
	public List<IReactorRecipe> getAllReactorRecipe();

}
