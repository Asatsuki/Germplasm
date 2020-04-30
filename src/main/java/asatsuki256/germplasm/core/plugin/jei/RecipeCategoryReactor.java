package asatsuki256.germplasm.core.plugin.jei;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import asatsuki256.germplasm.api.recipe.IReactorRecipe;
import asatsuki256.germplasm.api.recipe.ItemStackWithChance;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class RecipeCategoryReactor<RecipeWrapperReactor> implements IRecipeCategory{

	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	
	private int xOff = 25;
	private int yOff = 32;
	
	public RecipeCategoryReactor(IGuiHelper guiHelper) {
		ResourceLocation backgroundLocation = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/reactor_jei.png");
		background = guiHelper.createDrawable(backgroundLocation, xOff, yOff, 146, 68);
		icon = guiHelper.createDrawableIngredient(new ItemStack(GermplasmBlocks.reactor));
		localizedName = I18n.format("germplasm.recipe.reactor");
	}
	
	@Override
	public String getUid() {
		return "ReactorRecipe";
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
		guiItemStacks.init(0, true, 53-xOff, 33-yOff);
		guiItemStacks.init(1, true, 53-xOff, 51-yOff);
		guiItemStacks.init(2, true, 53-xOff, 69-yOff);
		guiItemStacks.init(3, false, 107-xOff, 33-yOff);
		guiItemStacks.init(4, false, 107-xOff, 51-yOff);
		guiItemStacks.init(5, false, 107-xOff, 69-yOff);
		guiItemStacks.set(ingredients);
		
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(0, true, 27-xOff, 34-yOff, 16, 64, 1000, false, null);
		guiFluidStacks.init(1, false, 153-xOff, 34-yOff, 16, 64, 1000, false, null);
		guiFluidStacks.set(ingredients);
	}
	
	static public class RecipeWrapperReactor implements IRecipeWrapper {

		IReactorRecipe recipe;
		
		public RecipeWrapperReactor(IReactorRecipe recipe) {
			super();
			this.recipe = recipe;
		}
		
		@Override
		public void getIngredients(IIngredients ingredients) {
			ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredientItem());
			ingredients.setInputLists(VanillaTypes.FLUID, recipe.getIngredientFluid());
			ingredients.setOutputs(VanillaTypes.ITEM, recipe.getResultItem());
			ingredients.setOutputs(VanillaTypes.FLUID, recipe.getResultFluid());
		}
		
		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			FontRenderer fr = minecraft.fontRenderer;
			NonNullList<ItemStackWithChance> outputs = recipe.getResultItemWithChance();
			int x = 102;
			int y = 7;
			for (ItemStackWithChance output : outputs) {
				int percent = (int) Math.ceil(output.chance * 100);
				fr.drawString(percent + "%", x, y, 0x404040, false);
				y += 18;
			}
			fr.drawString(recipe.getEnergy() + " FE", 25, 59, 0x404040, false);
		}
		
	}

}
