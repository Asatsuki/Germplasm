package asatsuki256.germplasm.api.gene.unit;

import java.util.List;

public interface IChromosome extends IGermplasmUnitBase, IGeneInsertable {
	
	public List<IGene> getGenes();

	public void setGenes(List<IGene> genes);

}
