package asatsuki256.germplasm.core.client.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SeedBakedModel implements IBakedModel {

	private final IBakedModel modelMain;
	private final SeedBakedModelFinal modelFinal;
	private final OverridesList overridesList;
	
	public SeedBakedModel(IBakedModel modelMain, Map<String, IBakedModel> modelSeed) {
		this.modelMain = modelMain;
		this.modelFinal = new SeedBakedModelFinal(this.modelMain, modelSeed);
		this.overridesList = new OverridesList(this);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return this.modelMain.getQuads(state, side, rand);
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
		return this.overridesList;
	}
	
	public SeedBakedModelFinal getModelFinal()
	{
		return modelFinal;
	}
	
	private static class OverridesList extends ItemOverrideList
	{
		private SeedBakedModel model;
		
		public OverridesList(SeedBakedModel model)
		{
			super(Collections.EMPTY_LIST);
			this.model = model;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack itemStack, World world, EntityLivingBase entity)
		{
			return this.model.getModelFinal().setCurrentItemStack(itemStack);
		}
	}

}
