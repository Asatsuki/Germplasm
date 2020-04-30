package asatsuki256.germplasm.core.gene.trait;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;
import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.trait.ITraitTypeRegistry;
import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest;
import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest.RenderType;
import asatsuki256.germplasm.core.item.GermplasmItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TraitTypeRegistry implements ITraitTypeRegistry{
	
	private Map<String, TraitType> list;
	
	public TraitTypeRegistry() {
		list = new HashMap<String, TraitType>();
		registerAll();
	}
	
	public boolean register(TraitType traitType) {
		if(!list.containsKey(traitType.traitId)) {
			list.put(traitType.traitId, traitType);
			return true;
		}
		return false;
	}
	
	public TraitType getTraitType(String id) {
		return list.get(id);
	}
	
	public Collection<TraitType> getTraitTypeList() {
		return list.values();
	}
	
	private void registerAll() {
		register(gain);
		register(growth);
		register(heat_tolerance);
		register(cold_tolerance);
		
		register(wheat);
		register(carrot);
		register(potato);
		register(tomato);
		register(waterlily);
		register(rose);
		register(golden_carrot);
		register(iron_potato);
		register(claylily);
		register(redstorgham);
	}
	
	public static TraitType gain = new TraitType(MODID + "." + "gain", UNLOC_PREFIX + "gain");
	public static TraitType growth = new TraitType(MODID + "." + "growth", UNLOC_PREFIX + "growth");
	public static TraitType heat_tolerance = new TraitType(MODID + "." + "heat_tolerance", UNLOC_PREFIX + "heat_tolerance");
	public static TraitType cold_tolerance = new TraitType(MODID + "." + "cold_tolerance", UNLOC_PREFIX + "cold_tolerance");
	//harvest
	public static TraitType wheat = new TraitTypeHarvest(MODID+"."+"wheat", UNLOC_PREFIX+"wheat")
			.setHarvest(new ItemStack(Items.WHEAT))
			.setHarvestMultiplier(1.0f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/wheat"))
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/wheat"), "inventory"));
	public static TraitType carrot = new TraitTypeHarvest(MODID+"."+"carrot", UNLOC_PREFIX+"carrot")
			.setHarvest(new ItemStack(Items.CARROT))
			.setHarvestMultiplier(2.7f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/carrots"))
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/carrot"), "inventory"));
	public static TraitType potato = new TraitTypeHarvest(MODID+"."+"potato", UNLOC_PREFIX+"potato")
			.setHarvest(new ItemStack(Items.POTATO))
			.setHarvestMultiplier(3.0f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/potatoes"))
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/potato"), "inventory"));
	public static TraitType waterlily = new TraitTypeHarvest(MODID+"."+"waterlily", UNLOC_PREFIX+"waterlily")
			.setHarvest(new ItemStack(Blocks.WATERLILY))
			.setHarvestMultiplier(2.0f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/waterlily"))
			.setRenderType(RenderType.CROSS)
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/waterlily"), "inventory"));
	public static TraitType rose = new TraitTypeHarvest(MODID+"."+"rose", UNLOC_PREFIX+"rose")
			.setHarvest(new ItemStack(Items.DYE, 1, 1))
			.setHarvestMultiplier(3.3f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/rose"))
			.setRenderType(RenderType.CROSS)
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/rose"), "inventory"));
	public static TraitType tomato = new TraitTypeHarvest(MODID+"."+"tomato", UNLOC_PREFIX+"tomato")
			.setHarvest(new ItemStack(GermplasmItems.tomato))
			.setHarvestMultiplier(3.1f)
			.setRenderType(TraitTypeHarvest.RenderType.CROSS)
			.setTexture(new ResourceLocation(MODID, "textures/crops/tomato"))
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/tomato"), "inventory"));
	public static TraitType golden_carrot = new TraitTypeHarvest(MODID+"."+"golden_carrot", UNLOC_PREFIX+"golden_carrot")
			.setHarvest(new ItemStack(Items.GOLDEN_CARROT))
			.setHarvestMultiplier(1.6f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/golden_carrots"))
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/golden_carrot"), "inventory"));
	public static TraitType iron_potato = new TraitTypeHarvest(MODID+"."+"iron_potato", UNLOC_PREFIX+"iron_potato")
			.setHarvest(new ItemStack(GermplasmItems.iron_potato))
			.setHarvestMultiplier(2.1f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/iron_potatoes"))
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/iron_potato"), "inventory"));
	public static TraitType claylily = new TraitTypeHarvest(MODID+"."+"claylily", UNLOC_PREFIX+"claylily")
			.setHarvest(new ItemStack(GermplasmItems.claylily))
			.setHarvestMultiplier(1.0f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/claylily"))
			.setRenderType(RenderType.CROSS)
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/claylily"), "inventory"));
	public static TraitType redstorgham = new TraitTypeHarvest(MODID+"."+"redstorgham", UNLOC_PREFIX+"redstorgham")
			.setHarvest(new ItemStack(GermplasmItems.redstorgham))
			.setHarvestMultiplier(1.0f)
			.setTexture(new ResourceLocation(MODID, "textures/crops/redstorgham"))
			.setItemModel(new ModelResourceLocation(new ResourceLocation(MODID, "seed/redstorgham"), "inventory"));

}
