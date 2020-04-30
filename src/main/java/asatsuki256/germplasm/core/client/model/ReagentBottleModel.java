package asatsuki256.germplasm.core.client.model;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class ReagentBottleModel implements IModel {
	
	private static final float NORTH_Z_FLUID = 7.502f / 16f;
    private static final float SOUTH_Z_FLUID = 8.498f / 16f;
	
    ModelResourceLocation mrlMain = new ModelResourceLocation(new ResourceLocation(MODID, "seed/seed_sample_plate"), "inventory");
    
	private final ResourceLocation bottleLocation = new ResourceLocation(MODID, "items/reagent_bottle_large"); //瓶の外形テクスチャ
	private final ResourceLocation liquidLocation = new ResourceLocation(MODID, "items/reagent_bottle_large_fill"); //瓶の中に入ってる液体の形テクスチャ
	private final Fluid fluid;
	
	public ReagentBottleModel()
    {
        this(null);
    }
	
	public ReagentBottleModel(@Nullable Fluid fluid)
    {
        this.fluid = fluid;
    }
	
	@Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

        if (bottleLocation != null)
            builder.add(bottleLocation);
        if (liquidLocation != null)
            builder.add(liquidLocation);
        if (fluid != null)
            builder.add(fluid.getStill());

        return builder.build();
    }
	
	@Override
    public Collection<ResourceLocation> getDependencies()
    {
		List<ResourceLocation> dependencies = new ArrayList<ResourceLocation>();
		
		dependencies.add(mrlMain);
        return dependencies;
    }

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
		
		TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());
        TextureAtlasSprite fluidSprite = null;
        TextureAtlasSprite particleSprite = null;
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder(); //IBakedModelのQuad
        
        if(fluid != null) {
            fluidSprite = bakedTextureGetter.apply(fluid.getStill());
        }
        if (bottleLocation != null)
        {
            IBakedModel model = (new ItemLayerModel(ImmutableList.of(bottleLocation))).bake(state, format, bakedTextureGetter);
            builder.addAll(model.getQuads(null, null, 0));
            particleSprite = model.getParticleTexture();
        }
        if (liquidLocation != null && fluidSprite != null)
        {
            TextureAtlasSprite liquid = bakedTextureGetter.apply(liquidLocation);
            builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, NORTH_Z_FLUID, EnumFacing.NORTH, fluid.getColor(), 1));
            builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, SOUTH_Z_FLUID, EnumFacing.SOUTH, fluid.getColor(), 1));
            particleSprite = fluidSprite;
        }
        
        IBakedModel modelMain;
        try {
			modelMain = ModelLoaderRegistry.getModel(mrlMain).bake(state, format, bakedTextureGetter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        
		return new Baked(this, builder.build(), fluidSprite, format, Maps.immutableEnumMap(transformMap), Maps.<String, IBakedModel> newHashMap(), modelMain);
	}
	
	@Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }
	
	@Override
    public ReagentBottleModel process(ImmutableMap<String, String> customData)
    {
        String fluidName = customData.get("fluid");
        Fluid fluid = FluidRegistry.getFluid(fluidName);

        if (fluid == null)
            fluid = this.fluid;

        // create new model with correct liquid
        return new ReagentBottleModel(fluid);
    }
	
	@Override
    public ReagentBottleModel retexture(ImmutableMap<String, String> textures)
    {
        return new ReagentBottleModel(fluid);
    }
	
	private static final class BakedOverrideHandler extends ItemOverrideList
    {
        public static final BakedOverrideHandler INSTANCE = new BakedOverrideHandler();

        private BakedOverrideHandler()
        {
            super(ImmutableList.<ItemOverride> of());

//            // DEBUG
//            System.out.println("Constructing BakedOverrideHandler");
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
            FluidStack fluidStack = null;
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey(FluidHandlerItemStack.FLUID_NBT_KEY))
            {
                fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(FluidHandlerItemStack.FLUID_NBT_KEY));
            }

            if (fluidStack == null || fluidStack.amount <= 0)
            {
                // // DEBUG
                 //System.out.println("fluid stack is null, returning original model");

                // empty bucket
                return originalModel;
            }

            // // DEBUG
             //System.out.println("Fluid stack was not null and fluid amount = "+fluidStack.amount);

            Baked model = (Baked) originalModel;

            Fluid fluid = fluidStack.getFluid();
            String name = fluid.getName();

            if (!model.cache.containsKey(name))
            {
                // DEBUG
                //System.out.println("The model cache does not have key for fluid name");
                IModel parent = model.parent.process(ImmutableMap.of("fluid", name));
                Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
                    @Override
                    public TextureAtlasSprite apply(ResourceLocation location)
                    {
                        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    }
                };
                IBakedModel bakedModel = parent.bake(new SimpleModelState(model.transforms), model.format,
                        textureGetter);
                model.cache.put(name, bakedModel);
                return bakedModel;
            }

            // // DEBUG
             //System.out.println("The model cache already has key so returning the model");

            return model.cache.get(name);
        }
    }
	
	private static final class Baked extends BakedItemModel
    {

        private final ReagentBottleModel parent;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final ImmutableMap<TransformType, TRSRTransformation> transforms;
        private final ImmutableList<BakedQuad> quads;
        private final TextureAtlasSprite particle;
        private final VertexFormat format;
        private IBakedModel transformModel;

        public Baked(ReagentBottleModel parent,
                ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format,
                ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
                Map<String, IBakedModel> cache,
                IBakedModel transformModel)
        {
            super(quads, particle, transforms, BakedOverrideHandler.INSTANCE, false);
        	this.quads = quads;
            this.particle = particle;
            this.format = format;
            this.parent = parent;
            this.transforms = transforms;
            this.cache = cache;
            this.transformModel = transformModel;
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return BakedOverrideHandler.INSTANCE;
        }
        
        

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
        {
        	return Pair.of(this, transformModel.handlePerspective(cameraTransformType).getRight());
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
        {
            if (side == null)
                return quads;
            return ImmutableList.of();
        }

        @Override
        public boolean isAmbientOcclusion()
        {
            return true;
        }

        @Override
        public boolean isGui3d()
        {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer()
        {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture()
        {
            return particle;
        }
    }

}
