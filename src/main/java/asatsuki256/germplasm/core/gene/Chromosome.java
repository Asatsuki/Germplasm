package asatsuki256.germplasm.core.gene;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.unit.IChromosome;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class Chromosome extends GermplasmUnitBase implements IChromosome {

	public static final String GENES_NBT_KEY = NBT_PREFIX + "genes";
	
	private List<Gene> genes;
	
	public Chromosome(List<Gene> genes) {
		this.genes = genes;
		this.defaultName = "Chromosome";
	}
	
	public Chromosome(Gene... genes) {
		this.genes = new ArrayList<Gene>(Arrays.asList(genes));
		this.defaultName = "Chromosome";
	}
	
	public Chromosome() {
		this.defaultName = "Chromosome";
	}

	public List<IGene> getGenes() {
		List<IGene> iGenes = new ArrayList<IGene>();
		for(Gene gene : genes) {
			iGenes.add((IGene)gene);
		}
		return iGenes;
	}

	public void setGenes(List<IGene> genes) {
		List<Gene> genesTemp = new ArrayList<Gene>();
		for(IGene gene : genes) {
			genesTemp.add((Gene)gene);
		}
		this.genes = genesTemp;
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
		for(Gene gene : genes) {
			analyzed += gene.analyze(simulated);
		}
		return analyzed;
	}
	
	@Override
	public List<IGermplasmUnitBase> getChilds(){
		List<IGermplasmUnitBase> units = new ArrayList<IGermplasmUnitBase>();
		for(Gene gene : this.genes) {
			units.add(gene);
		}
		return units;
	}
	
	@Override
	public int getTotalStrength(TraitType traitType) {
		int totalStrength = 0;
		for(IGene unit : genes) {
			if(unit.isDominant()) {
				totalStrength += unit.getTotalStrength(traitType);
			}
		}
		return totalStrength;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		NBTTagList genes = new NBTTagList();
		for (IGene gene : this.getGenes()) {
			NBTTagCompound geneTag = gene.serializeNBT();
			genes.appendTag(geneTag);
		}
		nbt.setTag(GENES_NBT_KEY, genes);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		List<Gene> genes = new ArrayList<Gene>();
		if (nbt != null && nbt.hasKey(GENES_NBT_KEY, Constants.NBT.TAG_LIST)) {
			NBTTagList genesTag = nbt.getTagList(GENES_NBT_KEY, Constants.NBT.TAG_COMPOUND);
			for (NBTBase geneBase : genesTag) {
				NBTTagCompound geneNBT = (NBTTagCompound)geneBase;
				Gene gene = new Gene();
				gene.deserializeNBT(geneNBT);
				genes.add(gene);
			}
		}
		this.genes = genes;
	}
	
	@Override
	public boolean insertGene(IGene gene, Random random) {
		List<IGene> childs = this.getGenes();
		if (childs.isEmpty()) {
			return false;
		}
		int index = random.nextInt(childs.size());
		
		IGene insertable = childs.get(index);
		List<IGene> newChilds = new ArrayList<>(childs);
		//置換
		newChilds.set(index, gene);
		this.setGenes(newChilds);
		return true;
	}
	
	
}
