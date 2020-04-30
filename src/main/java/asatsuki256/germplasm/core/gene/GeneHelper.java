package asatsuki256.germplasm.core.gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.gene.GenePool;
import asatsuki256.germplasm.api.gene.IGeneHelper;
import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest;
import asatsuki256.germplasm.api.gene.unit.IChromosome;
import asatsuki256.germplasm.api.gene.unit.IChromosomePair;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import net.minecraft.util.ResourceLocation;

public class GeneHelper implements IGeneHelper {
	
	Random random = new Random();
	
	public IGenome getOffspring(IGenome original, IGenome pollen) {
		if(original == null || pollen == null) return null;
		if(original.getPairs().size() != pollen.getPairs().size()) return null;
		List<ChromosomePair> pairs = new ArrayList<ChromosomePair>();
		for(int i = 0; i < original.getPairs().size(); i++) {
			Chromosome chromosomeA = (Chromosome) original.getPairs().get(i).getRandomChromosome(random);
			Chromosome chromosomeB = (Chromosome) pollen.getPairs().get(i).getRandomChromosome(random);
			pairs.add(new ChromosomePair(chromosomeA, chromosomeB));
		}
		IGenome offspring = new Genome(pairs);
		offspring.setGeneration(original.getGeneration() + 1);
		
		return offspring;
	}

	public List<TraitTypeHarvest> getHarvestFromUnit(IGermplasmUnitBase unit){
		List<TraitType> traitTypes = unit.getTraitTypes();
		List<TraitTypeHarvest> traitTypeHarvests = new ArrayList<TraitTypeHarvest>();
		for (TraitType traitType : traitTypes){
			if(traitType instanceof TraitTypeHarvest) {
				traitTypeHarvests.add((TraitTypeHarvest) traitType);
			}
		}
		return traitTypeHarvests;
	}
	
	public TraitTypeHarvest getPrimaryHarvest(IGermplasmUnitBase unit) {
		List<TraitTypeHarvest> harvests = getHarvestFromUnit(unit);
		int maxStrength = 0;
		TraitTypeHarvest primaryHarvest = null;
		for(TraitTypeHarvest harvest : harvests) {
			int strength = unit.getTotalStrength(harvest);
			if(strength > maxStrength) {
				primaryHarvest = harvest;
				maxStrength = strength;
			}
		}
		return primaryHarvest;
	}
	
	public IGenome getGenomeFromPool(Random random, GenePool genePool, int pairCount) {
		GenePool[] pools = new GenePool[pairCount];
		for (int i = 0; i < pools.length; i++) {
			pools[i] = genePool;
		}
		return getGenomeFromPool(random, pools);
	}
	
	public IGenome getGenomeFromPool(Random random, GenePool... genePool) {
		IGenome genome;
		List<ChromosomePair> pairs = new ArrayList<ChromosomePair>();
    	for(int i = 0; i < genePool.length; i++) {
    		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        	for(int j = 0; j < 2; j++) {
        		List<Gene> genes = new ArrayList<Gene>();
        		for(int k = 0; k < 3; k++) {
        			if (random != null) {
        				genes.add((Gene) genePool[i].getWeightedGene(random));
        			} else {
        				genes.add((Gene) genePool[i].getWeightedGene());
        			}
        		}
        		chromosomes.add(new Chromosome(genes));
        	}
        	pairs.add(new ChromosomePair(chromosomes));
    	}
    	genome = new Genome(pairs);
    	return genome;
	}
	
	public IGene getRandomGeneFromGenome(IGenome genome, Random random) {
		IChromosomePair pair = genome.getPairs().get(random.nextInt(genome.getPairs().size()));
		IChromosome chromosome = pair.getChromosomes().get(random.nextInt(pair.getChromosomes().size()));
		IGene gene = chromosome.getGenes().get(random.nextInt(chromosome.getGenes().size()));
		return gene;
	}
	
	public ResourceLocation[] getCropTexture(IGermplasmUnitBase unit) {
		TraitTypeHarvest primaryHarvest = getPrimaryHarvest(unit);
		return primaryHarvest != null ? primaryHarvest.getTextures() : TraitTypeHarvest.DEFAULT_TEXTURES;
	}
	
}
