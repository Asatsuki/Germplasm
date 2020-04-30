package asatsuki256.germplasm.api.gene;

import asatsuki256.germplasm.api.gene.unit.IChromosome;
import asatsuki256.germplasm.api.gene.unit.IChromosomePair;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.api.gene.unit.ITrait;
import net.minecraft.nbt.NBTTagCompound;

public interface IGeneNBTHelper {
	
	/**
	 * Traitの情報をNBTに書き込む
	 */
	public void setTraitToNBT(NBTTagCompound nbt, ITrait trait);
	
	/**
	 * Traitの情報をNBTから読み出す
	 */
	public ITrait getTraitFromNBT(NBTTagCompound nbt);
	
	/**
	 * Geneの情報をNBTに書き込む
	 */
	public void setGeneToNBT(NBTTagCompound nbt, IGene gene);
	
	/**
	 * Geneの情報をNBTから読み出す
	 */
	public IGene getGeneFromNBT(NBTTagCompound nbt);
	
	/**
	 * Chromosomeの情報をNBTに書き込む
	 */
	public void setChromosomeToNBT(NBTTagCompound nbt, IChromosome chromosome);
	
	/**
	 * Chromosomeの情報をNBTから読み出す
	 */
	public IChromosome getChromosomeFromNBT(NBTTagCompound nbt);
	
	/**
	 * ChromosomePairの情報をNBTに書き込む
	 */
	public void setChromosomePairToNBT(NBTTagCompound nbt, IChromosomePair chromosomePair);
	
	/**
	 * ChromosomePairの情報をNBTから読み出す
	 */
	public IChromosomePair getChromosomePairFromNBT(NBTTagCompound nbt);
	
	/**
	 * Genomeの情報をNBTに書き込む
	 */
	public void setGenomeToNBT(NBTTagCompound nbt, IGenome genome);
	
	/**
	 * Genomeの情報をNBTから読み出す
	 */
	public IGenome getGenomeFromNBT(NBTTagCompound nbt);
	
	/**
	 * Genomeを個体のNBTに格納
	 */
	public void setGenomeToIndividualNBT(NBTTagCompound nbt, IGenome genome);
	
	/**
	 * Genomeを個体のNBTから読み出す
	 * 存在しなければnullを返す
	 */
	public IGenome getGenomeFromIndividualNBT(NBTTagCompound nbt);
	
	/**
	 * Genome、ChromosomePair、Chromosome、Geneのうち保存されているものを個体のNBTから読み出す
	 * 存在しなければnullを返す
	 */
	public IGermplasmUnitBase getUnitFromIndividualNBT(NBTTagCompound nbt);
	
	/**
	 * Genome、ChromosomePair、Chromosome、Geneを個体のNBTに格納
	 */
	public void setUnitToindividualNBT(NBTTagCompound nbt, IGermplasmUnitBase unit);

}
