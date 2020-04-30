package asatsuki256.germplasm.api.gene.unit;

import java.util.List;
import java.util.Random;

public interface IChromosomePair extends IGermplasmUnitBase, IGeneInsertable {

	public List<IChromosome> getChromosomes();
	
	public void setChromosomes(List<IChromosome> chromosomes);
	
	public IChromosome getRandomChromosome(Random random);
	
}
