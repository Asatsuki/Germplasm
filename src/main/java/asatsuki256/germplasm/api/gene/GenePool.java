package asatsuki256.germplasm.api.gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.gene.unit.IGene;
import net.minecraft.util.WeightedRandom;

public class GenePool {
	
	private Random rand = new Random();
	private List<GeneWithWeight> pool = new ArrayList<GeneWithWeight>();
	
	public GenePool() {
		
	}

	public void add(IGene gene, int weight) {
		pool.add(new GeneWithWeight(gene, weight));
	}
	
	public void merge(GenePool pool) {
		this.pool.addAll(pool.getPool());
	}
	
	public List<GeneWithWeight> getPool() {
		return pool;
	}
	
	public IGene getWeightedGene() {
		
		if(!pool.isEmpty()) {
			
			return WeightedRandom.getRandomItem(rand, pool).gene;
		}
		return null;
	}
	
	public IGene getWeightedGene(Random random) {
		
		if(!pool.isEmpty()) {
			
			return WeightedRandom.getRandomItem(random, pool).gene;
		}
		return null;
	}
}
