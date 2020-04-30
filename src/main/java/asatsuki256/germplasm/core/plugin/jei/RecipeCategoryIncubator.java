package asatsuki256.germplasm.core.plugin.jei;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import java.util.ArrayList;
import java.util.List;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RecipeCategoryIncubator implements IRecipeCategory {
	
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	
	private int xOff = 37;
	private int yOff = 42;
	
	public RecipeCategoryIncubator(IGuiHelper guiHelper) {
		ResourceLocation backgroundLocation = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/incubator.png");
		background = guiHelper.createDrawable(backgroundLocation, xOff, yOff, 104, 48);
		icon = guiHelper.createDrawableIngredient(new ItemStack(GermplasmBlocks.incubator));
		localizedName = I18n.format("germplasm.recipe.incubator");
	}
	
	@Override
	public String getUid() {
		return "IncubatorRecipe";
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public String getModName() {
		return MODID;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public IDrawable getIcon() {
		return icon;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 45-xOff, 57-yOff);
		guiItemStacks.init(1, true, 80-xOff, 57-yOff);
		guiItemStacks.init(2, false, 115-xOff, 57-yOff);
		guiItemStacks.set(ingredients);
	}
	
	static public class RecipeWrapperIncubator implements IRecipeWrapper {

		ItemStack ingredient;
		ItemStack medium;
		ItemStack result;
		
		public RecipeWrapperIncubator(ItemStack ingredient, ItemStack medium, ItemStack result) {
			super();
			this.ingredient = ingredient;
			this.medium = medium;
			this.result = result;
		}
		
		@Override
		public void getIngredients(IIngredients ingredients) {
			List<ItemStack> list = new ArrayList<ItemStack>();
			list.add(ingredient);
			list.add(medium);
			ingredients.setInputs(VanillaTypes.ITEM, list);
			ingredients.setOutput(VanillaTypes.ITEM, result);
		}
		
		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			
		}
		
	}

}
