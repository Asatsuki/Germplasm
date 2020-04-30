package asatsuki256.germplasm.api.gene.unit;

import asatsuki256.germplasm.api.gene.TraitType;

public interface ITrait extends IGermplasmUnitBase {

	public TraitType getTraitType();
	
	public void setTraitType(TraitType type);
	
	public int getTraitStrength();
	
	public void setTraitStrength(int strength);
	
}
