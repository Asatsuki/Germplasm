package asatsuki256.germplasm.api.recipe;

import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public interface IReactorRecipe {
	
	public boolean canCraft(NonNullList<ItemStack> items, List<FluidStack> fluids);
	
	public NonNullList<ItemStack> getResultItemFromInput(NonNullList<ItemStack> items, List<FluidStack> fluids, Random rand);
	
	public NonNullList<ItemStack> getResultItemAll(NonNullList<ItemStack> items, List<FluidStack> fluids);
	
	public List<FluidStack> getResultFluidFromInput(NonNullList<ItemStack> items, List<FluidStack> fluids);
	
	public NonNullList<List<ItemStack>> getIngredientItem();
	
	public NonNullList<List<FluidStack>> getIngredientFluid();
	
	public NonNullList<ItemStackWithChance> getResultItemWithChance();
	
	public NonNullList<ItemStack> getResultItem();
	
	public List<FluidStack> getResultFluid();
	
	public int getEnergy();
	
	public int getPriority();
	
	public boolean isValid();

}
