package asatsuki256.germplasm.core.compat.industrialforegoing;

import com.buuz135.industrial.api.plant.PlantRecollectable;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class IndustrialForegoingCompat {
	
	@SubscribeEvent
    public void register(RegistryEvent.Register<PlantRecollectable> event) {
		IForgeRegistry<PlantRecollectable> registry = event.getRegistry();
        registry.register(new GermplasmPlantRecollectable());
	}
}
