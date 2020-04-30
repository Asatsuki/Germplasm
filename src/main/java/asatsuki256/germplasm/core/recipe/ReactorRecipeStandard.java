package asatsuki256.germplasm.core.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.recipe.IReactorRecipe;
import asatsuki256.germplasm.api.recipe.ItemStackWithChance;
import asatsuki256.germplasm.core.util.FluidDictContainer;
import asatsuki256.germplasm.core.util.GmpmFluidUtil;
import asatsuki256.germplasm.core.util.GmpmItemUtil;
import asatsuki256.germplasm.core.util.OreDictContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class ReactorRecipeStandard implements IReactorRecipe {
	
	private NonNullList<NonNullList<ItemStack>> ingredientItem;
	private NonNullList<List<FluidStack>> ingredientFluid;
	
	private NonNullList<ItemStackWithChance> resultItem;
	private List<FluidStack> resultFluid;
	
	private int energy;
	
	private boolean valid = true; //正しいレシピかどうか。OreDictionaryが空だとfalseになる
	
	/*
	 * ingredientItem: ItemStack or String or OreDictContainer
	 * ingredientFluid: FluidStack ore FluidDictContainer
	 * energy: FU base
	 */
	public ReactorRecipeStandard(Object[] ingredientItem, Object[] ingredientFluid, ItemStackWithChance[] resultItem, FluidStack[] resultFluid, int energy) {
		this.ingredientItem = NonNullList.create();
		for (Object item : ingredientItem) {
			if (item instanceof ItemStack) {
				this.ingredientItem.add(NonNullList.from(ItemStack.EMPTY, (ItemStack)item));
			} else if (item instanceof String) {
				OreDictContainer container = new OreDictContainer((String)item);
				this.ingredientItem.add(container.getItems());
				if (container.getItems().isEmpty()) markInvalid();
			} else if (item instanceof OreDictContainer) {
				this.ingredientItem.add(((OreDictContainer)item).getItems());
				if (((OreDictContainer)item).getItems().isEmpty()) markInvalid();
			}
		}
		this.ingredientFluid = NonNullList.create();
		for (Object fluid : ingredientFluid) {
			if (fluid instanceof FluidStack) {
				List<FluidStack> stacks = new ArrayList<FluidStack>();
				stacks.add((FluidStack) fluid);
				this.ingredientFluid.add(stacks);
			} else if (fluid instanceof FluidDictContainer) {
				this.ingredientFluid.add(((FluidDictContainer)fluid).getFluids());
			}
		}
		this.resultItem = NonNullList.from(null, resultItem);
		this.resultFluid = Arrays.asList(resultFluid);
		this.energy = energy;
	}
	
	public ReactorRecipeStandard(NonNullList<ItemStack> ingredientItem, Collection<FluidStack> ingredientFluid, NonNullList<ItemStackWithChance> resultItem, Collection<FluidStack> resultFluid, int energy) {
		this.ingredientItem = NonNullList.create();
		for (ItemStack itemstack : ingredientItem) {
			this.ingredientItem.add(NonNullList.from(ItemStack.EMPTY, itemstack));
		}
		this.ingredientFluid = NonNullList.create();
		for (FluidStack fluidstack : ingredientFluid) {
			ArrayList<FluidStack> fluid = new ArrayList<FluidStack>();
			fluid.add(fluidstack);
			this.ingredientFluid.add(fluid);
		}
		this.resultItem = resultItem;
		this.resultFluid = new ArrayList(resultFluid);
		this.energy = energy;
	}
	
	@Override
	public boolean canCraft(NonNullList<ItemStack> items, List<FluidStack> fluids) {
		if (!isValid()) return false;
		
		for (List<ItemStack> itemList : ingredientItem) {
			boolean flag = false;
			if (itemList.isEmpty()) continue;
			int count = itemList.get(0).getCount();
			if (GmpmItemUtil.takeItemStack(items, count, false, itemList.toArray(new ItemStack[0])) >= count) flag = true;
			if (!flag) return false;
		}
		for (List<FluidStack> fluidList : ingredientFluid) {
			boolean flag = false;
			for (FluidStack fluid : fluidList) {
				if (GmpmFluidUtil.containFluid(fluid, fluids)) flag = true;
			}
			if (!flag) return false;
		}
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getResultItemFromInput(NonNullList<ItemStack> items, List<FluidStack> fluids, Random rand) {
		NonNullList<ItemStack> list = NonNullList.create();
		if(!this.canCraft(items, fluids)) return list; //クラフト不可なら空
		for (ItemStackWithChance item : resultItem) {
			if (item.chance > rand.nextFloat()) {
				list.add(item.itemstack);
			} else {
				list.add(ItemStack.EMPTY);
			}
		}
		return list;
	}
	
	@Override
	public NonNullList<ItemStack> getResultItemAll(NonNullList<ItemStack> items, List<FluidStack> fluids) {
		NonNullList<ItemStack> list = NonNullList.create();
		for (ItemStackWithChance item : getResultItemWithChance()) {
			if(item.chance > 0) {
				list.add(item.itemstack);
			}
		}
		return list;
	}
	
	@Override
	public List<FluidStack> getResultFluidFromInput(NonNullList<ItemStack> items, List<FluidStack> fluids) {
		if(!this.canCraft(items, fluids)) return new ArrayList<FluidStack>();
		return getResultFluid();
	}
	
	@Override
	public NonNullList<List<ItemStack>> getIngredientItem() {
		NonNullList<List<ItemStack>> list = NonNullList.create();
		for(NonNullList<ItemStack> itemList : ingredientItem) {
			List<ItemStack> newItemList = NonNullList.create();
			for (ItemStack item : itemList) {
				newItemList.add(item.copy());
			}
			list.add(newItemList);
		}
		return list;
	}
	
	@Override
	public NonNullList<List<FluidStack>> getIngredientFluid() {
		NonNullList<List<FluidStack>> list = NonNullList.create();
		for (List<FluidStack> fluidList : ingredientFluid) {
			List<FluidStack> newFluidList = new ArrayList<FluidStack>();
			for(FluidStack fluid : fluidList) {
				if (fluid != null) newFluidList.add(fluid.copy());
			}
			list.add(newFluidList);
		}
		return list;
	}
	
	@Override
	public NonNullList<ItemStackWithChance> getResultItemWithChance() {
		NonNullList<ItemStackWithChance> list = NonNullList.create();
		for(ItemStackWithChance item : resultItem) {
			list.add(item.copy());
		}
		return list;
	}
	
	@Override
	public NonNullList<ItemStack> getResultItem() {
		NonNullList<ItemStack> list = NonNullList.create();
		for (ItemStackWithChance item : getResultItemWithChance()) {
			list.add(item.itemstack);
		}
		return list;
	}
	
	@Override
	public List<FluidStack> getResultFluid() {
		ArrayList<FluidStack> list = new ArrayList<FluidStack>();
		for(FluidStack fluid : resultFluid) {
			if(fluid != null) list.add(fluid.copy());
		}
		return list;
	}
	
	@Override
	public int getEnergy() {
		return energy;
	}

	@Override
	public int getPriority() {
		return 0;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	private void markInvalid() {
		valid = false;
	}

}
