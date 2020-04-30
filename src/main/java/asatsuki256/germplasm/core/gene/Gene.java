package asatsuki256.germplasm.core.gene;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.api.gene.unit.ITrait;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class Gene extends GermplasmUnitBase implements IGene {
	
	public static final String DOMINANT_NBT_KEY = NBT_PREFIX + "isDominant";
	public static final String TRAITS_NBT_KEY = NBT_PREFIX + "traits";
	
	private boolean isDominant;
	private List<Trait> traits;
	
	public Gene(boolean isDominant, List<Trait> traits) {
		this.traits = traits;
		this.defaultName = "Gene";
	}
	
	public Gene(boolean isDominant, Trait... traits) {
		this.isDominant = isDominant;
		this.traits = new ArrayList<Trait>(Arrays.asList(traits));
		this.defaultName = "Gene";
	}

	public Gene() {
		this.defaultName = "Gene";
	}

	public boolean isDominant() {
		return isDominant;
	}

	public void setDominant(boolean isDominant) {
		this.isDominant = isDominant;
	}

	public List<ITrait> getTraits() {
		List<ITrait> iTraits = new ArrayList<ITrait>();
		for(Trait trait : traits) {
			iTraits.add(trait);
		}
		return iTraits;
	}
	
	public void setTraits(List<ITrait> traits) {
		List<Trait> traitsTemp = new ArrayList<Trait>();
		for(ITrait trait : traits) {
			traitsTemp.add((Trait)trait);
		}
		this.traits = traitsTemp;
	}
	
	@Override
	public String getDisplayName() {
		if(name != null) {
			return name;
		} else {
			if(traits.size() == 1) {
				return traits.get(0).getDisplayName();
			}else if(traits.size() > 0) {
				return traits.get(0).getDisplayName() + "...";
			}
		}
		return defaultName;
	}
	
	@Override
	public String getDisplayName(int number) {
		return getDisplayName();
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
		for(Trait trait : traits) {
			analyzed += trait.analyze(simulated);
		}
		return analyzed;
	}
	
	public List<IGermplasmUnitBase> getChilds(){
		List<IGermplasmUnitBase> units = new ArrayList<IGermplasmUnitBase>();
		for(Trait trait : this.traits) {
			units.add(trait);
		}
		return units;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setBoolean(DOMINANT_NBT_KEY, this.isDominant());
		NBTTagList traits = new NBTTagList();
		for (ITrait trait : this.traits) {
			NBTTagCompound traitTag = trait.serializeNBT();
			traits.appendTag(traitTag);
		}
		nbt.setTag(TRAITS_NBT_KEY, traits);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		boolean isDominant = true;
		List<Trait> traits = new ArrayList<Trait>();
		if (nbt != null && nbt.hasKey(DOMINANT_NBT_KEY)) {
			isDominant = nbt.getBoolean(DOMINANT_NBT_KEY);
		}
		if (nbt != null && nbt.hasKey(TRAITS_NBT_KEY, Constants.NBT.TAG_LIST)) {
			NBTTagList traitsTag = nbt.getTagList(TRAITS_NBT_KEY, Constants.NBT.TAG_COMPOUND);
			for (NBTBase traitBase : traitsTag) {
				NBTTagCompound traitNBT = (NBTTagCompound)traitBase;
				Trait trait = new Trait();
				trait.deserializeNBT(traitNBT);
				traits.add(trait);
			}
		}
		this.isDominant = isDominant;
		this.traits = traits;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IGene) {
			if(this.isDominant() != ((IGene) obj).isDominant()) return false;
			if(!this.getTraits().equals(((IGene) obj).getTraits())) {
				return false;
			}
			return true;
		}
		return false;
	}
	
}
