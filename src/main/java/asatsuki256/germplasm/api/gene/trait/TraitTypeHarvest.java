package asatsuki256.germplasm.api.gene.trait;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.core.item.GermplasmItems;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

public class TraitTypeHarvest extends TraitType{
	
	static {
		
		}
	
	public static final ResourceLocation[] DEFAULT_TEXTURES = new ResourceLocation[] {
			new ResourceLocation(MODID, "textures/crops/weed/0.png"),
			new ResourceLocation(MODID, "textures/crops/weed/1.png"),
			new ResourceLocation(MODID, "textures/crops/weed/2.png"),
			new ResourceLocation(MODID, "textures/crops/weed/3.png"),
			new ResourceLocation(MODID, "textures/crops/weed/4.png"),
			new ResourceLocation(MODID, "textures/crops/weed/5.png"),
			new ResourceLocation(MODID, "textures/crops/weed/6.png"),
			new ResourceLocation(MODID, "textures/crops/weed/7.png"),
	};
	
	private ItemStack harvest = ItemStack.EMPTY;
	private float harvestMultiplier = 1.0f;
	private float growSpeed = 1.0f;
	private ResourceLocation[] textures = DEFAULT_TEXTURES.clone();
	private ModelResourceLocation itemModel;
	private RenderType renderType = RenderType.CROP;
	
	public TraitTypeHarvest(String traitId, String unlocalizedName) {
		super(traitId, unlocalizedName);

	}
	
	public TraitTypeHarvest setHarvest(ItemStack harvest) {
		this.harvest = harvest;
		return this;
	}
	
	public TraitTypeHarvest setHarvestMultiplier(float harvestMultiplier) {
		this.harvestMultiplier = harvestMultiplier;
		return this;
	}
	
	//textures/crops/wheat_stage -> textures/crops/wheat_stage_7.png
	public TraitTypeHarvest setTexture(ResourceLocation loc) {
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new ResourceLocation(loc.getResourceDomain(), loc.getResourcePath() + "/" + i + ".png");
		}
		return this;
	}
	
	public TraitTypeHarvest setRenderType(RenderType renderType) {
		this.renderType = renderType;
		return this;
	}
	
	public TraitTypeHarvest setItemModel(ModelResourceLocation itemModel) {
		this.itemModel = itemModel;
		ModelBakery.registerItemVariants(GermplasmItems.seed_sample, itemModel);
		return this;
	}
	
	public ItemStack getHarvest() {
		return harvest.copy();
	}
	
	public float getHarvestMultiplier() {
		return harvestMultiplier;
	}
	
	public ResourceLocation[] getTextures() {
		return textures;
	}
	
	public RenderType getRenderType() {
		return renderType;
	}
	
	public ModelResourceLocation getItemModel() {
		return itemModel;
	}
	
	public enum RenderType {
		CROP, CROSS
	};

}
