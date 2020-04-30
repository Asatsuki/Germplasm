package asatsuki256.germplasm.core.block;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;
import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import asatsuki256.germplasm.core.item.ItemBlockDesc;
import asatsuki256.germplasm.core.item.ItemBlockReagentBottle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GermplasmBlocks {
	
	public static Block genetic_crop;
	
	public static Block reagent_bottle;
	public static Block thresher;
	public static Block microscope;
	public static Block research_desk;
	public static Block solid_fuel_generator;
	public static Block fuel_cell;
	public static Block genome_analyzer;
	public static Block sample_sorter;
	public static Block reactor;
	public static Block incubator;
	public static Block dna_extractor;
	public static Block pcr;
	public static Block gene_isolator;
	public static Block electroporator;
	
	public static Block desk;
	public static Block base_casing;
	public static Block agriculture_casing;
	public static Block biotechnology_casing;
	public static Block genetics_casing;
	public static Block bioplastic_block;
	public static Block bioplastic_tile;
	public static Block bioplastic_tile_small;
	public static Block organic_glass;
	public static Block plastic_wall_light;
	
	public static Block debug_block;
	
	public static void register() {
		genetic_crop = new BlockGeneticCrop().setUnlocalizedName(UNLOC_PREFIX + "genetic_crop").setRegistryName(
				new ResourceLocation(MODID, "genetic_crop"));
		thresher = new BlockThresher().setUnlocalizedName(UNLOC_PREFIX + "thresher").setRegistryName(
				new ResourceLocation(MODID, "thresher"));
		microscope = new BlockMicroscope().setUnlocalizedName(UNLOC_PREFIX + "microscope").setRegistryName(
				new ResourceLocation(MODID, "microscope"));
		research_desk = new BlockResearchDesk().setUnlocalizedName(UNLOC_PREFIX + "research_desk").setRegistryName(
				new ResourceLocation(MODID, "research_desk"));
		genome_analyzer = new BlockGenomeAnalyzer().setUnlocalizedName(UNLOC_PREFIX + "genome_analyzer").setRegistryName(
				new ResourceLocation(MODID, "genome_analyzer"));
		desk = new BlockDecorative(Material.IRON).setUnlocalizedName(UNLOC_PREFIX + "desk").setRegistryName(
				new ResourceLocation(MODID, "desk"));
		reagent_bottle = new BlockReagentBottle().setUnlocalizedName(UNLOC_PREFIX + "reagent_bottle").setRegistryName(
				new ResourceLocation(MODID, "reagent_bottle"));
		reactor = new BlockReactor().setUnlocalizedName(UNLOC_PREFIX + "reactor").setRegistryName(
				new ResourceLocation(MODID, "reactor"));
		dna_extractor = new BlockDnaExtractor().setUnlocalizedName(UNLOC_PREFIX + "dna_extractor").setRegistryName(
				new ResourceLocation(MODID, "dna_extractor"));
		pcr = new BlockPCR().setUnlocalizedName(UNLOC_PREFIX + "pcr").setRegistryName(
				new ResourceLocation(MODID, "pcr"));
		incubator = new BlockIncubator().setUnlocalizedName(UNLOC_PREFIX + "incubator").setRegistryName(
				new ResourceLocation(MODID, "incubator"));
		electroporator = new BlockElectroporator().setUnlocalizedName(UNLOC_PREFIX + "electroporator").setRegistryName(
				new ResourceLocation(MODID, "electroporator"));
		gene_isolator = new BlockGeneIsolator().setUnlocalizedName(UNLOC_PREFIX + "gene_isolator").setRegistryName(
				new ResourceLocation(MODID, "gene_isolator"));
		solid_fuel_generator = new BlockSolidFuelGenerator().setUnlocalizedName(UNLOC_PREFIX + "solid_fuel_generator").setRegistryName(
				new ResourceLocation(MODID, "solid_fuel_generator"));
		fuel_cell = new BlockFuelCell().setUnlocalizedName(UNLOC_PREFIX + "fuel_cell").setRegistryName(
				new ResourceLocation(MODID, "fuel_cell"));
		sample_sorter = new BlockSampleSorter().setUnlocalizedName(UNLOC_PREFIX + "sample_sorter").setRegistryName(
				new ResourceLocation(MODID, "sample_sorter"));
		
		bioplastic_block = new BlockBioplastic().setUnlocalizedName(UNLOC_PREFIX + "bioplastic_block").setRegistryName(
				new ResourceLocation(MODID, "bioplastic_block"));
		base_casing = new BlockCasing().setUnlocalizedName(UNLOC_PREFIX + "base_casing").setRegistryName(
				new ResourceLocation(MODID, "base_casing"));
		agriculture_casing = new BlockCasing().setUnlocalizedName(UNLOC_PREFIX + "agriculture_casing").setRegistryName(
				new ResourceLocation(MODID, "agriculture_casing"));
		biotechnology_casing = new BlockCasing().setUnlocalizedName(UNLOC_PREFIX + "biotechnology_casing").setRegistryName(
				new ResourceLocation(MODID, "biotechnology_casing"));
		genetics_casing = new BlockCasing().setUnlocalizedName(UNLOC_PREFIX + "genetics_casing").setRegistryName(
				new ResourceLocation(MODID, "genetics_casing"));
		bioplastic_tile = new BlockBioplastic().setUnlocalizedName(UNLOC_PREFIX + "bioplastic_tile").setRegistryName(
				new ResourceLocation(MODID, "bioplastic_tile"));
		bioplastic_tile_small = new BlockBioplastic().setUnlocalizedName(UNLOC_PREFIX + "bioplastic_tile_small").setRegistryName(
				new ResourceLocation(MODID, "bioplastic_tile_small"));
		organic_glass = new BlockBioplastic().setRenderLayer(BlockRenderLayer.CUTOUT)
				.setLightOpacity(0)
				.setUnlocalizedName(UNLOC_PREFIX + "organic_glass").setRegistryName(
				new ResourceLocation(MODID, "organic_glass"));
		plastic_wall_light = new BlockWallLight().setUnlocalizedName(UNLOC_PREFIX + "plastic_wall_light").setRegistryName(
				new ResourceLocation(MODID, "plastic_wall_light"));
		
		debug_block = new BlockDebug().setUnlocalizedName(UNLOC_PREFIX + "debug_block").setRegistryName(
				new ResourceLocation(MODID, "debug_block"));
		ForgeRegistries.BLOCKS.register(genetic_crop);
    	ForgeRegistries.ITEMS.register(new ItemBlock(genetic_crop).setRegistryName(genetic_crop.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(thresher);
    	ForgeRegistries.ITEMS.register(new ItemBlock(thresher).setRegistryName(thresher.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(microscope);
    	ForgeRegistries.ITEMS.register(new ItemBlock(microscope).setRegistryName(microscope.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(desk);
    	ForgeRegistries.ITEMS.register(new ItemBlock(desk).setRegistryName(desk.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(research_desk);
    	ForgeRegistries.ITEMS.register(new ItemBlock(research_desk).setRegistryName(research_desk.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(reagent_bottle);
    	ForgeRegistries.ITEMS.register(new ItemBlockReagentBottle(reagent_bottle).setRegistryName(reagent_bottle.getRegistryName()));
    	
    	//electric
    	ForgeRegistries.BLOCKS.register(solid_fuel_generator);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(solid_fuel_generator).setRegistryName(solid_fuel_generator.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(fuel_cell);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(fuel_cell).setRegistryName(fuel_cell.getRegistryName()));
    	//agriculture
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(genome_analyzer).setRegistryName(genome_analyzer.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(genome_analyzer);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(sample_sorter).setRegistryName(sample_sorter.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(sample_sorter);
    	//biotechnology
    	ForgeRegistries.BLOCKS.register(reactor);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(reactor).setRegistryName(reactor.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(incubator);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(incubator).setRegistryName(incubator.getRegistryName()));
    	//genetics
    	ForgeRegistries.BLOCKS.register(dna_extractor);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(dna_extractor).setRegistryName(dna_extractor.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(pcr);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(pcr).setRegistryName(pcr.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(gene_isolator);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(gene_isolator).setRegistryName(gene_isolator.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(electroporator);
    	ForgeRegistries.ITEMS.register(new ItemBlockDesc(electroporator).setRegistryName(electroporator.getRegistryName()));
    	
    	ForgeRegistries.BLOCKS.register(base_casing);
    	ForgeRegistries.ITEMS.register(new ItemBlock(base_casing).setRegistryName(base_casing.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(agriculture_casing);
    	ForgeRegistries.ITEMS.register(new ItemBlock(agriculture_casing).setRegistryName(agriculture_casing.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(biotechnology_casing);
    	ForgeRegistries.ITEMS.register(new ItemBlock(biotechnology_casing).setRegistryName(biotechnology_casing.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(genetics_casing);
    	ForgeRegistries.ITEMS.register(new ItemBlock(genetics_casing).setRegistryName(genetics_casing.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(bioplastic_block);
    	ForgeRegistries.ITEMS.register(new ItemBlock(bioplastic_block).setRegistryName(bioplastic_block.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(bioplastic_tile);
    	ForgeRegistries.ITEMS.register(new ItemBlock(bioplastic_tile).setRegistryName(bioplastic_tile.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(bioplastic_tile_small);
    	ForgeRegistries.ITEMS.register(new ItemBlock(bioplastic_tile_small).setRegistryName(bioplastic_tile_small.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(organic_glass);
    	ForgeRegistries.ITEMS.register(new ItemBlock(organic_glass).setRegistryName(organic_glass.getRegistryName()));
    	ForgeRegistries.BLOCKS.register(plastic_wall_light);
    	ForgeRegistries.ITEMS.register(new ItemBlock(plastic_wall_light).setRegistryName(plastic_wall_light.getRegistryName()));
    	
    	ForgeRegistries.BLOCKS.register(debug_block);
    	ForgeRegistries.ITEMS.register(new ItemBlock(debug_block).setRegistryName(debug_block.getRegistryName()));
    	
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(genetic_crop), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":genetic_crop", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(thresher), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":thresher", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(microscope), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":microscope", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(research_desk), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":research_desk", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(genome_analyzer), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":genome_analyzer", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(desk), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":desk", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(reagent_bottle), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":reagent_bottle", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(reactor), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":reactor", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dna_extractor), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":dna_extractor", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(pcr), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":pcr", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(incubator), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":incubator", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(electroporator), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":electroporator", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(gene_isolator), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":gene_isolator", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(solid_fuel_generator), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":solid_fuel_generator", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fuel_cell), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":fuel_cell", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(sample_sorter), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":sample_sorter", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(base_casing), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":base_casing", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(agriculture_casing), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":agriculture_casing", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(biotechnology_casing), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":biotechnology_casing", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(genetics_casing), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":genetics_casing", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(bioplastic_block), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":bioplastic_block", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(bioplastic_tile), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":bioplastic_tile", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(bioplastic_tile_small), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":bioplastic_tile_small", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(organic_glass), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":organic_glass", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(plastic_wall_light), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":plastic_wall_light", "inventory"));
		net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(debug_block), 0,
				new net.minecraft.client.renderer.block.model.ModelResourceLocation(MODID + ":debug_block", "inventory"));
		
	}

}
