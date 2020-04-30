package asatsuki256.germplasm.core;

import asatsuki256.germplasm.core.block.GermplasmBlocks;
import asatsuki256.germplasm.core.fluid.GermplasmFluids;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerPreInit() {
		GermplasmItems.register();
		GermplasmBlocks.register();
		GermplasmFluids.register();
	}
	
	public void registerInit() {
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
	}
	
	public EntityPlayer getClientPlayer() {return null;}
	
	
}
