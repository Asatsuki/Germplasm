package asatsuki256.germplasm.core.client.model;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

public class SeedModel implements IModel{
	
	ModelResourceLocation mrlMain = new ModelResourceLocation(new ResourceLocation(MODID, "seed/seed_sample_plate"), "inventory");
	private Collection<TraitTypeHarvest> harvests = getHarvests();
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		List<ResourceLocation> dependencies = new ArrayList<ResourceLocation>();
		
		dependencies.add(mrlMain);
		
		for(TraitTypeHarvest harvest : harvests) {
			dependencies.add(harvest.getItemModel());
		}
        return dependencies;
    }
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		
		
		IBakedModel modelMain;
		Map<String, IBakedModel> modelSeed = new HashMap<String, IBakedModel>();
		
		for(TraitTypeHarvest traitType : harvests) {
			IBakedModel modelSeedBaked;
			try {
				modelSeedBaked = ModelLoaderRegistry.getModel(((TraitTypeHarvest) traitType).getItemModel()).bake(state, format, bakedTextureGetter);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			modelSeed.put(traitType.traitId, modelSeedBaked);
		}
		
		try {
			modelMain = ModelLoaderRegistry.getModel(mrlMain).bake(state, format, bakedTextureGetter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return new SeedBakedModel(modelMain, modelSeed);
	}
	
	private Collection<TraitTypeHarvest> getHarvests(){
		List<TraitTypeHarvest> harvests = new ArrayList<TraitTypeHarvest>();
		for(TraitType traitType : GeneAPI.traitTypeRegistry.getTraitTypeList()) {
			if(traitType instanceof TraitTypeHarvest) {
				harvests.add((TraitTypeHarvest) traitType);
			}
		}
		return harvests;
	}

}
