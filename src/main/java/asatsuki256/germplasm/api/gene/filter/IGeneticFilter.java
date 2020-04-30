package asatsuki256.germplasm.api.gene.filter;

import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;

public interface IGeneticFilter {
	
	public boolean isMatched(IGermplasmUnitBase unit);

}
