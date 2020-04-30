package asatsuki256.germplasm.core.village;

import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class CareerGenetics extends VillagerCareer {
	
	private final ITradeList[][] trades;
	
	public CareerGenetics(VillagerProfession parent, String name, ITradeList[][] trades){
        super(parent, name);
        this.trades = trades;
    }

}
