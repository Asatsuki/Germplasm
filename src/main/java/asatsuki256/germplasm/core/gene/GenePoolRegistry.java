package asatsuki256.germplasm.core.gene;

import java.util.HashMap;

import asatsuki256.germplasm.api.gene.GenePool;
import asatsuki256.germplasm.api.gene.IGenePoolRegistry;
import asatsuki256.germplasm.core.gene.trait.TraitTypeRegistry;
import asatsuki256.germplasm.core.item.GermplasmItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GenePoolRegistry implements IGenePoolRegistry{
	
	private static HashMap<String, GenePool> poolMap;
	private static HashMap<ItemStack, String> itemForPool;
	
	public GenePoolRegistry() {
		poolMap = new HashMap<String, GenePool>();
		itemForPool = new HashMap<ItemStack, String>();
		init();
	}
	
	public boolean registerGenePool(GenePool pool, String id) {
		if(!poolMap.containsKey(id)) {
			poolMap.put(id, pool);
			return true;
		}
		return false;
	}
	
	public boolean registerGenePool(GenePool pool, String id, ItemStack item) {
		if (registerGenePool(pool, id)) {
			itemForPool.put(item, id);
			return true;
		}
		return false;
	}
	
	public GenePool getPoolFromItem(ItemStack itemstack) {
		for (ItemStack item : itemForPool.keySet()) {
			if(ItemStack.areItemsEqual(item, itemstack)) {
				return poolMap.get(itemForPool.get(item));
			}
		}
		return null;
	}
	
	public GenePool pool(String id) {
		if(poolMap.containsKey(id)) {
			return poolMap.get(id);
		}
		return null;
	}

	public static GenePool general;
	public static GenePool wheat;
	public static GenePool carrot;
	public static GenePool waterlily;
	public static GenePool rose;
	public static GenePool tomato;
	public static GenePool villager_default;
	
	private void init() {
		general = new GenePool();
		general.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 1)), 80);
		general.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 2)), 60);
		general.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 3)), 40);
		general.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 1)), 100);
		general.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 2)), 75);
		general.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 3)), 50);
		general.add(new Gene(false, new Trait(TraitTypeRegistry.growth, -2)), 50);
		general.add(new Gene(false, new Trait(TraitTypeRegistry.heat_tolerance, 1)), 40);
		general.add(new Gene(false, new Trait(TraitTypeRegistry.cold_tolerance, 1)), 40);
		registerGenePool(general, "general");
		wheat = new GenePool();
		wheat.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 2)), 400);
		wheat.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 1)), 800);
		wheat.add(new Gene(false, new Trait(TraitTypeRegistry.growth, 2)), 800);
		wheat.add(new Gene(false, new Trait(TraitTypeRegistry.cold_tolerance, 1)), 200);
		wheat.add(new Gene(true, new Trait(TraitTypeRegistry.wheat, 1)), 1200);
		wheat.add(new Gene(true, new Trait(TraitTypeRegistry.redstorgham, 1)), 3);
		registerGenePool(wheat, "wheat", new ItemStack(Items.WHEAT_SEEDS));
		carrot = new GenePool();
		carrot.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 1)), 800);
		carrot.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 2)), 600);
		carrot.add(new Gene(false, new Trait(TraitTypeRegistry.gain, 3)), 400);
		carrot.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 1)), 600);
		carrot.add(new Gene(false, new Trait(TraitTypeRegistry.growth, 2)), 500);
		carrot.add(new Gene(false, new Trait(TraitTypeRegistry.growth, 3)), 300);
		carrot.add(new Gene(false, new Trait(TraitTypeRegistry.heat_tolerance, 1)), 200);
		carrot.add(new Gene(true, new Trait(TraitTypeRegistry.carrot, 1)), 1200);
		carrot.add(new Gene(true, new Trait(TraitTypeRegistry.golden_carrot, 1)), 5);
		registerGenePool(carrot, "carrot", new ItemStack(Items.CARROT));
		waterlily = new GenePool();
		waterlily.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 1)), 600);
		waterlily.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 2)), 500);
		waterlily.add(new Gene(false, new Trait(TraitTypeRegistry.gain, 3)), 200);
		waterlily.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 1)), 800);
		waterlily.add(new Gene(false, new Trait(TraitTypeRegistry.growth, 2)), 700);
		waterlily.add(new Gene(false, new Trait(TraitTypeRegistry.growth, 3)), 200);
		waterlily.add(new Gene(false, new Trait(TraitTypeRegistry.growth, 1)), 200);
		waterlily.add(new Gene(false, new Trait(TraitTypeRegistry.gain, 2), new Trait(TraitTypeRegistry.growth, 2)), 200);
		waterlily.add(new Gene(true, new Trait(TraitTypeRegistry.waterlily, 1)), 1200);
		waterlily.add(new Gene(true, new Trait(TraitTypeRegistry.claylily, 1)), 1);
		registerGenePool(waterlily, "waterlily", new ItemStack(Blocks.WATERLILY));
		rose = new GenePool();
		rose.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 1)), 1800);
		rose.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 2)), 100);
		rose.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 1)), 1500);
		rose.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 2)), 100);
		rose.add(new Gene(true, new Trait(TraitTypeRegistry.rose, 1)), 1200);
		registerGenePool(rose, "rose", new ItemStack(Blocks.DOUBLE_PLANT, 1, 4));
		tomato = new GenePool();
		tomato.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 1)), 1200);
		tomato.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 2)), 600);
		tomato.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 1)), 200);
		tomato.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 2)), 500);
		tomato.add(new Gene(true, new Trait(TraitTypeRegistry.tomato, 1)), 1200);
		registerGenePool(tomato, "tomato", new ItemStack(GermplasmItems.tomato));
		villager_default = new GenePool();
		villager_default.add(new Gene(true, new Trait(TraitTypeRegistry.gain, 2)), 80);
		villager_default.add(new Gene(true, new Trait(TraitTypeRegistry.growth, 2)), 80);
		villager_default.add(new Gene(true, new Trait(TraitTypeRegistry.carrot, 1)), 120);
		registerGenePool(villager_default, "villager_default");
	}

}
