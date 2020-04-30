package asatsuki256.germplasm.core.client.model;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ReagentBottleModelLoader implements ICustomModelLoader{
	
	private static ModelResourceLocation mrlMain = new ModelResourceLocation(MODID + ":reagent_bottle", "inventory");
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		// NOOP
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.equals(mrlMain);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return new ReagentBottleModel();
	}

}
