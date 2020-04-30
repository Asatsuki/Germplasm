package asatsuki256.germplasm.core;

import asatsuki256.germplasm.core.item.ItemFilterGenome;
import asatsuki256.germplasm.core.machine.ContainerDnaExtractor;
import asatsuki256.germplasm.core.machine.ContainerElectroporator;
import asatsuki256.germplasm.core.machine.ContainerFuelCell;
import asatsuki256.germplasm.core.machine.ContainerGeneIsolator;
import asatsuki256.germplasm.core.machine.ContainerGenomeAnalyzer;
import asatsuki256.germplasm.core.machine.ContainerGenomeFilter;
import asatsuki256.germplasm.core.machine.ContainerIncubator;
import asatsuki256.germplasm.core.machine.ContainerMicroscope;
import asatsuki256.germplasm.core.machine.ContainerPCR;
import asatsuki256.germplasm.core.machine.ContainerReactor;
import asatsuki256.germplasm.core.machine.ContainerResearchDesk;
import asatsuki256.germplasm.core.machine.ContainerSampleSorter;
import asatsuki256.germplasm.core.machine.ContainerSolidFuelGenerator;
import asatsuki256.germplasm.core.machine.GuiDnaExtractor;
import asatsuki256.germplasm.core.machine.GuiElectroporator;
import asatsuki256.germplasm.core.machine.GuiFuelCell;
import asatsuki256.germplasm.core.machine.GuiGeneIsolator;
import asatsuki256.germplasm.core.machine.GuiGenomeAnalyzer;
import asatsuki256.germplasm.core.machine.GuiGenomeFilter;
import asatsuki256.germplasm.core.machine.GuiIncubator;
import asatsuki256.germplasm.core.machine.GuiMicroscope;
import asatsuki256.germplasm.core.machine.GuiPCR;
import asatsuki256.germplasm.core.machine.GuiReactor;
import asatsuki256.germplasm.core.machine.GuiResearchDesk;
import asatsuki256.germplasm.core.machine.GuiSampleSorter;
import asatsuki256.germplasm.core.machine.GuiSolidFuelGenerator;
import asatsuki256.germplasm.core.tileentity.TileDnaExtractor;
import asatsuki256.germplasm.core.tileentity.TileElectroporator;
import asatsuki256.germplasm.core.tileentity.TileFuelCell;
import asatsuki256.germplasm.core.tileentity.TileGeneIsolator;
import asatsuki256.germplasm.core.tileentity.TileGenomeAnalyzer;
import asatsuki256.germplasm.core.tileentity.TileIncubator;
import asatsuki256.germplasm.core.tileentity.TileMicroscope;
import asatsuki256.germplasm.core.tileentity.TilePCR;
import asatsuki256.germplasm.core.tileentity.TileReactor;
import asatsuki256.germplasm.core.tileentity.TileResearchDesk;
import asatsuki256.germplasm.core.tileentity.TileSampleSorter;
import asatsuki256.germplasm.core.tileentity.TileSolidFuelGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{
	
	public static final int GUI_ID_MICROSCOPE = 0;
	public static final int GUI_ID_RESEARCH_DESK = 1;
	public static final int GUI_ID_ANALYZER = 2;
	public static final int GUI_ID_REACTOR = 3;
	public static final int GUI_ID_DNA_EXTRACTOR = 4;
	public static final int GUI_ID_PCR = 5;
	public static final int GUI_ID_INCUBATOR = 6;
	public static final int GUI_ID_ELECTROPORATOR = 7;
	public static final int GUI_ID_GENE_ISOLATOR = 8;
	public static final int GUI_ID_SOLID_FUEL_GENERATOR = 9;
	public static final int GUI_ID_FUEL_CELL = 10;
	public static final int GUI_ID_SAMPLE_SORTER = 11;
	public static final int GUI_ID_GENOME_FILTER = 12;
	
	@Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null) {
			if (ID == GUI_ID_MICROSCOPE && tile instanceof TileMicroscope) {
	            return new ContainerMicroscope(x, y, z, player.inventory, (TileMicroscope) tile);
	        }else if (ID == GUI_ID_RESEARCH_DESK && tile instanceof TileResearchDesk) {
	            return new ContainerResearchDesk(x, y, z, player.inventory, (TileResearchDesk) tile);
	        }else if (ID == GUI_ID_ANALYZER && tile instanceof TileGenomeAnalyzer) {
	            return new ContainerGenomeAnalyzer(x, y, z, player.inventory, (TileGenomeAnalyzer) tile);
	        }else if (ID == GUI_ID_REACTOR && tile instanceof TileReactor) {
	            return new ContainerReactor(x, y, z, player.inventory, (TileReactor) tile);
	        }else if (ID == GUI_ID_DNA_EXTRACTOR && tile instanceof TileDnaExtractor) {
	            return new ContainerDnaExtractor(x, y, z, player.inventory, (TileDnaExtractor) tile);
	        }else if (ID == GUI_ID_PCR && tile instanceof TilePCR) {
	            return new ContainerPCR(x, y, z, player.inventory, (TilePCR) tile);
	        }else if (ID == GUI_ID_INCUBATOR && tile instanceof TileIncubator) {
	            return new ContainerIncubator(x, y, z, player.inventory, (TileIncubator) tile);
	        }else if (ID == GUI_ID_ELECTROPORATOR && tile instanceof TileElectroporator) {
	            return new ContainerElectroporator(x, y, z, player.inventory, (TileElectroporator) tile);
	        }else if (ID == GUI_ID_GENE_ISOLATOR && tile instanceof TileGeneIsolator) {
	            return new ContainerGeneIsolator(x, y, z, player.inventory, (TileGeneIsolator) tile);
	        }else if (ID == GUI_ID_SOLID_FUEL_GENERATOR && tile instanceof TileSolidFuelGenerator) {
	            return new ContainerSolidFuelGenerator(x, y, z, player.inventory, (TileSolidFuelGenerator) tile);
	        }else if (ID == GUI_ID_FUEL_CELL && tile instanceof TileFuelCell) {
	            return new ContainerFuelCell(x, y, z, player.inventory, (TileFuelCell) tile);
	        }else if (ID == GUI_ID_SAMPLE_SORTER && tile instanceof TileSampleSorter) {
	            return new ContainerSampleSorter(x, y, z, player.inventory, (TileSampleSorter) tile);
	        }
		}
		//System.out.println(player.getHeldItem(EnumHand.MAIN_HAND));
		if (ID == GUI_ID_GENOME_FILTER && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFilterGenome) {
			ItemStack item = player.getHeldItem(EnumHand.MAIN_HAND);
			return new ContainerGenomeFilter(x, y, z, player.inventory, item);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
    	if(tile != null) {
    		if (ID == GUI_ID_MICROSCOPE && tile instanceof TileMicroscope) {
	            return new GuiMicroscope(x, y, z, player.inventory, (TileMicroscope) tile);
	        }else if (ID == GUI_ID_RESEARCH_DESK && tile instanceof TileResearchDesk) {
                return new GuiResearchDesk(x, y, z, player.inventory, (TileResearchDesk) tile);
            }else if (ID == GUI_ID_ANALYZER && tile instanceof TileGenomeAnalyzer) {
                return new GuiGenomeAnalyzer(x, y, z, player.inventory, (TileGenomeAnalyzer) tile);
            }else if (ID == GUI_ID_REACTOR && tile instanceof TileReactor) {
                return new GuiReactor(x, y, z, player.inventory, (TileReactor) tile);
            }else if (ID == GUI_ID_DNA_EXTRACTOR && tile instanceof TileDnaExtractor) {
	            return new GuiDnaExtractor(x, y, z, player.inventory, (TileDnaExtractor) tile);
	        }else if (ID == GUI_ID_PCR && tile instanceof TilePCR) {
	            return new GuiPCR(x, y, z, player.inventory, (TilePCR) tile);
	        }else if (ID == GUI_ID_INCUBATOR && tile instanceof TileIncubator) {
	            return new GuiIncubator(x, y, z, player.inventory, (TileIncubator) tile);
	        }else if (ID == GUI_ID_ELECTROPORATOR && tile instanceof TileElectroporator) {
	            return new GuiElectroporator(x, y, z, player.inventory, (TileElectroporator) tile);
	        }else if (ID == GUI_ID_GENE_ISOLATOR && tile instanceof TileGeneIsolator) {
	            return new GuiGeneIsolator(x, y, z, player.inventory, (TileGeneIsolator) tile);
	        }else if (ID == GUI_ID_SOLID_FUEL_GENERATOR && tile instanceof TileSolidFuelGenerator) {
	            return new GuiSolidFuelGenerator(x, y, z, player.inventory, (TileSolidFuelGenerator) tile);
	        }else if (ID == GUI_ID_FUEL_CELL && tile instanceof TileFuelCell) {
	            return new GuiFuelCell(x, y, z, player.inventory, (TileFuelCell) tile);
	        }else if (ID == GUI_ID_SAMPLE_SORTER && tile instanceof TileSampleSorter) {
	            return new GuiSampleSorter(x, y, z, player.inventory, (TileSampleSorter) tile);
	        }
    	}
    	if (ID == GUI_ID_GENOME_FILTER && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFilterGenome) {
			ItemStack item = player.getHeldItem(EnumHand.MAIN_HAND);
			return new GuiGenomeFilter(x, y, z, player.inventory, item);
        }
        return null;
    }

}
