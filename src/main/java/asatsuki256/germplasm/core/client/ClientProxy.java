package asatsuki256.germplasm.core.client;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import asatsuki256.germplasm.core.CommonProxy;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import asatsuki256.germplasm.core.client.model.ReagentBottleModelLoader;
import asatsuki256.germplasm.core.client.model.SeedModelLoader;
import asatsuki256.germplasm.core.client.tileentity.TileFuelCellRenderer;
import asatsuki256.germplasm.core.client.tileentity.TileGeneticCropRenderer;
import asatsuki256.germplasm.core.client.tileentity.TileGenomeAnalyzerRenderer;
import asatsuki256.germplasm.core.client.tileentity.TileMicroscopeRenderer;
import asatsuki256.germplasm.core.client.tileentity.TileReagentBottleRenderer;
import asatsuki256.germplasm.core.fluid.GermplasmFluids;
import asatsuki256.germplasm.core.gene.trait.TraitTypeRegistry;
import asatsuki256.germplasm.core.item.GermplasmItems;
import asatsuki256.germplasm.core.tileentity.TileDnaExtractor;
import asatsuki256.germplasm.core.tileentity.TileElectroporator;
import asatsuki256.germplasm.core.tileentity.TileFuelCell;
import asatsuki256.germplasm.core.tileentity.TileGeneIsolator;
import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import asatsuki256.germplasm.core.tileentity.TileGenomeAnalyzer;
import asatsuki256.germplasm.core.tileentity.TileIncubator;
import asatsuki256.germplasm.core.tileentity.TileMicroscope;
import asatsuki256.germplasm.core.tileentity.TilePCR;
import asatsuki256.germplasm.core.tileentity.TileReactor;
import asatsuki256.germplasm.core.tileentity.TileReagentBottle;
import asatsuki256.germplasm.core.tileentity.TileResearchDesk;
import asatsuki256.germplasm.core.tileentity.TileSampleSorter;
import asatsuki256.germplasm.core.tileentity.TileSolidFuelGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy{

	@Override
	public void registerPreInit() {
		GermplasmItems.register();
		GermplasmItems.registerModels();
		GermplasmBlocks.register();
		GermplasmBlocks.registerModels();
		GermplasmFluids.register();
		GermplasmFluids.renderFluids();
		
		TraitTypeRegistry.registerModels();
		ModelLoaderRegistry.registerLoader(new SeedModelLoader());
    	ModelLoaderRegistry.registerLoader(new ReagentBottleModelLoader());
    	ModelBakery.registerItemVariants(GermplasmItems.seed_sample, new ModelResourceLocation(new ResourceLocation(MODID, "seed/seed_sample_plate"), "inventory"));
	}
	
	@Override
	public void registerInit() {
		MinecraftForge.EVENT_BUS.register(new GermplasmClientEventHooks());
		GameRegistry.registerTileEntity(TileGeneticCrop.class, new ResourceLocation(GermplasmCore.MODID, "genetic_crop"));
		GameRegistry.registerTileEntity(TileMicroscope.class, new ResourceLocation(GermplasmCore.MODID, "microscope"));
		GameRegistry.registerTileEntity(TileResearchDesk.class, new ResourceLocation(GermplasmCore.MODID, "research_desk"));
		GameRegistry.registerTileEntity(TileGenomeAnalyzer.class, new ResourceLocation(GermplasmCore.MODID, "genome_analyzer"));
		GameRegistry.registerTileEntity(TileReagentBottle.class, new ResourceLocation(GermplasmCore.MODID, "reagent_bottle"));
		GameRegistry.registerTileEntity(TileReactor.class, new ResourceLocation(GermplasmCore.MODID, "reactor"));
		GameRegistry.registerTileEntity(TileDnaExtractor.class, new ResourceLocation(GermplasmCore.MODID, "dna_extractor"));
		GameRegistry.registerTileEntity(TilePCR.class, new ResourceLocation(GermplasmCore.MODID, "pcr"));
		GameRegistry.registerTileEntity(TileIncubator.class, new ResourceLocation(GermplasmCore.MODID, "incubator"));
		GameRegistry.registerTileEntity(TileElectroporator.class, new ResourceLocation(GermplasmCore.MODID, "electroporator"));
		GameRegistry.registerTileEntity(TileGeneIsolator.class, new ResourceLocation(GermplasmCore.MODID, "gene_isolator"));
		GameRegistry.registerTileEntity(TileSolidFuelGenerator.class, new ResourceLocation(GermplasmCore.MODID, "solid_fuel_generator"));
		GameRegistry.registerTileEntity(TileFuelCell.class, new ResourceLocation(GermplasmCore.MODID, "fuel_cell"));
		GameRegistry.registerTileEntity(TileSampleSorter.class, new ResourceLocation(GermplasmCore.MODID, "sample_sorter"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileMicroscope.class, new TileMicroscopeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileGenomeAnalyzer.class, new TileGenomeAnalyzerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileGeneticCrop.class, new TileGeneticCropRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReagentBottle.class, new TileReagentBottleRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFuelCell.class, new TileFuelCellRenderer());
	}
	
	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
	
}
