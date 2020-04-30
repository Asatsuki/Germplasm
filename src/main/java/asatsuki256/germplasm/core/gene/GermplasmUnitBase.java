package asatsuki256.germplasm.core.gene;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.ArrayList;
import java.util.List;

import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract class GermplasmUnitBase implements IGermplasmUnitBase {

	public static final String NAME_NBT_KEY = NBT_PREFIX + "name";
	public static final String ANALYZED_NBT_KEY = NBT_PREFIX + "isAnalyzed";
	
	protected String name;
	protected String defaultName;
	protected boolean isAnalyzed;
	
	protected GermplasmUnitBase() {
		super();
		this.name = null;
		defaultName = "Unknown";
		isAnalyzed = false;
	}
	
	protected GermplasmUnitBase(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisplayName() {
		if(name != null) {
			return name;
		} else {
			return defaultName;
		}
	}
	
	public String getDisplayName(int number) {
		if(name != null) {
			return name;
		} else {
			return defaultName + " " + number;
		}
	}

	public boolean isAnalyzed() {
		return isAnalyzed;
	}

	public void setAnalyzed(boolean isAnalyzed) {
		this.isAnalyzed = isAnalyzed;
	}
	
	@Override
	public int getTotalStrength(TraitType traitType) {
		int totalStrength = 0;
		for(IGermplasmUnitBase unit : getChilds()) {
			totalStrength += unit.getTotalStrength(traitType);
		}
		return totalStrength;
	}
	
	@Override
	public List<TraitType> getTraitTypes(){
		List<TraitType> traitTypes = new ArrayList<TraitType>();
		for (IGermplasmUnitBase base : getChilds()) {
			List<TraitType> childTraitTypes = base.getTraitTypes();
			for(TraitType type : childTraitTypes) {
				if(!traitTypes.contains(type)) traitTypes.add(type);
			}
		}
		return traitTypes;
	}
	
	public abstract List<IGermplasmUnitBase> getChilds();
	
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if(this.getName() != null) {
			nbt.setString(NAME_NBT_KEY, this.getName());
		}
		nbt.setBoolean(ANALYZED_NBT_KEY, this.isAnalyzed());
		return nbt;
	}
	
	public void deserializeNBT(NBTTagCompound nbt) {
		String nameFromNBT = null;
		if (nbt != null) {
			if (nbt.hasKey(NAME_NBT_KEY)) {
				nameFromNBT = nbt.getString(NAME_NBT_KEY);
			}
			if (nbt.hasKey(ANALYZED_NBT_KEY)) {
				this.setAnalyzed(nbt.getBoolean(ANALYZED_NBT_KEY));
			}
		}
		this.setName(nameFromNBT);
	}
	
	
}
