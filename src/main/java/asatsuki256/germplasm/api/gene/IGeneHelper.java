package asatsuki256.germplasm.api.gene;

import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import net.minecraft.util.ResourceLocation;

public interface IGeneHelper {
	
	/*
	 * 2つのゲノムからメンデルの法則に従い子を作る
	 * 組み換え価は0、突然変異も起きない
	 */
	public IGenome getOffspring(IGenome genomeA, IGenome genomeB);
	
	/*
	 * UnitからTraitTypeHarvestのListを取得
	 */
	public List<TraitTypeHarvest> getHarvestFromUnit(IGermplasmUnitBase unit);
	
	/*
	 * Unitから作物の見た目を左右するTraitTypeHarvestを取得
	 */
	public TraitTypeHarvest getPrimaryHarvest(IGermplasmUnitBase unit);
	
	/*
	 * Unitから作物のテクスチャを取得
	 */
	public ResourceLocation[] getCropTexture(IGermplasmUnitBase unit);
	
	/*
	 * genePoolから選んだGeneを含むGenomeを返す
	 */
	public IGenome getGenomeFromPool(Random random, GenePool genePool, int pairCount);
	
	/*
	 * GenomeからランダムなGeneを1個返す
	 */
	public IGene getRandomGeneFromGenome(IGenome genome, Random random);
	
	/*
	 * genePoolから選んだGeneを含むChromosomePairを含むGenomeを返す
	 */
	public IGenome getGenomeFromPool(Random random, GenePool... genePool);

}
