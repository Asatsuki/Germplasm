package asatsuki256.germplasm.api.gene.unit;

import java.util.List;

public interface IGenome extends IGermplasmUnitBase, IGeneInsertable {
	
	public List<IChromosomePair> getPairs();
	
	public void setPairs(List<IChromosomePair> pairs);
	
	public void setGeneration(int generation);
	
	public int getGeneration();
	
	public void resetGeneration();

}
