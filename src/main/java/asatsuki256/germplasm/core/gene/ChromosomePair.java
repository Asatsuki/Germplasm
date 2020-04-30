package asatsuki256.germplasm.core.gene;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.unit.IChromosome;
import asatsuki256.germplasm.api.gene.unit.IChromosomePair;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class ChromosomePair extends GermplasmUnitBase implements IChromosomePair {
	
	public static final String CHROMOSOMES_NBT_KEY = NBT_PREFIX + "chromosomes";
	
	private List<Chromosome> chromosomes = new ArrayList<Chromosome>();
	
	public ChromosomePair(Chromosome chromosomeX, Chromosome chromosomeY) {
		chromosomes.add(chromosomeX);
		chromosomes.add(chromosomeY);
		this.defaultName = "Pair";
	}
	
	public ChromosomePair(List<Chromosome> chromosomes) {
		this.chromosomes = chromosomes;
		this.defaultName = "Pair";
	}
	
	public ChromosomePair() {
		this.defaultName = "Pair";
	}

	public List<IChromosome> getChromosomes() {
		List<IChromosome> iChromosomes = new ArrayList<IChromosome>();
		for(Chromosome chromosome : chromosomes) {
			iChromosomes.add(chromosome);
		}
		return iChromosomes;
	}

	public void setChromosomes(List<IChromosome> chromosomes) {
		List<Chromosome> chromosomesTemp = new ArrayList<Chromosome>();
		for(IChromosome chromosome : chromosomes) {
			chromosomesTemp.add((Chromosome)chromosome);
		}
		this.chromosomes = chromosomesTemp;
	}
	
	public IChromosome getRandomChromosome(Random random) {
		return chromosomes.get(random.nextInt(chromosomes.size()));
	}
	
	@Override
	public int getTotalStrength(TraitType traitType) {
		int totalStrength = 0;
		for(IChromosome unit : chromosomes) {
			totalStrength += unit.getTotalStrength(traitType); //優性のものだけカウント
			List<IGene> recessives = new ArrayList();
			for(IChromosome unit2 : chromosomes) {
				if(unit != unit2) {
					recessives.addAll(unit2.getGenes()); //調べているもの以外の染色体の遺伝子全て
				}
			}
			for(IGene gene : unit.getGenes()) {
				if(!gene.isDominant() && recessives.contains(gene)){
					totalStrength += gene.getTotalStrength(traitType);
				}
			}
		}
		return totalStrength;
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
		for(Chromosome chromosome : chromosomes) {
			analyzed += chromosome.analyze(simulated);
		}
		return analyzed;
	}
	
	@Override
	public List<IGermplasmUnitBase> getChilds(){
		List<IGermplasmUnitBase> units = new ArrayList<IGermplasmUnitBase>();
		for(Chromosome chromosome : this.chromosomes) {
			units.add(chromosome);
		}
		return units;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		NBTTagList chromosomes = new NBTTagList();
		for (IChromosome chromosome : this.getChromosomes()) {
			NBTTagCompound chromosomeTag = chromosome.serializeNBT();
			chromosomes.appendTag(chromosomeTag);
		}
		nbt.setTag(CHROMOSOMES_NBT_KEY, chromosomes);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		if (nbt != null && nbt.hasKey(CHROMOSOMES_NBT_KEY, Constants.NBT.TAG_LIST)) {
			NBTTagList chromosomesTag = nbt.getTagList(CHROMOSOMES_NBT_KEY, Constants.NBT.TAG_COMPOUND);
			for (NBTBase chromosomeBase : chromosomesTag) {
				NBTTagCompound chromosomeTag = (NBTTagCompound)chromosomeBase;
				Chromosome chromosome = new Chromosome();
				chromosome.deserializeNBT(chromosomeTag);
				chromosomes.add(chromosome);
			}
		}
		ChromosomePair chromosomePair = new ChromosomePair(chromosomes.get(0), chromosomes.get(1));
		this.chromosomes = chromosomes;
	}
	
	@Override
	public boolean insertGene(IGene gene, Random random) {
		List<IChromosome> childs = this.getChromosomes();
		if (childs.isEmpty()) {
			return false;
		}
		int index = random.nextInt(childs.size());
		IChromosome target = childs.get(index);
		List<IChromosome> newChilds = new ArrayList<>(childs);
		target.insertGene(gene, random);
		newChilds.set(index, target);
		this.setChromosomes(newChilds);
		return true;
	}
	
}
