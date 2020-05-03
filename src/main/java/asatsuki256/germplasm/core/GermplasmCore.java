package asatsuki256.germplasm.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.recipe.RecipeAPI;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import asatsuki256.germplasm.core.client.model.ReagentBottleModelLoader;
import asatsuki256.germplasm.core.client.model.SeedModelLoader;
import asatsuki256.germplasm.core.compat.CompatHandler;
import asatsuki256.germplasm.core.gene.Chromosome;
import asatsuki256.germplasm.core.gene.ChromosomePair;
import asatsuki256.germplasm.core.gene.Gene;
import asatsuki256.germplasm.core.gene.Genome;
import asatsuki256.germplasm.core.network.GermplasmPacketHandler;
import asatsuki256.germplasm.core.recipe.GmpmRecipeRegistry;
import asatsuki256.germplasm.core.village.GermplasmVillage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = GermplasmCore.MODID, name = GermplasmCore.NAME, version = GermplasmCore.VERSION)
public class GermplasmCore {
	
	@Instance("germplasm")
	public static GermplasmCore instance;
	
	public static final String MODID = "germplasm";
	public static final String UNLOC_PREFIX = "germplasm.";
	public static final String NBT_PREFIX = "germplasm.";
    public static final String NAME = "Germplasm";
    public static final String VERSION = "0.1.2";
    
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final CreativeTabs tabAgrigenetics = new CreativeTabGermplasm(MODID);
	
    public static Genome sampleGenome;
    
    @SidedProxy(clientSide = "asatsuki256.germplasm.core.client.ClientProxy", serverSide = "asatsuki256.germplasm.core.CommonProxy")
	public static CommonProxy proxy;
    
    static {FluidRegistry.enableUniversalBucket();}
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.registerPreInit();
    	APILoader.loadAPI();
    	List<ChromosomePair> pairs = new ArrayList<ChromosomePair>();
    	for(int i = 0; i < 4; i++) {
    		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        	for(int j = 0; j < 2; j++) {
        		List<Gene> genes = new ArrayList<Gene>();
        		for(int k = 0; k < 3; k++) {
        			genes.add((Gene) GeneAPI.genePoolRegistry.pool("general").getWeightedGene());
        		}
        		chromosomes.add(new Chromosome(genes));
        	}
        	pairs.add(new ChromosomePair(chromosomes));
    	}
    	sampleGenome = new Genome(pairs);
    	
    	MinecraftForge.EVENT_BUS.register(GermplasmBlocks.class);
    	MinecraftForge.EVENT_BUS.register(GermplasmVillage.class);
    	GermplasmPacketHandler.init();
    	CompatHandler.preInit();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerInit();
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	((GmpmRecipeRegistry) RecipeAPI.recipeRegistry).registerAll();
    }
    
    
}
