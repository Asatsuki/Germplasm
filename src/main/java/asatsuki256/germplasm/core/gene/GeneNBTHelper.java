package asatsuki256.germplasm.core.gene;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import asatsuki256.germplasm.api.gene.IGeneNBTHelper;
import asatsuki256.germplasm.api.gene.unit.IChromosome;
import asatsuki256.germplasm.api.gene.unit.IChromosomePair;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.api.gene.unit.ITrait;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class GeneNBTHelper implements IGeneNBTHelper{
	
	public static final String GENE_NBT_KEY = NBT_PREFIX + "gene";
	public static final String CHROMOSOME_NBT_KEY = NBT_PREFIX + "chromosome";
	public static final String PAIR_NBT_KEY = NBT_PREFIX + "pair";
	public static final String GENOME_NBT_KEY = NBT_PREFIX + "genome";
	
	private void setUnitBaseToNBT(NBTTagCompound nbt, IGermplasmUnitBase unitBase) {
		nbt.merge(unitBase.serializeNBT());
	}
	
	private void getUnitBaseFromNBT(NBTTagCompound nbt, IGermplasmUnitBase unitBase) {
		unitBase.deserializeNBT(nbt);
	}
	
	public void setTraitToNBT(NBTTagCompound nbt, ITrait trait) {
		nbt.merge(trait.serializeNBT());
	}
	
	public ITrait getTraitFromNBT(NBTTagCompound nbt) {
		Trait trait = new Trait();
		trait.deserializeNBT(nbt);
		getUnitBaseFromNBT(nbt, trait);
		return trait;
	}
	
	public void setGeneToNBT(NBTTagCompound nbt, IGene gene) {
		nbt.merge(gene.serializeNBT());
	}
	
	public IGene getGeneFromNBT(NBTTagCompound nbt) {
		Gene gene = new Gene();
		gene.deserializeNBT(nbt);
		return gene;
	}
	
	public void setChromosomeToNBT(NBTTagCompound nbt, IChromosome chromosome) {
		nbt.merge(chromosome.serializeNBT());
	}
	
	public IChromosome getChromosomeFromNBT(NBTTagCompound nbt) {
		Chromosome chromosome = new Chromosome();
		chromosome.deserializeNBT(nbt);
		return chromosome;
	}
	
	public void setChromosomePairToNBT(NBTTagCompound nbt, IChromosomePair chromosomePair) {
		nbt.merge(chromosomePair.serializeNBT());
	}
	
	public IChromosomePair getChromosomePairFromNBT(NBTTagCompound nbt) {
		ChromosomePair pair = new ChromosomePair();
		pair.deserializeNBT(nbt);
		return pair;
	}
	
	public void setGenomeToNBT(NBTTagCompound nbt, IGenome genome) {
		nbt.merge(genome.serializeNBT());
	}
	
	public IGenome getGenomeFromNBT(NBTTagCompound nbt) {
		Genome genome = new Genome();
		genome.deserializeNBT(nbt);
		return genome;
	}
	
	public void setGenomeToIndividualNBT(NBTTagCompound nbt, IGenome genome) {
		NBTTagCompound nbtGene = new NBTTagCompound();
		setGenomeToNBT(nbtGene, genome);
		nbt.setTag(GENOME_NBT_KEY, nbtGene);
	}
	
	public IGenome getGenomeFromIndividualNBT(NBTTagCompound nbt) {
		if(nbt != null && nbt.hasKey(GENOME_NBT_KEY, Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound genomeNBT = (NBTTagCompound) nbt.getTag(GENOME_NBT_KEY);
			IGenome genome = getGenomeFromNBT(genomeNBT);
			return genome;
		}
		return null;
	}
	
	public void setUnitToindividualNBT(NBTTagCompound nbt, IGermplasmUnitBase unit) {
		NBTTagCompound unitNBT = new NBTTagCompound();
		if(unit instanceof IGenome) {
			setGenomeToNBT(unitNBT, (IGenome)unit);
			nbt.setTag(GENOME_NBT_KEY, unitNBT);
		}else if(unit instanceof IChromosomePair) {
			setChromosomePairToNBT(unitNBT, (IChromosomePair)unit);
			nbt.setTag(PAIR_NBT_KEY, unitNBT);
		}else if(unit instanceof IChromosome) {
			setChromosomeToNBT(unitNBT, (IChromosome)unit);
			nbt.setTag(CHROMOSOME_NBT_KEY, unitNBT);
		}else if(unit instanceof IGene) {
			setGeneToNBT(unitNBT, (IGene)unit);
			nbt.setTag(GENE_NBT_KEY, unitNBT);
		}
	}
	
	public IGermplasmUnitBase getUnitFromIndividualNBT(NBTTagCompound nbt) {
		if(nbt != null) {
			if(nbt.hasKey(GENOME_NBT_KEY, Constants.NBT.TAG_COMPOUND)) {
				return getGenomeFromNBT((NBTTagCompound) nbt.getTag(GENOME_NBT_KEY));
			}else if(nbt.hasKey(PAIR_NBT_KEY, Constants.NBT.TAG_COMPOUND)) {
				return getChromosomePairFromNBT((NBTTagCompound) nbt.getTag(PAIR_NBT_KEY));
			}else if(nbt.hasKey(CHROMOSOME_NBT_KEY, Constants.NBT.TAG_COMPOUND)) {
				return getChromosomeFromNBT((NBTTagCompound) nbt.getTag(CHROMOSOME_NBT_KEY));
			}else if(nbt.hasKey(GENE_NBT_KEY, Constants.NBT.TAG_COMPOUND)) {
				return getGeneFromNBT((NBTTagCompound) nbt.getTag(GENE_NBT_KEY));
			}
		}
		return null;
	}
}
