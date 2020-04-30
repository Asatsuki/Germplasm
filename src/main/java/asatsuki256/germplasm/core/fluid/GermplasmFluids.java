package asatsuki256.germplasm.core.fluid;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;
import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import net.minecraft.util.ResourceLocation;

public class GermplasmFluids {
	
	public static FluidGermplasm nucleotide;
	public static FluidGermplasm mutagen;
	public static FluidGermplasm bioethanol;
	public static BlockFluidGermplasm nucleotideBlock;
	public static BlockFluidGermplasm mutagenBlock;
	public static BlockFluidGermplasm bioethanolBlock;
	
	public static void register() {
		nucleotide = (FluidGermplasm) new FluidGermplasm("nucleotide", 
				new ResourceLocation(MODID, "fluids/nucleotide_still"), 
				new ResourceLocation(MODID, "fluids/nucleotide_flow"))
				.setUnlocalizedName(UNLOC_PREFIX + "nucleotide");
		mutagen = (FluidGermplasm) new FluidGermplasm("mutagen", 
				new ResourceLocation(MODID, "fluids/mutagen_still"), 
				new ResourceLocation(MODID, "fluids/mutagen_flow"))
				.setUnlocalizedName(UNLOC_PREFIX + "mutagen");
		bioethanol = (FluidGermplasm) new FluidGermplasm("bioethanol", 
				new ResourceLocation(MODID, "fluids/bioethanol_still"), 
				new ResourceLocation(MODID, "fluids/bioethanol_flow"))
				.setUnlocalizedName(UNLOC_PREFIX + "bioethanol");
		nucleotideBlock = new BlockFluidGermplasm(nucleotide);
		mutagenBlock = new BlockFluidGermplasm(mutagen);
		bioethanolBlock = new BlockFluidGermplasm(bioethanol);
	}
	
	public static void renderFluids() {
    	nucleotideBlock.render();
    	mutagenBlock.render();
    	bioethanolBlock.render();
    }

}
