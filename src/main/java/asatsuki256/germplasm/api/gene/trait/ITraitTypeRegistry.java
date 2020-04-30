package asatsuki256.germplasm.api.gene.trait;

import java.util.Collection;

import asatsuki256.germplasm.api.gene.TraitType;

public interface ITraitTypeRegistry {
	
	public boolean register(TraitType traitType);
	
	public TraitType getTraitType(String id);
	
	public Collection<TraitType> getTraitTypeList();

}
