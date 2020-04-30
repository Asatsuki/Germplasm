package asatsuki256.germplasm.core.gene.filter;

import asatsuki256.germplasm.api.gene.filter.IGeneticFilter;
import asatsuki256.germplasm.api.gene.unit.IChromosome;
import asatsuki256.germplasm.api.gene.unit.IChromosomePair;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;

/*
 * Genomeの特定位置にGeneがあれば通す
 * 位置が0なら問わない
 */
public class FilterGenome implements IGeneticFilter {
	
	public FilterGenome(IGene gene, int pairNum, int chromosomeNum, int geneNum) {
		this.gene = gene;
		this.pairNum = pairNum;
		this.chromosomeNum = chromosomeNum;
		this.geneNum = geneNum;
	}
	
	public IGene gene;
	public int pairNum, chromosomeNum, geneNum;

	@Override
	public boolean isMatched(IGermplasmUnitBase unit) {
		if (!(unit instanceof IGenome)) return false;
		if (!(gene != null)) return false;
		IGenome genome = (IGenome) unit;
		if (pairNum < 0 || genome.getPairs().size() < pairNum) return false;
		for (int i = 0; i < genome.getPairs().size(); i++) {
			if (pairNum != 0 && pairNum - 1 != i) continue;
			IChromosomePair pair = genome.getPairs().get(i);
			if (chromosomeNum < 0 || pair.getChromosomes().size() < chromosomeNum) continue;
			for (int j = 0; j < pair.getChromosomes().size(); j++) {
				if (chromosomeNum != 0 && chromosomeNum - 1 != j) continue;
				IChromosome chromosome = pair.getChromosomes().get(j);
				if (geneNum < 0 || chromosome.getGenes().size() < geneNum) continue;
				for (int k = 0; k < chromosome.getGenes().size(); k++) {
					if (geneNum != 0 && geneNum - 1 != k) continue;
					IGene gene = chromosome.getGenes().get(k);
					if(gene.equals(getGene())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private IGene getGene() {
		return gene;
	}

}
