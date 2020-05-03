package asatsuki256.germplasm.core.gene;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.ArrayList;
import java.util.List;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.api.gene.unit.ITrait;
import net.minecraft.nbt.NBTTagCompound;

public class Trait extends GermplasmUnitBase implements ITrait {
	
	public static final String TRAITTYPE_NBT_KEY = NBT_PREFIX + "traitType";
	public static final String STRENGTH_NBT_KEY = NBT_PREFIX + "traitStrength";
	public static final String TRAIT_TAG_NBT_KEY = NBT_PREFIX + "traitTag";
	
	private TraitType traitType;
	private int traitStrength;
	private NBTTagCompound tagCompound;
	
	public Trait(TraitType type, int strength) {
		this.traitType = type;
		this.traitStrength = strength;
		this.tagCompound = new NBTTagCompound();
		resetDefaultName();
	}
	
	public Trait() {
		
	}

	public TraitType getTraitType() {
		return traitType;
	}
	
	public void setTraitType(TraitType type) {
		this.traitType = type;
		resetDefaultName();
	}
	
	public int getTraitStrength() {
		return traitStrength;
	}
	
	public void setTraitStrength(int strength) {
		this.traitStrength = strength;
		resetDefaultName();
	}
	
	public NBTTagCompound gettagCompound() {
		return this.tagCompound;
	}
	
	public void setTagCompound(NBTTagCompound nbt) {
		this.tagCompound = nbt;
	}
	
	private void resetDefaultName() {
		this.defaultName = traitType.getTraitId() + " " + traitStrength;
	}
	
	@Override
	public int analyze(boolean simulated) {
		int analyzed = 0;
		if(!isAnalyzed) {
			if(!simulated) {
				setAnalyzed(true);
			}
			analyzed++;
		}
		return analyzed;
	}
	
	@Override
	public int getTotalStrength(TraitType traitType) {
		if(this.traitType == traitType) {
			return this.traitStrength;
		}
		return 0;
	}
	
	@Override
	public List<TraitType> getTraitTypes(){
		List<TraitType> traitTypes = new ArrayList<TraitType>();
		traitTypes.add(traitType);
		return traitTypes;
	}
	
	@Override
	public List<IGermplasmUnitBase> getChilds() {
		return new ArrayList<IGermplasmUnitBase>();
	}
	
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setString(TRAITTYPE_NBT_KEY, this.getTraitType().getTraitId());
		nbt.setInteger(STRENGTH_NBT_KEY, this.getTraitStrength());
		nbt.setTag(TRAIT_TAG_NBT_KEY, tagCompound);
		return nbt;
	}
	
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		String traitTypeFromNBT = null;
		int strengthFromNBT = 0;
		NBTTagCompound tagFromNBT = new NBTTagCompound();
		if (nbt != null && nbt.hasKey(TRAITTYPE_NBT_KEY)) {
			traitTypeFromNBT = nbt.getString(TRAITTYPE_NBT_KEY);
		}
		if (nbt != null && nbt.hasKey(STRENGTH_NBT_KEY)) {
			strengthFromNBT = nbt.getInteger(STRENGTH_NBT_KEY);
		}
		if (nbt != null && nbt.hasKey(TRAIT_TAG_NBT_KEY)) {
			tagFromNBT = nbt.getCompoundTag(TRAIT_TAG_NBT_KEY);
		}
		this.setTraitType(GeneAPI.traitTypeRegistry.getTraitType(traitTypeFromNBT));
		this.setTraitStrength(strengthFromNBT);
		this.setTagCompound(tagFromNBT);
	}
	
	/*
	 * TraitTypeとTraitStrengthが同じならTrue
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ITrait) {
			if(!this.getTraitType().equals(((ITrait) obj).getTraitType())) return false;
			if(this.getTraitStrength() != ((ITrait) obj).getTraitStrength()) return false;
			return true;
		}
		return false;
	}
	
}
