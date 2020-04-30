package asatsuki256.germplasm.api.gene.unit;

import java.util.List;

public interface IGene extends IGermplasmUnitBase {
	
	public List<ITrait> getTraits();

	public void setTraits(List<ITrait> traits);

	public boolean isDominant();
	
}
