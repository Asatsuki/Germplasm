package asatsuki256.germplasm.api.gene;

import asatsuki256.germplasm.api.gene.unit.IGene;
import net.minecraft.util.WeightedRandom;

public class GeneWithWeight extends WeightedRandom.Item{
	
	public IGene gene;
	
	public GeneWithWeight(IGene gene, int weight) {
		super(weight);
		this.gene = gene;
	}

}
