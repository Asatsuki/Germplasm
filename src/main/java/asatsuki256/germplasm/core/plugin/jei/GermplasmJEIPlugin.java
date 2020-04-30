package asatsuki256.germplasm.core.plugin.jei;

import java.util.ArrayList;
import java.util.List;

import asatsuki256.germplasm.api.recipe.IReactorRecipe;
import asatsuki256.germplasm.api.recipe.RecipeAPI;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import asatsuki256.germplasm.core.item.GermplasmItems;
import asatsuki256.germplasm.core.plugin.jei.RecipeCategoryIncubator.RecipeWrapperIncubator;
import asatsuki256.germplasm.core.plugin.jei.RecipeCategoryReactor.RecipeWrapperReactor;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class GermplasmJEIPlugin implements IModPlugin{
	
	private RecipeCategoryReactor reactor;
	private RecipeCategoryIncubator incubator;
	
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

	}
	
	public void registerIngredients(IModIngredientRegistration registry) {

	}
	
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		reactor = new RecipeCategoryReactor(jeiHelpers.getGuiHelper());
		registry.addRecipeCategories(reactor);
		incubator = new RecipeCategoryIncubator(jeiHelpers.getGuiHelper());
		registry.addRecipeCategories(incubator);
	}
	
	public void register(IModRegistry registry) {
		//reactor
		List<RecipeWrapperReactor> reactorRecipe = new ArrayList<RecipeWrapperReactor>();
		for (IReactorRecipe recipe : RecipeAPI.recipeRegistry.getAllReactorRecipe()) {
			if (recipe.isValid()) {
				reactorRecipe.add(new RecipeWrapperReactor(recipe));
			}
		}
		registry.addRecipes(reactorRecipe, reactor.getUid());
		registry.addRecipeCatalyst(new ItemStack(GermplasmBlocks.reactor), reactor.getUid());
		//incubator
		List<RecipeWrapperIncubator> incubatorRecipe = new ArrayList<RecipeWrapperIncubator>();
		incubatorRecipe.add(new RecipeWrapperIncubator(new ItemStack(GermplasmItems.seed_sample), new ItemStack(GermplasmItems.solid_medium_auxin), new ItemStack(GermplasmItems.callus)));
		incubatorRecipe.add(new RecipeWrapperIncubator(new ItemStack(GermplasmItems.callus), new ItemStack(GermplasmItems.solid_medium_auxin), new ItemStack(GermplasmItems.callus, 2)));
		incubatorRecipe.add(new RecipeWrapperIncubator(new ItemStack(GermplasmItems.callus), new ItemStack(GermplasmItems.solid_medium), new ItemStack(GermplasmItems.seed_sample)));
		registry.addRecipes(incubatorRecipe, incubator.getUid());
		registry.addRecipeCatalyst(new ItemStack(GermplasmBlocks.incubator), incubator.getUid());
	}
	
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}

}
