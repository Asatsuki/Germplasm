package asatsuki256.germplasm.core.recipe;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import asatsuki256.germplasm.api.recipe.IGermplasmRecipeRegistry;
import asatsuki256.germplasm.api.recipe.IReactorRecipe;
import asatsuki256.germplasm.api.recipe.ItemStackWithChance;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import asatsuki256.germplasm.core.fluid.GermplasmFluids;
import asatsuki256.germplasm.core.item.GermplasmItems;
import asatsuki256.germplasm.core.util.OreDictContainer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class GmpmRecipeRegistry implements IGermplasmRecipeRegistry{
	
	private int defaultEnergy = 10000;

	private List<IReactorRecipe> reactorRegistry;

	private List<IReactorRecipe> reactorRegistrySort;

	public GmpmRecipeRegistry() {
		reactorRegistry = new ArrayList<IReactorRecipe>();
		reactorRegistrySort = new ArrayList<IReactorRecipe>();
	}

	public boolean register(IReactorRecipe recipe) {
		if (reactorRegistry.add(recipe)) {
			reactorRegistrySort = reactorRegistry.stream()
				.sorted(comparing(IReactorRecipe::getPriority)
						.thenComparing(x -> reactorRegistry.indexOf(x)))
				.collect(Collectors.toList());
			return true;
		}
		return false;
	}
	
	public IReactorRecipe getReactorRecipe(NonNullList<ItemStack> items, List<FluidStack> fluids) {
		for (IReactorRecipe recipe : reactorRegistrySort) {
			if (recipe.canCraft(items, fluids)) {
				return recipe;
			}
		}
		return null;
	}
	
	public List<IReactorRecipe> getAllReactorRecipe(){
		return new ArrayList<IReactorRecipe>(reactorRegistry);
	}

	public void registerAll() {
		//Ore Dictionary
		OreDictionary.registerOre("itemSilicon", GermplasmItems.silicon);
		//Vanilla furnace recipe
		GameRegistry.addSmelting(GermplasmBlocks.bioplastic_tile, new ItemStack(GermplasmBlocks.organic_glass), 0.1f);
		GameRegistry.addSmelting(GermplasmItems.claylily, new ItemStack(GermplasmItems.silicon), 0.1f);
		
		register(new ReactorRecipeStandard(
				new Object[] {new OreDictContainer("cropPotato", 8)}, 
				new Object[] {} , 
				new ItemStackWithChance[] {
						new ItemStackWithChance(new ItemStack(GermplasmItems.bioplastic), 1f),
						new ItemStackWithChance(new ItemStack(GermplasmItems.biomass, 2), 1f)}, 
				new FluidStack[] {},
				defaultEnergy * 2
				));
		register(new ReactorRecipeStandard(
				new Object[] {new ItemStack(GermplasmItems.biomass), new ItemStack(Items.SUGAR)}, 
				new Object[] {new FluidStack(FluidRegistry.WATER, 500)} , 
				new ItemStackWithChance[] {}, 
				new FluidStack[] {new FluidStack(GermplasmFluids.bioethanol, 250)},
				defaultEnergy / 2
				));
		register(new ReactorRecipeStandard(
				new ItemStack[] {new ItemStack(Items.FERMENTED_SPIDER_EYE)}, 
				new FluidStack[] {new FluidStack(FluidRegistry.WATER, 250)} , 
				new ItemStackWithChance[] {}, 
				new FluidStack[] {new FluidStack(GermplasmFluids.mutagen, 250)},
				defaultEnergy
				));
		register(new ReactorRecipeStandard(
				new Object[] {new ItemStack(Items.SLIME_BALL), new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())}, 
				new Object[] {new FluidStack(FluidRegistry.WATER, 100)} , 
				new ItemStackWithChance[] {new ItemStackWithChance(new ItemStack(GermplasmItems.solid_medium), 1f)}, 
				new FluidStack[] {},
				defaultEnergy / 2
				));
		register(new ReactorRecipeStandard(
				new Object[] {new ItemStack(GermplasmItems.seed_sample)}, 
				new Object[] {new FluidStack(GermplasmFluids.bioethanol, 50)} , 
				new ItemStackWithChance[] {new ItemStackWithChance(new ItemStack(GermplasmItems.auxin), 1f)}, 
				new FluidStack[] {},
				defaultEnergy / 2
				));
		
		//Crop Extract
		register(new ReactorRecipeStandard(
				new Object[] {new ItemStack(Items.GOLDEN_CARROT)}, 
				new Object[] {} , 
				new ItemStackWithChance[] {new ItemStackWithChance(new ItemStack(Items.GOLD_NUGGET), 0.50f),
						new ItemStackWithChance(new ItemStack(Items.GOLD_NUGGET), 0.30f)}, 
				new FluidStack[] {},
				defaultEnergy
				));
		register(new ReactorRecipeStandard(
				new Object[] {new ItemStack(GermplasmItems.claylily)}, 
				new Object[] {} , 
				new ItemStackWithChance[] {new ItemStackWithChance(new ItemStack(Items.CLAY_BALL, 4), 1.00f),
						new ItemStackWithChance(new ItemStack(Items.CLAY_BALL, 4), 0.50f)}, 
				new FluidStack[] {},
				defaultEnergy
				));
		register(new ReactorRecipeStandard(
				new Object[] {new ItemStack(GermplasmItems.redstorgham)}, 
				new Object[] {} , 
				new ItemStackWithChance[] {new ItemStackWithChance(new ItemStack(Items.REDSTONE), 1.00f),
						new ItemStackWithChance(new ItemStack(Items.REDSTONE), 0.50f)}, 
				new FluidStack[] {},
				defaultEnergy
				));
		
		//dNTP
		Map<Object, Integer> dNTPOres = new HashMap<>();
		dNTPOres.put("seedWheat", 3);
		dNTPOres.put("seedPumpkin", 3);
		dNTPOres.put("seedMelon", 3);
		dNTPOres.put("seedBeetroot", 3);
		dNTPOres.put("cropPotato", 4);
		dNTPOres.put("cropWheat", 3);
		dNTPOres.put("cropCarrot", 6);
		dNTPOres.put("cropBeetroot", 6);
		dNTPOres.put(new ItemStack(Items.CHICKEN), 12);
		dNTPOres.put(new ItemStack(Items.PORKCHOP), 16);
		dNTPOres.put(new ItemStack(Items.BEEF), 18);
		dNTPOres.put(new ItemStack(Items.MUTTON), 14);
		dNTPOres.put(new ItemStack(Items.RABBIT), 12);
		dNTPOres.put(new ItemStack(Items.FISH), 24);
		dNTPOres.put("cropSoyBean", 12);
		dNTPOres.put("listAllSeed", 3);
		dNTPOres.put("listAllVeggie", 4);
		for (Object dNTPOre : dNTPOres.keySet()) {
			int amount = dNTPOres.get(dNTPOre);
			register(new ReactorRecipeStandard(
					new Object[] {dNTPOre}, 
					new Object[] {new FluidStack(GermplasmFluids.bioethanol, amount)} , 
					new ItemStackWithChance[] {}, 
					new FluidStack[] {new FluidStack(GermplasmFluids.nucleotide, amount)},
					amount * 100
					));
		}
	}

}
