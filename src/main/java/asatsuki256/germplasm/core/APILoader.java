package asatsuki256.germplasm.core;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.recipe.RecipeAPI;
import asatsuki256.germplasm.core.gene.GeneHelper;
import asatsuki256.germplasm.core.gene.GeneNBTHelper;
import asatsuki256.germplasm.core.gene.GenePoolRegistry;
import asatsuki256.germplasm.core.gene.trait.TraitTypeRegistry;
import asatsuki256.germplasm.core.recipe.GmpmRecipeRegistry;

public class APILoader {
	
	public static void loadAPI() {
		GeneAPI.traitTypeRegistry = new TraitTypeRegistry();
		GeneAPI.genePoolRegistry = new GenePoolRegistry();
		
		GeneAPI.nbtHelper = new GeneNBTHelper();
		GeneAPI.geneHelper = new GeneHelper();
		
		GeneAPI.isLoaded = true;
		
		RecipeAPI.recipeRegistry = new GmpmRecipeRegistry();
		
		RecipeAPI.isLoaded = true;
	}

}
