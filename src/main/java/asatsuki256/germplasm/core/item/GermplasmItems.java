package asatsuki256.germplasm.core.item;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;
import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GermplasmItems {

	public static Item seed_sample;
	public static Item genome_sample;
	public static Item gene_sample;
	public static Item callus;
	public static Item seed_bag;
	public static Item watering_can;
	public static Item loupe;
	public static Item seed_collector;
	public static Item genome_filter;
	public static Item biomass;
	public static Item bioplastic;
	public static Item silicon;
	public static Item solid_medium;
	public static Item solid_medium_auxin;
	public static Item auxin;
	public static Item basic_processor;
	public static Item advanced_processor;
	public static Item heating_unit;
	public static Item motor_unit;
	public static Item thermal_unit;
	public static Item capacitor_unit;
	
	public static Item tomato;
	public static Item iron_potato;
	public static Item claylily;
	public static Item redstorgham;
	
	public static void register() {
		seed_sample = new ItemSeedSample().setUnlocalizedName(UNLOC_PREFIX + "seed_sample").setRegistryName(new ResourceLocation(MODID, "seed_sample"));
		genome_sample = new ItemGenomeSample().setUnlocalizedName(UNLOC_PREFIX + "genome_sample").setRegistryName(new ResourceLocation(MODID, "genome_sample"));
		gene_sample = new ItemGeneSample().setUnlocalizedName(UNLOC_PREFIX + "gene_sample").setRegistryName(new ResourceLocation(MODID, "gene_sample"));
		callus = new ItemCallus().setUnlocalizedName(UNLOC_PREFIX + "callus").setRegistryName(new ResourceLocation(MODID, "callus"));
		seed_bag = new ItemSeedBag().setUnlocalizedName(UNLOC_PREFIX + "seed_bag").setRegistryName(new ResourceLocation(MODID, "seed_bag"));
		watering_can = new ItemWateringCan().setUnlocalizedName(UNLOC_PREFIX + "watering_can").setRegistryName(new ResourceLocation(MODID, "watering_can"));
		loupe = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "loupe").setRegistryName(new ResourceLocation(MODID, "loupe"));
		seed_collector = new ItemBase(UNLOC_PREFIX + "seed_collector.desc").setUnlocalizedName(UNLOC_PREFIX + "seed_collector").setRegistryName(new ResourceLocation(MODID, "seed_collector"));
		genome_filter = new ItemFilterGenome().setUnlocalizedName(UNLOC_PREFIX + "genome_filter").setRegistryName(new ResourceLocation(MODID, "genome_filter"));
		biomass = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "biomass").setRegistryName(new ResourceLocation(MODID, "biomass"));
		bioplastic = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "bioplastic").setRegistryName(new ResourceLocation(MODID, "bioplastic"));
		silicon = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "silicon").setRegistryName(new ResourceLocation(MODID, "silicon"));
		solid_medium = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "solid_medium").setRegistryName(new ResourceLocation(MODID, "solid_medium"));
		solid_medium_auxin = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "solid_medium_auxin").setRegistryName(new ResourceLocation(MODID, "solid_medium_auxin"));
		auxin = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "auxin").setRegistryName(new ResourceLocation(MODID, "auxin"));
		basic_processor = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "basic_processor").setRegistryName(new ResourceLocation(MODID, "basic_processor"));
		advanced_processor = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "advanced_processor").setRegistryName(new ResourceLocation(MODID, "advanced_processor"));
		heating_unit = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "heating_unit").setRegistryName(new ResourceLocation(MODID, "heating_unit"));
		motor_unit = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "motor_unit").setRegistryName(new ResourceLocation(MODID, "motor_unit"));
		thermal_unit = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "thermal_unit").setRegistryName(new ResourceLocation(MODID, "thermal_unit"));
		capacitor_unit = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "capacitor_unit").setRegistryName(new ResourceLocation(MODID, "capacitor_unit"));
		
		tomato = new ItemGmpmFood(4, 0.6f, false).setUnlocalizedName(UNLOC_PREFIX + "tomato").setRegistryName(new ResourceLocation(MODID, "tomato"));
		iron_potato = new ItemGmpmFood(2, 0.1f, false).setUnlocalizedName(UNLOC_PREFIX + "iron_potato").setRegistryName(new ResourceLocation(MODID, "iron_potato"));
		claylily = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "claylily").setRegistryName(new ResourceLocation(MODID, "claylily"));
		redstorgham = new ItemBase().setUnlocalizedName(UNLOC_PREFIX + "redstorgham").setRegistryName(new ResourceLocation(MODID, "redstorgham"));
		
		ForgeRegistries.ITEMS.register(GermplasmItems.seed_sample);
		ForgeRegistries.ITEMS.register(GermplasmItems.genome_sample);
		ForgeRegistries.ITEMS.register(GermplasmItems.gene_sample);
		ForgeRegistries.ITEMS.register(GermplasmItems.callus);
		ForgeRegistries.ITEMS.register(GermplasmItems.seed_bag);
		ForgeRegistries.ITEMS.register(GermplasmItems.watering_can);
		ForgeRegistries.ITEMS.register(GermplasmItems.loupe);
		ForgeRegistries.ITEMS.register(GermplasmItems.seed_collector);
		ForgeRegistries.ITEMS.register(GermplasmItems.genome_filter);
		ForgeRegistries.ITEMS.register(GermplasmItems.biomass);
		ForgeRegistries.ITEMS.register(GermplasmItems.bioplastic);
		ForgeRegistries.ITEMS.register(GermplasmItems.silicon);
		ForgeRegistries.ITEMS.register(GermplasmItems.solid_medium);
		ForgeRegistries.ITEMS.register(GermplasmItems.solid_medium_auxin);
		ForgeRegistries.ITEMS.register(GermplasmItems.auxin);
		ForgeRegistries.ITEMS.register(GermplasmItems.basic_processor);
		ForgeRegistries.ITEMS.register(GermplasmItems.advanced_processor);
		ForgeRegistries.ITEMS.register(GermplasmItems.heating_unit);
		ForgeRegistries.ITEMS.register(GermplasmItems.motor_unit);
		ForgeRegistries.ITEMS.register(GermplasmItems.thermal_unit);
		ForgeRegistries.ITEMS.register(GermplasmItems.capacitor_unit);
		
    	ForgeRegistries.ITEMS.register(GermplasmItems.tomato);
    	ForgeRegistries.ITEMS.register(GermplasmItems.iron_potato);
    	ForgeRegistries.ITEMS.register(GermplasmItems.claylily);
    	ForgeRegistries.ITEMS.register(GermplasmItems.redstorgham);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(seed_sample, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":seed_sample", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(genome_sample, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":genome_sample", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(gene_sample, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":gene_sample", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(callus, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":callus", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(seed_bag, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":seed_bag", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(watering_can, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":watering_can", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(loupe, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":loupe", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(seed_collector, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":seed_collector", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(genome_filter, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":genome_filter", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(biomass, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":biomass", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(bioplastic, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":bioplastic", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(silicon, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":silicon", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(solid_medium, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":solid_medium", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(solid_medium_auxin, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":solid_medium_auxin", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(auxin, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":auxin", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(basic_processor, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":basic_processor", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(advanced_processor, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":advanced_processor", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(heating_unit, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":heating_unit", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(motor_unit, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":motor_unit", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(thermal_unit, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":thermal_unit", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(capacitor_unit, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":capacitor_unit", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(tomato, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":tomato", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(iron_potato, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":iron_potato", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(claylily, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":claylily", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(redstorgham, 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":redstorgham", "inventory"));
		
		//ModelBakery.registerItemVariants(GermplasmItems.seed_sample, new ModelResourceLocation(new ResourceLocation(MODID, "seeds/seed_sample_plate"), "inventory"));
	}
	
}
