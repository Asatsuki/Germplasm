package asatsuki256.germplasm.core.gene;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.gene.unit.IChromosomePair;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class Genome extends GermplasmUnitBase implements IGenome {

	public static final String PAIRS_NBT_KEY = NBT_PREFIX + "pairs";
	public static final String GENERATION_NBT_KEY = NBT_PREFIX + "generation";
	
	private List<ChromosomePair> pairs;
	private int generation;
	
	public Genome(List<ChromosomePair> pairs) {
		this.pairs = pairs;
		this.defaultName = "Genome";
		this.generation = 0;
	}
	
	public Genome(ChromosomePair... pairs) {
		this.pairs = new ArrayList<ChromosomePair>(Arrays.asList(pairs));
		this.defaultName = "Genome";
		this.generation = 0;
	}

	public List<IChromosomePair> getPairs() {
		List<IChromosomePair> iPairs = new ArrayList<IChromosomePair>();
		for(ChromosomePair pair : pairs) {
			iPairs.add(pair);
		}
		return iPairs;
	}

	public void setPairs(List<IChromosomePair> pairs) {
		List<ChromosomePair> pairsTemp = new ArrayList<ChromosomePair>();
		for(IChromosomePair pair : pairs) {
			pairsTemp.add((ChromosomePair)pair);
		}
		this.pairs = pairsTemp;
	}
	
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	
	public int getGeneration() {
		return this.generation;
	}
	
	public void resetGeneration() {
		this.generation = 0;
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
		for(ChromosomePair pair : pairs) {
			analyzed += pair.analyze(simulated);
		}
		return analyzed;
	}
	
	@Override
	public List<IGermplasmUnitBase> getChilds(){
		List<IGermplasmUnitBase> units = new ArrayList<IGermplasmUnitBase>();
		for(ChromosomePair pair : this.pairs) {
			units.add(pair);
		}
		return units;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		NBTTagList pairs = new NBTTagList();
		for (IChromosomePair pair : this.getPairs()) {
			NBTTagCompound pairTag = pair.serializeNBT();
			pairs.appendTag(pairTag);
		}
		nbt.setTag(PAIRS_NBT_KEY, pairs);
		nbt.setInteger(GENERATION_NBT_KEY, this.generation);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		List<ChromosomePair> pairs = new ArrayList<ChromosomePair>();
		if (nbt != null && nbt.hasKey(PAIRS_NBT_KEY, Constants.NBT.TAG_LIST)) {
			NBTTagList pairsTag = nbt.getTagList(PAIRS_NBT_KEY, Constants.NBT.TAG_COMPOUND);
			for (NBTBase pairBase : pairsTag) {
				NBTTagCompound pairNBT = (NBTTagCompound)pairBase;
				ChromosomePair pair = new ChromosomePair();
				pair.deserializeNBT(pairNBT);
				pairs.add(pair);
			}
			this.pairs = pairs;
		}
		if(nbt.hasKey(GENERATION_NBT_KEY, Constants.NBT.TAG_INT)) {
			this.generation = nbt.getInteger(GENERATION_NBT_KEY);
		}
	}
	
	public boolean insertGene(IGene gene, Random random) {
		List<IChromosomePair> childs = this.getPairs();
		if (childs.isEmpty()) {
			return false;
		}
		int index = random.nextInt(childs.size());
		
		IChromosomePair target = childs.get(index);
		List<IChromosomePair> newChilds = new ArrayList<>(childs);
		target.insertGene(gene, random);
		newChilds.set(index, target);
		this.setPairs(newChilds);
		return true;
	}
	
}
