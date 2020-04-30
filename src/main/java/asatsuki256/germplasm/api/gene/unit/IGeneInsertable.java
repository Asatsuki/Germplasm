package asatsuki256.germplasm.api.gene.unit;

import java.util.Random;

public interface IGeneInsertable extends IGermplasmUnitBase {
	
	//ランダムな遺伝子を別の遺伝子で置換挿入
	public boolean insertGene(IGene gene, Random random);

}
