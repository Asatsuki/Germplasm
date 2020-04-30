package asatsuki256.germplasm.api.gene.unit;

import java.util.List;

import asatsuki256.germplasm.api.gene.TraitType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGermplasmUnitBase extends INBTSerializable<NBTTagCompound> {

	public String getName();
	
	public void setName(String name);
	
	public String getDisplayName();
	
	public String getDisplayName(int number);
	
	public boolean isAnalyzed();
	
	public void setAnalyzed(boolean isAnalyzed);
	
	public int analyze(boolean simulated);

	public int getTotalStrength(TraitType traitType);
	
	public List<TraitType> getTraitTypes();
	
	public List<IGermplasmUnitBase> getChilds();

}
