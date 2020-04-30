package asatsuki256.germplasm.core.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class SeedBakedModelFinal implements IBakedModel{

	private final IBakedModel modelMain;
	private final Map<String, IBakedModel> modelSeed; //keyがTraitId、valueがTraitIdに応じたBakedModel
	private ItemStack itemStack;
	
	public SeedBakedModelFinal(IBakedModel modelMain, Map<String, IBakedModel> modelSeed)
	{
		this.modelMain = modelMain;
		this.modelSeed = modelSeed;
		this.itemStack = null;
	}
	
	public SeedBakedModelFinal setCurrentItemStack(ItemStack itemStack)
	{
		this.itemStack = itemStack;
		return this;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		ArrayList<BakedQuad> list = new ArrayList<BakedQuad>();
		list.addAll(modelMain.getQuads(state, side, rand));
		
		//ItemStackのNBTからgenomeを読み込み
		IGenome genome = GeneAPI.nbtHelper.getGenomeFromIndividualNBT(itemStack.getTagCompound());
		if(genome != null) {
			TraitTypeHarvest harvest = GeneAPI.geneHelper.getPrimaryHarvest(genome);
			if(harvest != null && this.modelSeed.containsKey(harvest.traitId)) {
				//genomeのPrimaryHarvestのモデルをとってきて、Quadsに加える
				list.addAll(this.modelSeed.get(harvest.traitId).getQuads(state, side, rand));
			}
		}
		
		return list;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.modelMain.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.modelMain.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return this.modelMain.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return this.modelMain.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return this.modelMain.getOverrides();
	}
	
	public org.apache.commons.lang3.tuple.Pair<? extends IBakedModel, javax.vecmath.Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		return org.apache.commons.lang3.tuple.Pair.of(this, modelMain.handlePerspective(cameraTransformType).getRight());
    }

}
