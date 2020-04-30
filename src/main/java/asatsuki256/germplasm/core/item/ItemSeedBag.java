package asatsuki256.germplasm.core.item;

import java.util.ArrayList;
import java.util.List;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.core.gene.Chromosome;
import asatsuki256.germplasm.core.gene.ChromosomePair;
import asatsuki256.germplasm.core.gene.Gene;
import asatsuki256.germplasm.core.gene.Genome;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSeedBag extends ItemBase {
	
	public ItemSeedBag() {
		super(false);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		
		ItemStack seedStack = new ItemStack(GermplasmItems.seed_sample);
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		IGenome genome;
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
    	genome = new Genome(pairs);
		
		GeneAPI.nbtHelper.setGenomeToIndividualNBT(nbt, genome);
		
		seedStack.setTagCompound(nbt);
		
		player.addItemStackToInventory(seedStack);
		
		stack.shrink(1);
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

}
