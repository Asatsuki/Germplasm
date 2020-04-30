package asatsuki256.germplasm.core.compat;

import asatsuki256.germplasm.core.compat.industrialforegoing.IndustrialForegoingCompat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class CompatHandler {
	
	public static void preInit() {
		if (Loader.isModLoaded("industrialforegoing")) {
			MinecraftForge.EVENT_BUS.register(new IndustrialForegoingCompat());
        }
	}

}
